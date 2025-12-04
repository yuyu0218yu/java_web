package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushuang.demo.entity.Role;
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
     */
    @Select("SELECT r.* " +
            "FROM sys_role r " +
            "JOIN sys_user_role ur ON r.id = ur.role_id AND ur.deleted = 0 " +
            "WHERE r.deleted = 0 AND ur.user_id = #{userId}")
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编码查询角色
     */
    @Select("SELECT * FROM sys_role WHERE deleted = 0 AND role_code = #{roleCode}")
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询所有启用状态的角色
     */
    @Select("SELECT * FROM sys_role WHERE deleted = 0 AND status = 1 ORDER BY sort_order ASC")
    List<Role> selectEnabledRoles();

    /**
     * 检查角色编码是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_role WHERE deleted = 0 AND role_code = #{roleCode} AND id != #{excludeId}")
    int checkRoleCodeExists(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);

    /**
     * 根据角色ID查询权限数量
     */
    @Select("SELECT COUNT(1) FROM sys_role_permission WHERE deleted = 0 AND role_id = #{roleId}")
    int countPermissionsByRoleId(@Param("roleId") Long roleId);
}