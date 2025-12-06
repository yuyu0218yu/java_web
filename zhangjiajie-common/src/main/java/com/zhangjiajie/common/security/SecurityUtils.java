package com.zhangjiajie.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * 安全工具类
 * 提供获取当前登录用户信息的便捷方法
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // 工具类禁止实例化
    }

    /**
     * 获取当前认证信息
     *
     * @return Authentication对象，可能为null
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户
     *
     * @return LoginUser对象，如果未登录或不是LoginUser类型则返回null
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser) {
            return (LoginUser) principal;
        }
        return null;
    }

    /**
     * 获取当前登录用户（Optional包装）
     *
     * @return Optional<LoginUser>
     */
    public static Optional<LoginUser> getLoginUserOptional() {
        return Optional.ofNullable(getLoginUser());
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，未登录返回null
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * 获取当前用户ID（必须存在）
     *
     * @return 用户ID
     * @throws IllegalStateException 如果未登录
     */
    public static Long requireUserId() {
        Long userId = getUserId();
        if (userId == null) {
            throw new IllegalStateException("用户未登录");
        }
        return userId;
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名，未登录返回null
     */
    public static String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser) {
            return ((LoginUser) principal).getUsername();
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return authentication.getName();
    }

    /**
     * 获取当前用户名（必须存在）
     *
     * @return 用户名
     * @throws IllegalStateException 如果未登录
     */
    public static String requireUsername() {
        String username = getUsername();
        if (username == null) {
            throw new IllegalStateException("用户未登录");
        }
        return username;
    }

    /**
     * 获取当前用户昵称
     *
     * @return 昵称，未登录或无昵称返回用户名
     */
    public static String getNickname() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.getNickname() != null) {
            return loginUser.getNickname();
        }
        return getUsername();
    }

    /**
     * 获取当前用户头像
     *
     * @return 头像URL，未登录返回null
     */
    public static String getAvatar() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getAvatar() : null;
    }

    /**
     * 获取当前用户角色编码
     *
     * @return 角色编码，未登录返回null
     */
    public static String getRoleCode() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getRoleCode() : null;
    }

    /**
     * 判断当前用户是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isLoggedIn() {
        Authentication authentication = getAuthentication();
        return authentication != null 
               && authentication.isAuthenticated() 
               && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 判断当前用户是否为超级管理员
     *
     * @return true-是超级管理员，false-不是
     */
    public static boolean isSuperAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.isSuperAdmin();
    }

    /**
     * 判断当前用户是否为管理员（包括超级管理员）
     *
     * @return true-是管理员，false-不是
     */
    public static boolean isAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.isAdmin();
    }

    /**
     * 判断当前用户是否有指定权限
     *
     * @param permission 权限标识
     * @return true-有权限，false-无权限
     */
    public static boolean hasPermission(String permission) {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.hasPermission(permission);
    }

    /**
     * 判断当前用户是否有任一指定权限
     *
     * @param permissions 权限标识数组
     * @return true-有任一权限，false-无任何权限
     */
    public static boolean hasAnyPermission(String... permissions) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        if (loginUser.isSuperAdmin()) {
            return true;
        }
        for (String permission : permissions) {
            if (loginUser.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否有所有指定权限
     *
     * @param permissions 权限标识数组
     * @return true-有所有权限，false-缺少某些权限
     */
    public static boolean hasAllPermissions(String... permissions) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        if (loginUser.isSuperAdmin()) {
            return true;
        }
        for (String permission : permissions) {
            if (!loginUser.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前用户是否有指定角色
     *
     * @param role 角色编码
     * @return true-有该角色，false-无该角色
     */
    public static boolean hasRole(String role) {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.hasRole(role);
    }

    /**
     * 判断当前用户是否有任一指定角色
     *
     * @param roles 角色编码数组
     * @return true-有任一角色，false-无任何角色
     */
    public static boolean hasAnyRole(String... roles) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        for (String role : roles) {
            if (loginUser.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 要求当前用户必须有指定权限
     *
     * @param permission 权限标识
     * @throws SecurityException 如果没有权限
     */
    public static void requirePermission(String permission) {
        if (!hasPermission(permission)) {
            throw new SecurityException("没有权限: " + permission);
        }
    }

    /**
     * 要求当前用户必须有指定角色
     *
     * @param role 角色编码
     * @throws SecurityException 如果没有角色
     */
    public static void requireRole(String role) {
        if (!hasRole(role)) {
            throw new SecurityException("没有角色: " + role);
        }
    }

    /**
     * 要求当前用户必须是管理员
     *
     * @throws SecurityException 如果不是管理员
     */
    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new SecurityException("需要管理员权限");
        }
    }

    /**
     * 要求当前用户必须是超级管理员
     *
     * @throws SecurityException 如果不是超级管理员
     */
    public static void requireSuperAdmin() {
        if (!isSuperAdmin()) {
            throw new SecurityException("需要超级管理员权限");
        }
    }
}
