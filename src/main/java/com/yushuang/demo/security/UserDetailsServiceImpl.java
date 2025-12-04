package com.yushuang.demo.security;

import com.yushuang.demo.entity.User;
import com.yushuang.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.warn("用户 {} 不存在", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if (user.getDeleted() != null && user.getDeleted() == 1) {
            log.warn("用户 {} 已被删除", username);
            throw new UsernameNotFoundException("用户已被删除: " + username);
        }

        log.debug("加载用户信息: {}", username);

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}