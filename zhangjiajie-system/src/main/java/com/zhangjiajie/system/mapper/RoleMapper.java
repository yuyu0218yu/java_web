package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.dto.RoleWithPermissionsDTO;
import com.zhangjiajie.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户ID查询角色列表
     * 复杂JOIN查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT r.* " +
            "FROM sys_role r " +
            "JOIN sys_user_role ur ON r.id = ur.role_id AND ur.deleted = 0 " +
            "WHERE r.deleted = 0 AND ur.user_id = #{userId}")
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询角色
     * 用于AuthService注册时查找普通用户角色
     */
    @Select("SELECT * FROM sys_role WHERE deleted = 0 AND role_code = #{roleCode}")
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询所有角色及其菜单权限数量
     * 返回角色列表，每个角色包含其拥有的菜单权限数量
     */
    @Select("SELECT r.*, COUNT(DISTINCT rm.menu_id) as permission_count " +
            "FROM sys_role r " +
            "LEFT JOIN sys_role_menu rm ON r.id = rm.role_id " +
            "WHERE r.deleted = 0 " +
            "GROUP BY r.id, r.role_name, r.role_code, r.description, r.data_scope, r.sort_order, r.status, r.create_time, r.update_time, r.deleted " +
            "ORDER BY r.sort_order ASC")
    List<RoleWithPermissionsDTO> selectRolesWithPermissionCount();
}