package com.yushuang.demo.service;

import com.yushuang.demo.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口
 */
public interface PermissionService {

    /**
     * 获取所有权限列表
     */
    List<Permission> getAllPermissions();

    /**
     * 获取权限树形结构
     */
    List<Map<String, Object>> getPermissionTree();

    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long id);

    /**
     * 创建权限
     */
    void createPermission(Permission permission);

    /**
     * 更新权限
     */
    void updatePermission(Long id, Permission permission);

    /**
     * 删除权限
     */
    void deletePermission(Long id);

    /**
     * 根据角色ID获取权限列表
     */
    List<Permission> getPermissionsByRoleId(Long roleId);
}
