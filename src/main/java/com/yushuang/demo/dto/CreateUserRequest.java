package com.yushuang.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建用户请求DTO
 */
@Data
public class CreateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度需在3-20之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度需在6-50之间")
    private String password;

    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Min(value = 0, message = "性别值不合法")
    @Max(value = 2, message = "性别值不合法")
    private Integer gender;

    private LocalDate birthday;

    @Size(max = 255, message = "头像链接长度不能超过255")
    private String avatar;

    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    @NotNull(message = "角色不能为空")
    private Long roleId;
}
