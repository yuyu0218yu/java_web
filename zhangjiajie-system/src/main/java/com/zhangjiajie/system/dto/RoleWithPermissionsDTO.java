package com.zhangjiajie.system.dto;

import com.zhangjiajie.common.enums.DataScope;
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

    /**
     * 数据范围说明（根据dataScope字段生成）
     */
    private String dataScopeDesc;

    /**
     * 获取数据范围描述
     */
    public String getDataScopeDesc() {
        if (dataScopeDesc != null) {
            return dataScopeDesc;
        }
        return DataScope.getDetailedDesc(getDataScope());
    }
}
