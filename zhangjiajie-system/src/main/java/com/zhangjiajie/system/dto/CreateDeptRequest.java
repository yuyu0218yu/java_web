package com.zhangjiajie.system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 创建部门请求DTO
 *
 * @author yushuang
 * @since 2025-12-08
 */
@Data
public class CreateDeptRequest {

    /**
     * 父部门ID (0=根节点)
     */
    @NotNull(message = "父部门ID不能为空")
    private Long parentId;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100")
    private String deptName;

    /**
     * 部门编码
     */
    @Size(max = 50, message = "部门编码长度不能超过50")
    private String deptCode;

    /**
     * 负责人
     */
    @Size(max = 50, message = "负责人姓名长度不能超过50")
    private String leader;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

    /**
     * 排序
     */
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
