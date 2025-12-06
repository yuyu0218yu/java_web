package com.yushuang.demo.util;

import com.yushuang.demo.exception.BusinessException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 通用校验工具类
 * 提供常用的数据校验方法
 *
 * @author yushuang
 */
public final class ValidationUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    // 常用正则表达式 - 使用枚举或常量类封装
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,20}$"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );

    private static final Pattern IP_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );

    private static final Pattern CHINESE_PATTERN = Pattern.compile(
        "^[\\u4e00-\\u9fa5]+$"
    );

    private ValidationUtil() {
        // 工具类不应被实例化
    }

    /**
     * 使用JSR-303校验对象
     */
    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                message.append(violation.getPropertyPath())
                       .append(": ")
                       .append(violation.getMessage())
                       .append("; ");
            }
            throw new BusinessException(400, message.toString());
        }
    }

    // ======================== 格式校验方法 ========================

    /**
     * 校验邮箱格式
     */
    public static boolean isEmail(String email) {
        return matches(email, EMAIL_PATTERN);
    }

    /**
     * 校验邮箱格式（校验失败抛出异常）
     */
    public static void validateEmail(String email, String message) {
        validatePattern(email, EMAIL_PATTERN, message, "邮箱格式不正确");
    }

    /**
     * 校验手机号格式
     */
    public static boolean isPhone(String phone) {
        return matches(phone, PHONE_PATTERN);
    }

    /**
     * 校验手机号格式（校验失败抛出异常）
     */
    public static void validatePhone(String phone, String message) {
        validatePattern(phone, PHONE_PATTERN, message, "手机号格式不正确");
    }

    /**
     * 校验身份证号格式
     */
    public static boolean isIdCard(String idCard) {
        return matches(idCard, ID_CARD_PATTERN);
    }

    /**
     * 校验身份证号格式（校验失败抛出异常）
     */
    public static void validateIdCard(String idCard, String message) {
        validatePattern(idCard, ID_CARD_PATTERN, message, "身份证号格式不正确");
    }

    /**
     * 校验用户名格式（3-20位字母数字下划线）
     */
    public static boolean isUsername(String username) {
        return matches(username, USERNAME_PATTERN);
    }

    /**
     * 校验用户名格式（校验失败抛出异常）
     */
    public static void validateUsername(String username, String message) {
        validatePattern(username, USERNAME_PATTERN, message, "用户名格式不正确（3-20位字母数字下划线）");
    }

    /**
     * 校验密码强度（8-20位，必须包含大小写字母和数字）
     */
    public static boolean isStrongPassword(String password) {
        return matches(password, PASSWORD_PATTERN);
    }

    /**
     * 校验密码强度（校验失败抛出异常）
     */
    public static void validatePassword(String password, String message) {
        validatePattern(password, PASSWORD_PATTERN, message, "密码强度不足（8-20位，必须包含大小写字母和数字）");
    }

    /**
     * 校验URL格式
     */
    public static boolean isUrl(String url) {
        return matches(url, URL_PATTERN);
    }

    /**
     * 校验URL格式（校验失败抛出异常）
     */
    public static void validateUrl(String url, String message) {
        validatePattern(url, URL_PATTERN, message, "URL格式不正确");
    }

    /**
     * 校验IP地址格式
     */
    public static boolean isIpAddress(String ip) {
        return matches(ip, IP_PATTERN);
    }

    /**
     * 校验IP地址格式（校验失败抛出异常）
     */
    public static void validateIpAddress(String ip, String message) {
        validatePattern(ip, IP_PATTERN, message, "IP地址格式不正确");
    }

    /**
     * 校验是否为纯中文
     */
    public static boolean isChinese(String text) {
        return matches(text, CHINESE_PATTERN);
    }

    // ======================== 通用校验方法 ========================

    /**
     * 校验是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 校验是否非空（校验失败抛出异常）
     */
    public static void validateNotEmpty(String str, String message) {
        if (isEmpty(str)) {
            throwValidationError(message, "参数不能为空");
        }
    }

    /**
     * 校验字符串长度
     */
    public static boolean isLengthBetween(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * 校验字符串长度（校验失败抛出异常）
     */
    public static void validateLength(String str, int min, int max, String message) {
        if (!isLengthBetween(str, min, max)) {
            throwValidationError(message, String.format("长度必须在%d到%d之间", min, max));
        }
    }

    /**
     * 校验数值范围
     */
    public static boolean isNumberBetween(Number value, Number min, Number max) {
        if (value == null) {
            return false;
        }
        double val = value.doubleValue();
        return val >= min.doubleValue() && val <= max.doubleValue();
    }

    /**
     * 校验数值范围（校验失败抛出异常）
     */
    public static void validateNumber(Number value, Number min, Number max, String message) {
        if (!isNumberBetween(value, min, max)) {
            throwValidationError(message, String.format("数值必须在%s到%s之间", min, max));
        }
    }

    /**
     * 校验对象不为null
     */
    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throwValidationError(message, "对象不能为空");
        }
    }

    /**
     * 校验条件为true
     */
    public static void validateTrue(boolean condition, String message) {
        if (!condition) {
            throwValidationError(message, "校验失败");
        }
    }

    /**
     * 校验条件为false
     */
    public static void validateFalse(boolean condition, String message) {
        if (condition) {
            throwValidationError(message, "校验失败");
        }
    }

    // ======================== 私有辅助方法 ========================

    /**
     * 通用正则匹配
     */
    private static boolean matches(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    /**
     * 通用正则校验（校验失败抛出异常）
     */
    private static void validatePattern(String value, Pattern pattern, String customMessage, String defaultMessage) {
        if (!matches(value, pattern)) {
            throwValidationError(customMessage, defaultMessage);
        }
    }

    /**
     * 抛出校验异常
     */
    private static void throwValidationError(String customMessage, String defaultMessage) {
        throw new BusinessException(400, customMessage != null ? customMessage : defaultMessage);
    }
}
