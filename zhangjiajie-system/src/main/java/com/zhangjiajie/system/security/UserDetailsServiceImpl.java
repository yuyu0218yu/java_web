package com.zhangjiajie.system.security;

import com.zhangjiajie.common.security.LoginUser;
import com.zhangjiajie.system.dto.UserWithRole;
import com.zhangjiajie.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户详情服务实现
 * 用于Spring Security用户认证
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户及角色信息
        UserWithRole user = userMapper.selectUserWithRoleByUsername(username);
        if (user == null) {
            log.warn("用户 {} 不存在", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if (user.getDeleted() != null && user.getDeleted() == 1) {
            log.warn("用户 {} 已被删除", username);
            throw new UsernameNotFoundException("用户已被删除: " + username);
        }

        // 构建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<String> permissionSet = new HashSet<>();
        
        // 添加角色（Spring Security的hasRole需要ROLE_前缀）
        String roleCode = user.getRoleCode();
        if (roleCode != null && !roleCode.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode));
            log.debug("用户 {} 角色: {}", username, roleCode);
        }
        
        // 添加具体权限
        List<String> permissions = userMapper.selectUserPermissions(user.getId());
        if (permissions != null) {
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
                permissionSet.add(permission);
            }
            log.debug("用户 {} 权限: {}", username, permissions);
        }

        log.debug("加载用户信息: {}, 权限数量: {}", username, authorities.size());

        // 构建LoginUser对象，包含完整用户信息
        LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), user.getPassword(), authorities);
        loginUser.setNickname(user.getNickname());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setEmail(user.getEmail());
        loginUser.setPhone(user.getPhone());
        loginUser.setRoleCode(user.getRoleCode());
        loginUser.setRoleName(user.getRoleName());
        loginUser.setStatus(user.getStatus());
        loginUser.setPermissions(permissionSet);
        loginUser.setLoginTime(System.currentTimeMillis());

        return loginUser;
    }
}