package com.yushuang.demo.service.impl;

import com.yushuang.demo.dto.LoginRequest;
import com.yushuang.demo.dto.LoginResponse;
import com.yushuang.demo.dto.UserInfo;
import com.yushuang.demo.entity.User;
import com.yushuang.demo.event.LoginEvent;
import com.yushuang.demo.mapper.UserMapper;
import com.yushuang.demo.service.AuthService;
import com.yushuang.demo.util.IpUtil;
import com.yushuang.demo.util.JwtUtil;
import com.yushuang.demo.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        HttpServletRequest request = WebUtil.getRequest();
        String ip = IpUtil.getClientIp(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "";

        try {
            // 验证用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // 获取用户详情
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 生成JWT token
            String token = jwtUtil.generateToken(userDetails);

            // 获取用户信息
            User user = userMapper.selectByUsername(loginRequest.getUsername());
            UserInfo userInfo = convertToUserInfo(user);

            // 获取用户权限（这里简化处理，实际应该从数据库获取）
            List<String> permissions = getUserPermissions(user.getId());

            LoginResponse response = new LoginResponse(token, userInfo);
            response.setPermissions(permissions);

            log.info("用户 {} 登录成功", loginRequest.getUsername());

            // 发布登录成功事件
            publishLoginEvent(loginRequest.getUsername(), ip, userAgent, true, null);

            return response;

        } catch (AuthenticationException e) {
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
        // 这里简化处理，实际应该从数据库查询用户的角色和权限
        List<String> permissions = new ArrayList<>();
        permissions.add("user:view");
        permissions.add("user:create");
        permissions.add("role:view");
        permissions.add("*"); // 超级管理员权限
        return permissions;
    }
}