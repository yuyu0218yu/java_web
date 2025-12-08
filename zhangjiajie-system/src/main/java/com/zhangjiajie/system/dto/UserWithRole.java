package com.zhangjiajie.system.dto;

import com.zhangjiajie.common.enums.DataScope;
import com.zhangjiajie.system.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色信息DTO
 * 包含用户基本信息、角色信息、部门信息
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
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门完整路径（如：张家界旅游公司/技术部/后端组）
     */
    private String deptPath;

    /**
     * 数据范围说明
     */
    private String dataScopeDesc;

    /**
     * 获取数据范围描述
     */
    public String getDataScopeDesc() {
        if (dataScopeDesc != null) {
            return dataScopeDesc;
        }
        if (dataScope == null) {
            return "未设置";
        }
        
        DataScope scope = DataScope.getByCode(dataScope);
        return scope != null ? scope.getDesc() : "未知";
    }
}
