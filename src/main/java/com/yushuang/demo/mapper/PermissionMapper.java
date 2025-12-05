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
     * 根据角色ID查询权限列表
     * 复杂JOIN查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT p.* " +
            "FROM sys_permission p " +
            "JOIN sys_role_permission rp ON p.id = rp.permission_id AND rp.deleted = 0 " +
            "WHERE p.deleted = 0 AND rp.role_id = #{roleId} " +
            "ORDER BY p.sort_order ASC")
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}