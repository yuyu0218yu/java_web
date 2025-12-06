package com.zhangjiajie.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 用于标记需要数据权限控制的方法
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表的别名
     */
    String deptAlias() default "d";

    /**
     * 用户表的别名
     */
    String userAlias() default "u";

    /**
     * 部门ID字段名
     */
    String deptIdColumn() default "dept_id";

    /**
     * 用户ID字段名
     */
    String userIdColumn() default "user_id";

    /**
     * 数据权限类型
     */
    ScopeType scopeType() default ScopeType.DEPT_AND_CHILD;

    /**
     * 数据权限类型枚举
     */
    enum ScopeType {
        /**
         * 全部数据权限
         */
        ALL,

        /**
         * 自定义数据权限
         */
        CUSTOM,

        /**
         * 本部门数据权限
         */
        DEPT,

        /**
         * 本部门及以下数据权限
         */
        DEPT_AND_CHILD,

        /**
         * 仅本人数据权限
         */
        SELF
    }
}
