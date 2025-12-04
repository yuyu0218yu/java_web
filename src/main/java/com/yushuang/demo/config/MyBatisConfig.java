package com.yushuang.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Configuration
@MapperScan("com.yushuang.demo.mapper")
public class MyBatisConfig {
    
}
