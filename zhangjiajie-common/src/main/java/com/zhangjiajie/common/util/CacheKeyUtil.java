package com.zhangjiajie.common.util;

import java.util.StringJoiner;

/**
 * 缓存Key生成工具类
 * 统一管理缓存key的生成规则，避免key冲突
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class CacheKeyUtil {

    private CacheKeyUtil() {
        // 工具类禁止实例化
    }

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * 应用前缀
     */
    private static final String APP_PREFIX = "zjj";

    // ==================== 通用Key前缀 ====================

    /**
     * 用户相关
     */
    public static final String USER_PREFIX = APP_PREFIX + SEPARATOR + "user";

    /**
     * 认证相关
     */
    public static final String AUTH_PREFIX = APP_PREFIX + SEPARATOR + "auth";

    /**
     * 权限相关
     */
    public static final String PERM_PREFIX = APP_PREFIX + SEPARATOR + "perm";

    /**
     * 验证码相关
     */
    public static final String CAPTCHA_PREFIX = APP_PREFIX + SEPARATOR + "captcha";

    /**
     * 限流相关
     */
    public static final String RATE_LIMIT_PREFIX = APP_PREFIX + SEPARATOR + "rate";

    /**
     * 锁相关
     */
    public static final String LOCK_PREFIX = APP_PREFIX + SEPARATOR + "lock";

    // ==================== Key生成方法 ====================

    /**
     * 生成缓存Key
     *
     * @param parts key组成部分
     * @return 缓存key
     */
    public static String generate(String... parts) {
        StringJoiner joiner = new StringJoiner(SEPARATOR);
        joiner.add(APP_PREFIX);
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }

    /**
     * 生成用户相关缓存Key
     *
     * @param parts key组成部分
     * @return 缓存key
     */
    public static String user(String... parts) {
        return buildKey(USER_PREFIX, parts);
    }

    /**
     * 生成用户信息缓存Key
     *
     * @param userId 用户ID
     * @return 缓存key
     */
    public static String userInfo(Long userId) {
        return user("info", String.valueOf(userId));
    }

    /**
     * 生成用户权限缓存Key
     *
     * @param userId 用户ID
     * @return 缓存key
     */
    public static String userPermissions(Long userId) {
        return user("permissions", String.valueOf(userId));
    }

    /**
     * 生成用户菜单缓存Key
     *
     * @param userId 用户ID
     * @return 缓存key
     */
    public static String userMenus(Long userId) {
        return user("menus", String.valueOf(userId));
    }

    /**
     * 生成认证相关缓存Key
     *
     * @param parts key组成部分
     * @return 缓存key
     */
    public static String auth(String... parts) {
        return buildKey(AUTH_PREFIX, parts);
    }

    /**
     * 生成Token缓存Key
     *
     * @param token token值
     * @return 缓存key
     */
    public static String token(String token) {
        return auth("token", token);
    }

    /**
     * 生成刷新Token缓存Key
     *
     * @param refreshToken 刷新token值
     * @return 缓存key
     */
    public static String refreshToken(String refreshToken) {
        return auth("refresh", refreshToken);
    }

    /**
     * 生成验证码缓存Key
     *
     * @param uuid 验证码唯一标识
     * @return 缓存key
     */
    public static String captcha(String uuid) {
        return buildKey(CAPTCHA_PREFIX, uuid);
    }

    /**
     * 生成短信验证码缓存Key
     *
     * @param phone 手机号
     * @return 缓存key
     */
    public static String smsCode(String phone) {
        return buildKey(CAPTCHA_PREFIX, "sms", phone);
    }

    /**
     * 生成邮箱验证码缓存Key
     *
     * @param email 邮箱
     * @return 缓存key
     */
    public static String emailCode(String email) {
        return buildKey(CAPTCHA_PREFIX, "email", email);
    }

    /**
     * 生成限流缓存Key
     *
     * @param key 限流标识
     * @return 缓存key
     */
    public static String rateLimit(String key) {
        return buildKey(RATE_LIMIT_PREFIX, key);
    }

    /**
     * 生成限流缓存Key（带IP）
     *
     * @param key 限流标识
     * @param ip  IP地址
     * @return 缓存key
     */
    public static String rateLimitByIp(String key, String ip) {
        return buildKey(RATE_LIMIT_PREFIX, key, "ip", ip);
    }

    /**
     * 生成限流缓存Key（带用户ID）
     *
     * @param key    限流标识
     * @param userId 用户ID
     * @return 缓存key
     */
    public static String rateLimitByUser(String key, Long userId) {
        return buildKey(RATE_LIMIT_PREFIX, key, "user", String.valueOf(userId));
    }

    /**
     * 生成分布式锁Key
     *
     * @param parts key组成部分
     * @return 缓存key
     */
    public static String lock(String... parts) {
        return buildKey(LOCK_PREFIX, parts);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建缓存Key
     *
     * @param prefix 前缀
     * @param parts  组成部分
     * @return 缓存key
     */
    private static String buildKey(String prefix, String... parts) {
        StringJoiner joiner = new StringJoiner(SEPARATOR);
        joiner.add(prefix);
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                joiner.add(part);
            }
        }
        return joiner.toString();
    }
}
