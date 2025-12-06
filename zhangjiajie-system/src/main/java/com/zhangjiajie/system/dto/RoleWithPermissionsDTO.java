package com.zhangjiajie.system.dto;

import com.zhangjiajie.system.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色及权限数量DTO
 * 继承Role实体，额外包含权限数量信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleWithPermissionsDTO extends Role {

    /**
     * 该角色拥有的权限数量
     */
    private Integer permissionCount;
}
