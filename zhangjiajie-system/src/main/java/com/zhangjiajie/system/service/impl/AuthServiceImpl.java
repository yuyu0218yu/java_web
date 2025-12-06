package com.zhangjiajie.system.service.impl;

import com.zhangjiajie.system.dto.LoginRequest;
import com.zhangjiajie.system.dto.LoginResponse;
import com.zhangjiajie.system.dto.UserInfo;
import com.zhangjiajie.system.dto.RegisterRequest;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.entity.Role;
import com.zhangjiajie.system.entity.UserRole;
import com.zhangjiajie.system.event.LoginEvent;
import com.zhangjiajie.system.mapper.UserMapper;
import com.zhangjiajie.system.mapper.UserRoleMapper;
import com.zhangjiajie.system.service.AuthService;
import com.zhangjiajie.system.service.RoleService;
import com.zhangjiajie.system.service.UserService;
import com.zhangjiajie.common.util.IpUtil;
import com.zhangjiajie.common.util.JwtUtil;
import com.zhangjiajie.common.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserService userService;
    private final RoleService roleService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        HttpServletRequest request = WebUtil.getRequest();
        String ip = IpUtil.getClientIp(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "";

        try {
            // 加载用户信息并验证密码（包含角色信息）
            UserMapper.UserWithRole user = userMapper.selectUserWithRoleByUsername(loginRequest.getUsername());
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            log.debug("登录密码匹配结果: {}", matches);
            if (!matches) {
                throw new RuntimeException("用户名或密码错误");
            }

            // 获取用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // 生成JWT token
            String token = jwtUtil.generateToken(userDetails);

            // 获取用户信息（包含角色）
            UserInfo userInfo = convertToUserInfoWithRole(user);

            // 获取用户权限
            List<String> permissions = getUserPermissions(user.getId());

            LoginResponse response = new LoginResponse(token, userInfo);
            response.setPermissions(permissions);

            log.info("用户 {} 登录成功", loginRequest.getUsername());

            // 发布登录成功事件
            publishLoginEvent(loginRequest.getUsername(), ip, userAgent, true, null);

            return response;

        } catch (Exception e) {
            log.warn("用户 {} 登录失败: {}", loginRequest.getUsername(), e.getMessage());

            // 发布登录失败事件
            publishLoginEvent(loginRequest.getUsername(), ip, userAgent, false, e.getMessage());

            throw new RuntimeException("用户名或密码错误");
        }
    }

    /**
     * 发布登录事件
     */
    private void publishLoginEvent(String username, String ip, String userAgent, Boolean success, String errorMessage) {
        try {
            LoginEvent loginEvent = new LoginEvent(this, username, ip, userAgent, success, errorMessage);
            eventPublisher.publishEvent(loginEvent);
        } catch (Exception e) {
            log.error("发布登录事件失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public Object getUserInfo(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return convertToUserInfo(user);
    }

    @Override
    public UserInfo getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("未登录");
        }
        String username = authentication.getName();
        // 查询用户及角色信息
        var userWithRole = userMapper.selectUserWithRoleByUsername(username);
        if (userWithRole == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserInfoWithRole(userWithRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentUserProfile(String username, String nickname, String phone, String avatar) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 手机号唯一性校验（如果传入）- 使用UserService的检查方法
        if (phone != null && !phone.isBlank()) {
            if (userService.checkPhoneExists(phone, user.getId())) {
                throw new RuntimeException("手机号已被占用");
            }
            user.setPhone(phone);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    /**
     * 用户注册（默认普通用户）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest registerRequest) {
        // 保留账号直接拒绝
        if ("admin".equalsIgnoreCase(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 校验用户名是否重复 - 使用UserService的检查方法
        if (userService.checkUsernameExists(registerRequest.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }

        // 查找普通用户角色 - 使用RoleService
        Role userRole = roleService.getByRoleCode("USER");
        if (userRole == null || (userRole.getDeleted() != null && userRole.getDeleted() == 1)) {
            throw new RuntimeException("普通用户角色不存在，请先初始化角色数据");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setStatus(User.Status.ENABLED.getCode());

        try {
            int inserted = userMapper.insert(user);
            if (inserted != 1) {
                throw new RuntimeException("注册失败，请重试");
            }
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("用户名已存在");
        }
        try {
            // 绑定普通用户角色
            UserRole ur = new UserRole();
            ur.setUserId(user.getId());
            ur.setRoleId(userRole.getId());
            userRoleMapper.insert(ur);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("用户名已存在");
        }

        log.info("用户 {} 注册成功", registerRequest.getUsername());
    }

    /**
     * 将User实体转换为UserInfo DTO
     * 提取公共的转换逻辑，避免重复代码
     */
    private UserInfo convertToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        return userInfo;
    }

    /**
     * 将UserWithRole实体转换为UserInfo DTO（包含角色信息）
     */
    private UserInfo convertToUserInfoWithRole(UserMapper.UserWithRole userWithRole) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userWithRole.getId());
        userInfo.setUsername(userWithRole.getUsername());
        userInfo.setEmail(userWithRole.getEmail());
        userInfo.setPhone(userWithRole.getPhone());
        userInfo.setNickname(userWithRole.getNickname());
        userInfo.setAvatar(userWithRole.getAvatar());
        userInfo.setRoleName(userWithRole.getRoleName());
        userInfo.setRoleCode(userWithRole.getRoleCode());
        userInfo.setCreatedAt(userWithRole.getCreateTime());
        return userInfo;
    }

    @Override
    public String refreshToken(String token) {
        try {
            // 验证token
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Token无效");
            }

            // 提取用户名
            String username = jwtUtil.extractUsername(token);

            // 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 生成新token
            String newToken = jwtUtil.generateToken(userDetails);

            log.info("用户 {} 刷新token成功", username);
            return newToken;

        } catch (Exception e) {
            log.warn("刷新token失败: {}", e.getMessage());
            throw new RuntimeException("刷新token失败");
        }
    }

    /**
     * 获取用户权限列表
     */
    private List<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        // 从数据库查询真实权限
        List<String> permissions = userMapper.selectUserPermissions(userId);
        return permissions != null ? permissions : new ArrayList<>();
    }
}