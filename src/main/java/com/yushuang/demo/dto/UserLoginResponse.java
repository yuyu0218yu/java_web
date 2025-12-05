package com.yushuang.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录响应DTO (用于UserController)
 */
@Data
public class UserLoginResponse {
    
    private Long userId;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private List<String> permissions;
}
