package com.zhangjiajie.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private UserInfo user;
    private List<String> permissions;

    public LoginResponse(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }
}