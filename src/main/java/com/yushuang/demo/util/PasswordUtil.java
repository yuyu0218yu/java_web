package com.yushuang.demo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密工具类
 */
public class PasswordUtil {

    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA256 = "SHA-256";
    private static final String ALGORITHM_SHA512 = "SHA-512";

    // 默认盐值长度
    private static final int DEFAULT_SALT_LENGTH = 16;

    // 密码迭代次数
    private static final int DEFAULT_ITERATIONS = 10000;

    /**
     * 生成随机盐值
     */
    public static String generateSalt() {
        return generateSalt(DEFAULT_SALT_LENGTH);
    }

    /**
     * 生成指定长度的随机盐值
     */
    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * MD5加密
     */
    public static String md5(String text) {
        return digest(text, ALGORITHM_MD5);
    }

    /**
     * MD5加密（带盐值）
     */
    public static String md5WithSalt(String text, String salt) {
        return digest(text + salt, ALGORITHM_MD5);
    }

    /**
     * SHA-256加密
     */
    public static String sha256(String text) {
        return digest(text, ALGORITHM_SHA256);
    }

    /**
     * SHA-256加密（带盐值）
     */
    public static String sha256WithSalt(String text, String salt) {
        return digest(text + salt, ALGORITHM_SHA256);
    }

    /**
     * SHA-512加密
     */
    public static String sha512(String text) {
        return digest(text, ALGORITHM_SHA512);
    }

    /**
     * SHA-512加密（带盐值）
     */
    public static String sha512WithSalt(String text, String salt) {
        return digest(text + salt, ALGORITHM_SHA512);
    }

    /**
     * 使用指定算法进行加密
     */
    public static String digest(String text, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(text.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法不支持: " + algorithm, e);
        }
    }

    /**
     * 密码加密（推荐使用）
     * 使用SHA-256 + 盐值 + 多次迭代
     */
    public static String encryptPassword(String password) {
        return encryptPassword(password, generateSalt(), DEFAULT_ITERATIONS);
    }

    /**
     * 密码加密（指定盐值）
     */
    public static String encryptPassword(String password, String salt) {
        return encryptPassword(password, salt, DEFAULT_ITERATIONS);
    }

    /**
     * 密码加密（指定盐值和迭代次数）
     */
    public static String encryptPassword(String password, String salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM_SHA256);
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] passwordBytes = password.getBytes();

            digest.reset();
            digest.update(saltBytes);

            byte[] hash = digest.digest(passwordBytes);
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return Base64.getEncoder().encodeToString(hash) + ":" + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法不支持: " + ALGORITHM_SHA256, e);
        }
    }

    /**
     * 验证密码
     */
    public static boolean verifyPassword(String password, String encryptedPassword) {
        if (password == null || encryptedPassword == null) {
            return false;
        }

        try {
            String[] parts = encryptedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }

            String expectedHash = parts[0];
            String salt = parts[1];

            String encrypted = encryptPassword(password, salt);
            String actualHash = encrypted.split(":")[0];
            return expectedHash.equals(actualHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 4) {
            length = 4;
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // 确保至少包含一个大写字母、小写字母、数字和特殊字符
        sb.append(chars.charAt(random.nextInt(26))); // 大写字母
        sb.append(chars.charAt(random.nextInt(26) + 26)); // 小写字母
        sb.append(chars.charAt(random.nextInt(10) + 52)); // 数字
        sb.append(chars.charAt(random.nextInt(chars.length() - 62) + 62)); // 特殊字符

        // 填充剩余长度
        for (int i = 4; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        // 打乱字符顺序
        char[] array = sb.toString().toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return new String(array);
    }

    /**
     * 生成默认长度的随机密码（8位）
     */
    public static String generateRandomPassword() {
        return generateRandomPassword(8);
    }

    /**
     * 检查密码强度
     * @param password 密码
     * @return 强度等级：1-弱，2-中等，3-强，4-很强
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 1;
        }

        int score = 0;

        // 长度评分
        if (password.length() >= 8) {
            score += 1;
        }
        if (password.length() >= 12) {
            score += 1;
        }

        // 字符类型评分
        if (password.matches(".*[a-z].*")) { // 小写字母
            score += 1;
        }
        if (password.matches(".*[A-Z].*")) { // 大写字母
            score += 1;
        }
        if (password.matches(".*[0-9].*")) { // 数字
            score += 1;
        }
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) { // 特殊字符
            score += 1;
        }

        // 复杂度评分
        if (score >= 5) {
            return 4; // 很强
        } else if (score >= 4) {
            return 3; // 强
        } else if (score >= 3) {
            return 2; // 中等
        } else {
            return 1; // 弱
        }
    }

    /**
     * 获取密码强度描述
     */
    public static String getPasswordStrengthDescription(int level) {
        switch (level) {
            case 1:
                return "弱";
            case 2:
                return "中等";
            case 3:
                return "强";
            case 4:
                return "很强";
            default:
                return "未知";
        }
    }

    /**
     * 获取密码强度描述
     */
    public static String getPasswordStrengthDescription(String password) {
        int level = checkPasswordStrength(password);
        return getPasswordStrengthDescription(level);
    }

    /**
     * 验证密码格式
     * @param password 密码
     * @param minLength 最小长度
     * @param requireUppercase 是否要求大写字母
     * @param requireLowercase 是否要求小写字母
     * @param requireNumbers 是否要求数字
     * @param requireSpecialChars 是否要求特殊字符
     * @return 验证结果
     */
    public static PasswordValidationResult validatePassword(String password,
            int minLength, boolean requireUppercase, boolean requireLowercase,
            boolean requireNumbers, boolean requireSpecialChars) {

        PasswordValidationResult result = new PasswordValidationResult();

        if (password == null || password.isEmpty()) {
            result.valid = false;
            result.message = "密码不能为空";
            return result;
        }

        if (password.length() < minLength) {
            result.valid = false;
            result.message = "密码长度不能少于" + minLength + "位";
            return result;
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            result.valid = false;
            result.message = "密码必须包含大写字母";
            return result;
        }

        if (requireLowercase && !password.matches(".*[a-z].*")) {
            result.valid = false;
            result.message = "密码必须包含小写字母";
            return result;
        }

        if (requireNumbers && !password.matches(".*[0-9].*")) {
            result.valid = false;
            result.message = "密码必须包含数字";
            return result;
        }

        if (requireSpecialChars && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            result.valid = false;
            result.message = "密码必须包含特殊字符";
            return result;
        }

        result.valid = true;
        result.message = "密码格式正确";
        return result;
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 密码验证结果
     */
    public static class PasswordValidationResult {
        public boolean valid;
        public String message;
    }
}