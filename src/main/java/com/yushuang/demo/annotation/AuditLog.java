package com.yushuang.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

    /**
     * 操作类型
     */
    String operation() default "";

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean saveRequestData() default true;

    /**
     * 是否记录响应数据
     */
    boolean saveResponseData() default false;

    /**
     * 敏感参数字段，不记录这些字段的值
     */
    String[] sensitiveFields() default {"password", "pwd", "token", "secret", "key"};

    /**
     * 排除的参数类型，这些类型的参数不记录
     */
    Class<?>[] excludeTypes() default {
            org.springframework.web.multipart.MultipartFile.class,
            jakarta.servlet.http.HttpServletRequest.class,
            jakarta.servlet.http.HttpServletResponse.class,
            org.springframework.web.servlet.ModelAndView.class,
            org.springframework.ui.Model.class
    };
}