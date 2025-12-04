package com.yushuang.demo.util;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 字符串工具类
 */
public class StringUtil extends StringUtils {

    /**
     * 生成UUID字符串（去除横线）
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成指定长度的随机字符串
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机数字字符串
     */
    public static String generateRandomNumber(int length) {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 字符串左填充
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int padLength = size - str.length();
        if (padLength <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padLength; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 字符串右填充
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int padLength = size - str.length();
        if (padLength <= 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < padLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 驼峰命名转下划线命名
     */
    public static String camelToUnderscore(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线命名转驼峰命名
     */
    public static String underscoreToCamel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    sb.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 隐藏字符串中间部分（如手机号、身份证号等）
     */
    public static String hideMiddle(String str, int start, int end, String replaceChar) {
        if (str == null || str.isEmpty() || start < 0 || end > str.length() || start >= end) {
            return str;
        }
        String prefix = str.substring(0, start);
        String suffix = str.substring(end);
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = start; i < end; i++) {
            sb.append(replaceChar);
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 隐藏手机号中间4位
     */
    public static String hidePhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return hideMiddle(phone, 3, 7, "*");
    }

    /**
     * 隐藏邮箱用户名部分
     */
    public static String hideEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return email;
        }

        String hiddenUsername = username.substring(0, 2) + "***" + username.substring(username.length() - 1);
        return hiddenUsername + "@" + domain;
    }

    /**
     * 去除字符串中所有空格
     */
    public static String removeAllSpaces(String str) {
        return str == null ? null : str.replaceAll("\\s+", "");
    }

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+");
    }

    /**
     * 判断字符串是否为手机号
     */
    public static boolean isPhone(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 判断字符串是否为邮箱
     */
    public static boolean isEmail(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * 字符串反转
     */
    public static String reverse(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 截取字符串（超出长度用...代替）
     */
    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    /**
     * 截取字符串（超出长度用指定字符串代替）
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - suffix.length()) + suffix;
    }
}