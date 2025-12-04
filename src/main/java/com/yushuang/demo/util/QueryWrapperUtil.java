package com.yushuang.demo.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.util.Collection;
import java.util.Map;

/**
 * MyBatis-Plus查询构造工具类
 */
public class QueryWrapperUtil {

    /**
     * 创建QueryWrapper
     */
    public static <T> QueryWrapper<T> create() {
        return new QueryWrapper<>();
    }

    /**
     * 创建LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> createLambda() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 创建UpdateWrapper
     */
    public static <T> UpdateWrapper<T> createUpdate() {
        return new UpdateWrapper<>();
    }

    /**
     * 创建LambdaUpdateWrapper
     */
    public static <T> LambdaUpdateWrapper<T> createLambdaUpdate() {
        return new LambdaUpdateWrapper<>();
    }

    /**
     * 等于查询
     */
    public static <T> QueryWrapper<T> eq(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.eq(column, value);
        }
        return wrapper;
    }

    /**
     * 不等于查询
     */
    public static <T> QueryWrapper<T> ne(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.ne(column, value);
        }
        return wrapper;
    }

    /**
     * 大于查询
     */
    public static <T> QueryWrapper<T> gt(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.gt(column, value);
        }
        return wrapper;
    }

    /**
     * 大于等于查询
     */
    public static <T> QueryWrapper<T> ge(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.ge(column, value);
        }
        return wrapper;
    }

    /**
     * 小于查询
     */
    public static <T> QueryWrapper<T> lt(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.lt(column, value);
        }
        return wrapper;
    }

    /**
     * 小于等于查询
     */
    public static <T> QueryWrapper<T> le(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.le(column, value);
        }
        return wrapper;
    }

    /**
     * 模糊查询
     */
    public static <T> QueryWrapper<T> like(QueryWrapper<T> wrapper, String column, String value) {
        if (StringUtil.hasText(value)) {
            wrapper.like(column, value);
        }
        return wrapper;
    }

    /**
     * 左模糊查询
     */
    public static <T> QueryWrapper<T> likeLeft(QueryWrapper<T> wrapper, String column, String value) {
        if (StringUtil.hasText(value)) {
            wrapper.likeLeft(column, value);
        }
        return wrapper;
    }

    /**
     * 右模糊查询
     */
    public static <T> QueryWrapper<T> likeRight(QueryWrapper<T> wrapper, String column, String value) {
        if (StringUtil.hasText(value)) {
            wrapper.likeRight(column, value);
        }
        return wrapper;
    }

    /**
     * IN查询
     */
    public static <T> QueryWrapper<T> in(QueryWrapper<T> wrapper, String column, Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            wrapper.in(column, value);
        }
        return wrapper;
    }

    /**
     * IN查询（数组）
     */
    public static <T> QueryWrapper<T> in(QueryWrapper<T> wrapper, String column, Object[] value) {
        if (value != null && value.length > 0) {
            wrapper.in(column, value);
        }
        return wrapper;
    }

    /**
     * NOT IN查询
     */
    public static <T> QueryWrapper<T> notIn(QueryWrapper<T> wrapper, String column, Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            wrapper.notIn(column, value);
        }
        return wrapper;
    }

    /**
     * NOT IN查询（数组）
     */
    public static <T> QueryWrapper<T> notIn(QueryWrapper<T> wrapper, String column, Object[] value) {
        if (value != null && value.length > 0) {
            wrapper.notIn(column, value);
        }
        return wrapper;
    }

    /**
     * IS NULL查询
     */
    public static <T> QueryWrapper<T> isNull(QueryWrapper<T> wrapper, String column) {
        wrapper.isNull(column);
        return wrapper;
    }

    /**
     * IS NOT NULL查询
     */
    public static <T> QueryWrapper<T> isNotNull(QueryWrapper<T> wrapper, String column) {
        wrapper.isNotNull(column);
        return wrapper;
    }

    /**
     * BETWEEN查询
     */
    public static <T> QueryWrapper<T> between(QueryWrapper<T> wrapper, String column, Object start, Object end) {
        if (start != null && end != null) {
            wrapper.between(column, start, end);
        }
        return wrapper;
    }

    /**
     * 排序
     */
    public static <T> QueryWrapper<T> orderBy(QueryWrapper<T> wrapper, String column, boolean isAsc) {
        if (StringUtil.hasText(column)) {
            wrapper.orderBy(true, isAsc, column);
        }
        return wrapper;
    }

    /**
     * 升序排序
     */
    public static <T> QueryWrapper<T> orderByAsc(QueryWrapper<T> wrapper, String... columns) {
        for (String column : columns) {
            wrapper.orderByAsc(column);
        }
        return wrapper;
    }

    /**
     * 降序排序
     */
    public static <T> QueryWrapper<T> orderByDesc(QueryWrapper<T> wrapper, String... columns) {
        for (String column : columns) {
            wrapper.orderByDesc(column);
        }
        return wrapper;
    }

    /**
     * 分组查询
     */
    public static <T> QueryWrapper<T> groupBy(QueryWrapper<T> wrapper, String... columns) {
        for (String column : columns) {
            wrapper.groupBy(column);
        }
        return wrapper;
    }

    /**
     * 根据Map条件构造查询
     */
    public static <T> QueryWrapper<T> allEq(QueryWrapper<T> wrapper, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            params.forEach((key, value) -> {
                if (value != null) {
                    wrapper.eq(key, value);
                }
            });
        }
        return wrapper;
    }

    /**
     * 根据Map条件构造查询（忽略空值）
     */
    public static <T> QueryWrapper<T> allEqIgnoreNull(QueryWrapper<T> wrapper, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            params.forEach((key, value) -> {
                if (value != null && !(value instanceof String && ((String) value).trim().isEmpty())) {
                    wrapper.eq(key, value);
                }
            });
        }
        return wrapper;
    }

    /**
     * 日期范围查询
     */
    public static <T> QueryWrapper<T> dateRange(QueryWrapper<T> wrapper, String column, String startDate, String endDate) {
        if (StringUtil.hasText(startDate)) {
            wrapper.ge(column, startDate);
        }
        if (StringUtil.hasText(endDate)) {
            wrapper.le(column, endDate);
        }
        return wrapper;
    }

    /**
     * 数字范围查询
     */
    public static <T> QueryWrapper<T> numberRange(QueryWrapper<T> wrapper, String column, Number minValue, Number maxValue) {
        if (minValue != null) {
            wrapper.ge(column, minValue);
        }
        if (maxValue != null) {
            wrapper.le(column, maxValue);
        }
        return wrapper;
    }

    /**
     * OR条件构造器
     */
    public static <T> QueryWrapper<T> or(QueryWrapper<T> wrapper) {
        wrapper.or();
        return wrapper;
    }

    /**
     * AND条件构造器
     */
    public static <T> QueryWrapper<T> and(QueryWrapper<T> wrapper) {
        // AND操作通常用于连接已有条件，这里直接返回wrapper
        return wrapper;
    }

    /**
     * AND条件构造器（带条件）
     */
    public static <T> QueryWrapper<T> and(QueryWrapper<T> wrapper, String column, Object value) {
        if (value != null) {
            wrapper.and(w -> w.eq(column, value));
        }
        return wrapper;
    }

    /**
     * AND条件构造器（带自定义条件）
     */
    public static <T> QueryWrapper<T> and(QueryWrapper<T> wrapper, java.util.function.Consumer<QueryWrapper<T>> consumer) {
        wrapper.and(consumer);
        return wrapper;
    }

    /**
     * 嵌套查询
     */
    public static <T> QueryWrapper<T> nested(QueryWrapper<T> wrapper, java.util.function.Consumer<QueryWrapper<T>> consumer) {
        wrapper.nested(consumer);
        return wrapper;
    }

    /**
     * 应用Lambda表达式查询
     */
    public static <T> LambdaQueryWrapper<T> applyLambda(LambdaQueryWrapper<T> wrapper,
            java.util.function.Consumer<LambdaQueryWrapper<T>> consumer) {
        consumer.accept(wrapper);
        return wrapper;
    }

    /**
     * 构建通用查询条件（根据传入的参数Map）
     */
    public static <T> QueryWrapper<T> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<T> wrapper = create();
        if (params != null && !params.isEmpty()) {
            params.forEach((key, value) -> {
                if (value != null) {
                    String column = StringUtil.camelToUnderscore(key);
                    wrapper.eq(column, value);
                }
            });
        }
        return wrapper;
    }

    /**
     * 构建模糊查询条件（根据传入的参数Map）
     */
    public static <T> QueryWrapper<T> buildLikeQueryWrapper(Map<String, Object> params, String... likeFields) {
        QueryWrapper<T> wrapper = create();
        if (params != null && !params.isEmpty()) {
            params.forEach((key, value) -> {
                if (value != null) {
                    String column = StringUtil.camelToUnderscore(key);

                    // 检查是否是模糊查询字段
                    boolean isLikeField = false;
                    for (String likeField : likeFields) {
                        if (key.equals(likeField)) {
                            isLikeField = true;
                            break;
                        }
                    }

                    if (isLikeField && value instanceof String) {
                        wrapper.like(column, value.toString());
                    } else {
                        wrapper.eq(column, value);
                    }
                }
            });
        }
        return wrapper;
    }

    /**
     * 设置更新字段
     */
    public static <T> UpdateWrapper<T> setUpdateFields(UpdateWrapper<T> wrapper, Map<String, Object> updateFields) {
        if (updateFields != null && !updateFields.isEmpty()) {
            updateFields.forEach((key, value) -> {
                if (value != null) {
                    String column = StringUtil.camelToUnderscore(key);
                    wrapper.set(column, value);
                }
            });
        }
        return wrapper;
    }

    /**
     * 创建时间范围查询条件
     */
    public static <T> QueryWrapper<T> createTimeRange(QueryWrapper<T> wrapper, String startTime, String endTime) {
        return dateRange(wrapper, "create_time", startTime, endTime);
    }

    /**
     * 更新时间范围查询条件
     */
    public static <T> QueryWrapper<T> updateTimeRange(QueryWrapper<T> wrapper, String startTime, String endTime) {
        return dateRange(wrapper, "update_time", startTime, endTime);
    }

    /**
     * 逻辑删除查询（未删除）
     */
    public static <T> QueryWrapper<T> notDeleted(QueryWrapper<T> wrapper) {
        return eq(wrapper, "deleted", 0);
    }

    /**
     * 逻辑删除查询（已删除）
     */
    public static <T> QueryWrapper<T> deleted(QueryWrapper<T> wrapper) {
        return eq(wrapper, "deleted", 1);
    }

    /**
     * 状态查询（启用状态）
     */
    public static <T> QueryWrapper<T> enabled(QueryWrapper<T> wrapper) {
        return eq(wrapper, "status", 1);
    }

    /**
     * 状态查询（禁用状态）
     */
    public static <T> QueryWrapper<T> disabled(QueryWrapper<T> wrapper) {
        return eq(wrapper, "status", 0);
    }
}