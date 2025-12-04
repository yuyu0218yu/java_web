package com.yushuang.demo.service;

import com.yushuang.demo.dto.LoginRequest;
import com.yushuang.demo.dto.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 获取当前用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Object getUserInfo(String username);

    /**
     * 刷新token
     *
     * @param token 当前token
     * @return 新token
     */
    String refreshToken(String token);
}