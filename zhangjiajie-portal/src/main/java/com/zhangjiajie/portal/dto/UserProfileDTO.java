package com.zhangjiajie.portal.dto;

import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserProfileDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 0未知 1男 2女
     */
    private Integer gender;

    /**
     * 个人简介
     */
    private String bio;
}
