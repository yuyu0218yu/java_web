package com.zhangjiajie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 张家界后台管理系统启动类
 *
 * @author zhangjiajie
 */
@SpringBootApplication(scanBasePackages = {"com.zhangjiajie"})
@MapperScan({"com.zhangjiajie.system.mapper", "com.zhangjiajie.generator.mapper"})
public class ZhangjiajieAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangjiajieAdminApplication.class, args);
    }

}
