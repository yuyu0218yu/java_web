package com.zhangjiajie.common.constant;

/**
 * 系统常量
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class Constants {

    private Constants() {
        // 常量类禁止实例化
    }

    // ==================== 通用状态 ====================

    /**
     * 启用状态
     */
    public static final int STATUS_ENABLED = 1;

    /**
     * 禁用状态
     */
    public static final int STATUS_DISABLED = 0;

    /**
     * 未删除
     */
    public static final int NOT_DELETED = 0;

    /**
     * 已删除
     */
    public static final int DELETED = 1;

    // ==================== 角色编码 ====================

    /**
     * 超级管理员角色
     */
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";

    /**
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * 普通用户角色
     */
    public static final String ROLE_USER = "USER";

    // ==================== 分页默认值 ====================

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    // ==================== 字符集 ====================

    /**
     * UTF-8字符集
     */
    public static final String UTF8 = "UTF-8";

    // ==================== 时间格式 ====================

    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    // ==================== 文件相关 ====================

    /**
     * 允许上传的图片类型
     */
    public static final String[] IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    /**
     * 允许上传的文档类型
     */
    public static final String[] DOC_EXTENSIONS = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt"};

    /**
     * 默认最大文件大小（10MB）
     */
    public static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024;

    // ==================== 缓存相关 ====================

    /**
     * 验证码过期时间（秒）
     */
    public static final int CAPTCHA_EXPIRE_SECONDS = 300;

    /**
     * Token过期时间（秒）- 24小时
     */
    public static final int TOKEN_EXPIRE_SECONDS = 86400;

    // ==================== 正则表达式 ====================

    /**
     * 手机号正则
     */
    public static final String REGEX_PHONE = "^1[3-9]\\d{9}$";

    /**
     * 邮箱正则
     */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * 用户名正则（4-20位，字母数字下划线）
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{4,20}$";

    /**
     * 密码正则（6-20位，至少包含字母和数字）
     */
    public static final String REGEX_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,20}$";
}
