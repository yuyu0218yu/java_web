package com.zhangjiajie.system.dto;

import com.zhangjiajie.system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色信息DTO
 * 包含用户基本信息、角色信息、结构信息
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithRole extends User {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 结构名称
     */
    private String deptName;
}
