package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushuang.demo.entity.RolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色权限关联Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据角色ID查询角色权限关联
     */
    @Select("SELECT * FROM sys_role_permission WHERE deleted = 0 AND role_id = #{roleId}")
    List<RolePermission> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查询角色权限关联
     */
    @Select("SELECT * FROM sys_role_permission WHERE deleted = 0 AND permission_id = #{permissionId}")
    List<RolePermission> selectByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 根据角色ID删除角色权限关联
     */
    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID删除角色权限关联
     */
    @Delete("DELETE FROM sys_role_permission WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 检查角色是否拥有指定权限
     */
    @Select("SELECT COUNT(1) FROM sys_role_permission WHERE deleted = 0 AND role_id = #{roleId} AND permission_id = #{permissionId}")
    int checkRoleHasPermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据角色ID和权限类型查询权限数量
     */
    @Select("SELECT COUNT(rp.*) FROM sys_role_permission rp " +
            "JOIN sys_permission p ON rp.permission_id = p.id " +
            "WHERE rp.deleted = 0 AND p.deleted = 0 AND rp.role_id = #{roleId} AND p.permission_type = #{permissionType}")
    int countPermissionsByRoleIdAndType(@Param("roleId") Long roleId, @Param("permissionType") Integer permissionType);
}