package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushuang.demo.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户ID查询权限列表
     */
    @Select("SELECT DISTINCT p.* " +
            "FROM sys_permission p " +
            "JOIN sys_role_permission rp ON p.id = rp.permission_id AND rp.deleted = 0 " +
            "JOIN sys_user_role ur ON rp.role_id = ur.role_id AND ur.deleted = 0 " +
            "WHERE p.deleted = 0 AND ur.user_id = #{userId} " +
            "ORDER BY p.sort_order ASC")
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限列表
     */
    @Select("SELECT p.* " +
            "FROM sys_permission p " +
            "JOIN sys_role_permission rp ON p.id = rp.permission_id AND rp.deleted = 0 " +
            "WHERE p.deleted = 0 AND rp.role_id = #{roleId} " +
            "ORDER BY p.sort_order ASC")
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限编码查询权限
     */
    @Select("SELECT * FROM sys_permission WHERE deleted = 0 AND permission_code = #{permissionCode}")
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 查询所有菜单权限（树形结构）
     */
    @Select("SELECT * FROM sys_permission " +
            "WHERE deleted = 0 AND permission_type = 1 " +
            "ORDER BY parent_id ASC, sort_order ASC")
    List<Permission> selectMenuPermissions();

    /**
     * 查询指定父级下的子权限
     */
    @Select("SELECT * FROM sys_permission " +
            "WHERE deleted = 0 AND parent_id = #{parentId} " +
            "ORDER BY sort_order ASC")
    List<Permission> selectPermissionsByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有启用状态的权限
     */
    @Select("SELECT * FROM sys_permission WHERE deleted = 0 AND status = 1 ORDER BY sort_order ASC")
    List<Permission> selectEnabledPermissions();

    /**
     * 检查权限编码是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_permission WHERE deleted = 0 AND permission_code = #{permissionCode} AND id != #{excludeId}")
    int checkPermissionCodeExists(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);

    /**
     * 根据权限类型查询权限
     */
    @Select("SELECT * FROM sys_permission WHERE deleted = 0 AND permission_type = #{permissionType} ORDER BY sort_order ASC")
    List<Permission> selectPermissionsByType(@Param("permissionType") Integer permissionType);
}