package com.zhangjiajie.system.service;

import com.zhangjiajie.system.dto.LoginRequest;
import com.zhangjiajie.system.dto.LoginResponse;
import com.zhangjiajie.system.dto.RegisterRequest;
import com.zhangjiajie.system.dto.UserInfo;

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
     * 用户注册（默认普通用户）
     */
    void register(RegisterRequest registerRequest);

    /**
     * 获取当前用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Object getUserInfo(String username);

    /**
     * 获取当前用户信息（基于上下文）
     */
    UserInfo getCurrentUserProfile();

    /**
     * 更新当前用户信息（头像/昵称/手机号）
     */
    void updateCurrentUserProfile(String username, String nickname, String phone, String avatar);

    /**
     * 修改当前用户密码
     */
    void changePassword(String username, String oldPassword, String newPassword);

    /**
     * 刷新token
     *
     * @param token 当前token
     * @return 新token
     */
    String refreshToken(String token);
}