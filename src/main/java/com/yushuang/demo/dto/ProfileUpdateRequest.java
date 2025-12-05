package com.yushuang.demo.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 个人信息更新请求
 */
@Data
public class ProfileUpdateRequest {

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Size(max = 20, message = "手机号长度不合法")
    private String phone;

    @Size(max = 255, message = "头像链接长度不能超过255")
    private String avatar;
}
