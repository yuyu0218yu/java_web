package com.yushuang.demo;

import com.yushuang.demo.util.PasswordUtil;

/**
 * 密码生成器测试类
 * 用于生成初始化用户数据的加密密码
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        // 生成密码 "123456" 的加密结果
        String plainPassword = "123456";

        // 使用 PasswordUtil 生成加密密码
        String encryptedPassword1 = PasswordUtil.encryptPassword(plainPassword);
        String encryptedPassword2 = PasswordUtil.encryptPassword(plainPassword);
        String encryptedPassword3 = PasswordUtil.encryptPassword(plainPassword);

        System.out.println("原始密码: " + plainPassword);
        System.out.println("========================================");

        // 为三个用户生成不同的加密密码
        System.out.println("admin用户加密密码: " + encryptedPassword1);
        System.out.println("manager用户加密密码: " + encryptedPassword2);
        System.out.println("user1用户加密密码: " + encryptedPassword3);
        System.out.println("========================================");

        // 解析加密密码获取盐值
        String[] parts1 = encryptedPassword1.split(":");
        String[] parts2 = encryptedPassword2.split(":");
        String[] parts3 = encryptedPassword3.split(":");

        System.out.println("admin用户盐值: " + parts1[1]);
        System.out.println("manager用户盐值: " + parts2[1]);
        System.out.println("user1用户盐值: " + parts3[1]);
        System.out.println("========================================");

        // 验证密码
        System.out.println("验证admin密码: " + PasswordUtil.verifyPassword("123456", encryptedPassword1));
        System.out.println("验证manager密码: " + PasswordUtil.verifyPassword("123456", encryptedPassword2));
        System.out.println("验证user1密码: " + PasswordUtil.verifyPassword("123456", encryptedPassword3));
        System.out.println("验证错误密码: " + PasswordUtil.verifyPassword("wrongpassword", encryptedPassword1));

        // 生成SQL插入语句
        System.out.println("========================================");
        System.out.println("SQL插入语句:");
        System.out.println("INSERT INTO sys_user (username, password, salt, real_name, email, phone, gender, status) VALUES");
        System.out.println("('admin', '" + encryptedPassword1 + "', '" + parts1[1] + "', '超级管理员', 'admin@example.com', '13800138000', 1, 1),");
        System.out.println("('manager', '" + encryptedPassword2 + "', '" + parts2[1] + "', '管理员', 'manager@example.com', '13800138001', 1, 1),");
        System.out.println("('user1', '" + encryptedPassword3 + "', '" + parts3[1] + "', '普通用户1', 'user1@example.com', '13800138002', 1, 1);");
    }
}