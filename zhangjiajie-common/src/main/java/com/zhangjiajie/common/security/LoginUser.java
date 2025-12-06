package com.zhangjiajie.common.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户信息
 * 扩展Spring Security的UserDetails，包含额外的用户信息
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户状态（1正常 0禁用）
     */
    private Integer status;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * Spring Security 权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 登录时间戳
     */
    private Long loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    public LoginUser() {
    }

    public LoginUser(Long userId, String username, String password, 
                     Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     */
    @Override
    public boolean isEnabled() {
        return status != null && status == 1;
    }

    /**
     * 判断是否为超级管理员
     */
    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(roleCode);
    }

    /**
     * 判断是否为管理员（包括超级管理员和普通管理员）
     */
    public boolean isAdmin() {
        return "SUPER_ADMIN".equals(roleCode) || "ADMIN".equals(roleCode);
    }

    /**
     * 判断是否有指定权限
     */
    public boolean hasPermission(String permission) {
        if (isSuperAdmin()) {
            return true; // 超级管理员拥有所有权限
        }
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 判断是否有指定角色
     */
    public boolean hasRole(String role) {
        return role != null && role.equals(roleCode);
    }
}
