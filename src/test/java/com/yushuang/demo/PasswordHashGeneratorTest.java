package com.yushuang.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 简单的BCrypt密码生成测试，用于生成数据库可用的哈希
 */
class PasswordHashGeneratorTest {

    @Test
    void generateBcryptPassword() {
        // 修改为你想要设置的新密码
        String rawPassword = "123456";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String bcrypt = encoder.encode(rawPassword);

        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt哈希: " + bcrypt);

        // 验证生成的哈希可匹配
        assert encoder.matches(rawPassword, bcrypt);
    }
}
