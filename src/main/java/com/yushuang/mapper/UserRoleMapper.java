package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yushuang.demo.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID查询用户角色关联
     */
    @Select("SELECT * FROM sys_user_role WHERE deleted = 0 AND user_id = #{userId}")
    List<UserRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户角色关联
     */
    @Select("SELECT * FROM sys_user_role WHERE deleted = 0 AND role_id = #{roleId}")
    List<UserRole> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID删除用户角色关联
     */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户角色关联
     */
    @Delete("DELETE FROM sys_user_role WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户是否拥有指定角色
     */
    @Select("SELECT COUNT(1) FROM sys_user_role WHERE deleted = 0 AND user_id = #{userId} AND role_id = #{roleId}")
    int checkUserHasRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}