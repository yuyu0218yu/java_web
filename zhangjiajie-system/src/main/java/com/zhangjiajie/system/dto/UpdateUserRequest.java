package com.zhangjiajie.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 更新用户请求DTO
 */
@Data
public class UpdateUserRequest {

    @Size(min = 3, max = 20, message = "用户名长度需在3-20之间")
    private String username;

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

    private List<Long> roleIds;

    /**
     * 部门ID
     */
    private Long deptId;
}
