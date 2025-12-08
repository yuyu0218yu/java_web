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
        Integer scope = getDataScope();
        if (scope == null) {
            return "未设置";
        }
        switch (scope) {
            case 1:
                return "全部数据 - 可以查看和管理所有部门的数据";
            case 2:
                return "本部门及下级 - 只能查看和管理用户所在部门及其下级部门的数据";
            case 3:
                return "本部门 - 只能查看和管理用户所在部门的数据";
            case 4:
                return "仅本人 - 只能查看和管理自己的数据";
            default:
                return "未知范围";
        }
    }
}
