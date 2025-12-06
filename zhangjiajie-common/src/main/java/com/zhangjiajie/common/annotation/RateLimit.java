package com.zhangjiajie.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流注解
 * 用于标记需要限流的接口
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流key前缀（默认使用方法全限定名）
     */
    String key() default "";

    /**
     * 时间窗口内允许的最大请求次数
     */
    int count() default 100;

    /**
     * 时间窗口大小
     */
    int time() default 60;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流提示消息
     */
    String message() default "请求过于频繁，请稍后重试";

    /**
     * 限流类型枚举
     */
    enum LimitType {
        /**
         * 默认：针对接口全局限流
         */
        DEFAULT,

        /**
         * 按IP限流
         */
        IP,

        /**
         * 按用户限流
         */
        USER
    }
}
