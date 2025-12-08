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
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 数据范围：1-全部数据 2-本部门及下级 3-本部门 4-仅本人
     */
    private Integer dataScope;

    /**
     * 结构名称
     */
    private String deptName;
}
