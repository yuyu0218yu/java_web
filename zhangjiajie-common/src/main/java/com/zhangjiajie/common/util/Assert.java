package com.zhangjiajie.common.util;

import com.zhangjiajie.common.exception.BusinessException;
import com.zhangjiajie.common.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.Map;

/**
 * 业务断言工具类
 * 用于简化业务校验代码，校验失败时抛出业务异常
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class Assert {

    private Assert() {
        // 工具类禁止实例化
    }

    /**
     * 断言对象不为null
     *
     * @param obj     待校验对象
     * @param message 错误消息
     * @throws BusinessException 如果对象为null
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言资源存在（不为null）
     *
     * @param obj     待校验对象
     * @param message 错误消息
     * @throws ResourceNotFoundException 如果对象为null
     */
    public static void found(Object obj, String message) {
        if (obj == null) {
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * 断言字符串不为空
     *
     * @param str     待校验字符串
     * @param message 错误消息
     * @throws BusinessException 如果字符串为null或空
     */
    public static void notEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param collection 待校验集合
     * @param message    错误消息
     * @throws BusinessException 如果集合为null或空
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言Map不为空
     *
     * @param map     待校验Map
     * @param message 错误消息
     * @throws BusinessException 如果Map为null或空
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言数组不为空
     *
     * @param array   待校验数组
     * @param message 错误消息
     * @throws BusinessException 如果数组为null或空
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言条件为true
     *
     * @param expression 待校验条件
     * @param message    错误消息
     * @throws BusinessException 如果条件为false
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言条件为false
     *
     * @param expression 待校验条件
     * @param message    错误消息
     * @throws BusinessException 如果条件为true
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言两个对象相等
     *
     * @param obj1    对象1
     * @param obj2    对象2
     * @param message 错误消息
     * @throws BusinessException 如果对象不相等
     */
    public static void equals(Object obj1, Object obj2, String message) {
        if (obj1 == null && obj2 == null) {
            return;
        }
        if (obj1 == null || !obj1.equals(obj2)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言两个对象不相等
     *
     * @param obj1    对象1
     * @param obj2    对象2
     * @param message 错误消息
     * @throws BusinessException 如果对象相等
     */
    public static void notEquals(Object obj1, Object obj2, String message) {
        if (obj1 == null && obj2 == null) {
            throw new BusinessException(message);
        }
        if (obj1 != null && obj1.equals(obj2)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言数值在指定范围内（包含边界）
     *
     * @param value   待校验数值
     * @param min     最小值
     * @param max     最大值
     * @param message 错误消息
     * @throws BusinessException 如果数值不在范围内
     */
    public static void inRange(long value, long min, long max, String message) {
        if (value < min || value > max) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言数值大于指定值
     *
     * @param value   待校验数值
     * @param min     最小值（不包含）
     * @param message 错误消息
     * @throws BusinessException 如果数值不大于指定值
     */
    public static void greaterThan(long value, long min, String message) {
        if (value <= min) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言数值小于指定值
     *
     * @param value   待校验数值
     * @param max     最大值（不包含）
     * @param message 错误消息
     * @throws BusinessException 如果数值不小于指定值
     */
    public static void lessThan(long value, long max, String message) {
        if (value >= max) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言字符串长度在指定范围内
     *
     * @param str       待校验字符串
     * @param minLength 最小长度（包含）
     * @param maxLength 最大长度（包含）
     * @param message   错误消息
     * @throws BusinessException 如果长度不在范围内
     */
    public static void lengthBetween(String str, int minLength, int maxLength, String message) {
        if (str == null) {
            throw new BusinessException(message);
        }
        int length = str.length();
        if (length < minLength || length > maxLength) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言字符串匹配正则表达式
     *
     * @param str     待校验字符串
     * @param regex   正则表达式
     * @param message 错误消息
     * @throws BusinessException 如果不匹配
     */
    public static void matches(String str, String regex, String message) {
        if (str == null || !str.matches(regex)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言是合法的手机号
     *
     * @param phone   待校验手机号
     * @param message 错误消息
     * @throws BusinessException 如果不是合法手机号
     */
    public static void isPhone(String phone, String message) {
        if (!StringUtil.isPhone(phone)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言是合法的邮箱
     *
     * @param email   待校验邮箱
     * @param message 错误消息
     * @throws BusinessException 如果不是合法邮箱
     */
    public static void isEmail(String email, String message) {
        if (!StringUtil.isEmail(email)) {
            throw new BusinessException(message);
        }
    }
}
