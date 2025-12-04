package com.yushuang.demo;

import com.yushuang.demo.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * 密码生成器测试类 - 修复编码问题版本
 */
public class PasswordGeneratorTestFixed {

    @Test
    public void generateAdminPassword() {
        // 设置控制台输出编码为UTF-8
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            // 如果设置失败，继续执行
        }

        String plainPassword = "123456";

        System.out.println("========================================");
        System.out.println("开始生成管理员密码...");
        System.out.println("原始密码: " + plainPassword);
        System.out.println("========================================");

        // 生成三个用户的加密密码
        String encryptedPassword1 = PasswordUtil.encryptPassword(plainPassword);
        String encryptedPassword2 = PasswordUtil.encryptPassword(plainPassword);
        String encryptedPassword3 = PasswordUtil.encryptPassword(plainPassword);

        // 解析加密密码获取盐值
        String[] parts1 = encryptedPassword1.split(":");
        String[] parts2 = encryptedPassword2.split(":");
        String[] parts3 = encryptedPassword3.split(":");

        System.out.println("admin用户:");
        System.out.println("  加密密码: " + encryptedPassword1);
        System.out.println("  盐值: " + parts1[1]);
        System.out.println();

        System.out.println("manager用户:");
        System.out.println("  加密密码: " + encryptedPassword2);
        System.out.println("  盐值: " + parts2[1]);
        System.out.println();

        System.out.println("user1用户:");
        System.out.println("  加密密码: " + encryptedPassword3);
        System.out.println("  盐值: " + parts3[1]);
        System.out.println("========================================");

        // 验证密码
        System.out.println("密码验证:");
        System.out.println("  admin密码验证: " + PasswordUtil.verifyPassword("123456", encryptedPassword1));
        System.out.println("  manager密码验证: " + PasswordUtil.verifyPassword("123456", encryptedPassword2));
        System.out.println("  user1密码验证: " + PasswordUtil.verifyPassword("123456", encryptedPassword3));
        System.out.println("  错误密码验证: " + PasswordUtil.verifyPassword("wrongpassword", encryptedPassword1));
        System.out.println("========================================");

        // 生成SQL插入语句
        System.out.println("SQL插入语句:");
        System.out.println("INSERT INTO sys_user (username, password, salt, real_name, email, phone, gender, status) VALUES");
        System.out.println("('admin', '" + encryptedPassword1 + "', '" + parts1[1] + "', '超级管理员', 'admin@example.com', '13800138000', 1, 1),");
        System.out.println("('manager', '" + encryptedPassword2 + "', '" + parts2[1] + "', '管理员', 'manager@example.com', '13800138001', 1, 1),");
        System.out.println("('user1', '" + encryptedPassword3 + "', '" + parts3[1] + "', '普通用户1', 'user1@example.com', '13800138002', 1, 1);");
        System.out.println("========================================");

        // 密码强度检查
        System.out.println("密码强度分析:");
        System.out.println("  密码 '123456' 强度: " + PasswordUtil.getPasswordStrengthDescription("123456") +
                          " (等级: " + PasswordUtil.checkPasswordStrength("123456") + ")");
        System.out.println("  密码 'Admin123!' 强度: " + PasswordUtil.getPasswordStrengthDescription("Admin123!") +
                          " (等级: " + PasswordUtil.checkPasswordStrength("Admin123!") + ")");
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        PasswordGeneratorTestFixed test = new PasswordGeneratorTestFixed();
        test.generateAdminPassword();
    }
}