package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.system.dto.UserWithRole;
import com.zhangjiajie.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户列表（包含角色信息和部门完整信息）
     * 复杂JOIN查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT u.*, r.id as role_id, r.role_name, r.role_code, r.data_scope, " +
            "d.dept_name, d.dept_code, " +
            "CASE " +
            "  WHEN d.ancestors IS NULL OR d.ancestors = '' THEN d.dept_name " +
            "  ELSE CONCAT(" +
            "    (SELECT GROUP_CONCAT(dept_name ORDER BY id SEPARATOR '/') " +
            "     FROM sys_dept " +
            "     WHERE FIND_IN_SET(id, d.ancestors) AND deleted = 0), " +
            "    '/', d.dept_name) " +
            "END as dept_path " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 " +
            "WHERE u.deleted = 0 " +
            "ORDER BY u.create_time DESC")
    IPage<UserWithRole> selectUserPageWithRole(Page<UserWithRole> page);

    /**
     * 根据用户名查询用户（包含角色信息和部门完整信息）
     * 复杂JOIN查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, " +
            "u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, " +
            "u.last_login_time, u.last_login_ip, u.create_time, u.update_time, " +
            "u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, " +
            "d.dept_name, d.dept_code, " +
            "CASE " +
            "  WHEN d.ancestors IS NULL OR d.ancestors = '' THEN d.dept_name " +
            "  ELSE CONCAT(" +
            "    (SELECT GROUP_CONCAT(dept_name ORDER BY id SEPARATOR '/') " +
            "     FROM sys_dept " +
            "     WHERE FIND_IN_SET(id, d.ancestors) AND deleted = 0), " +
            "    '/', d.dept_name) " +
            "END as dept_path " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 " +
            "WHERE u.deleted = 0 AND u.username = #{username}")
    UserWithRole selectUserWithRoleByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户权限列表
     * 复杂JOIN查询，无法用LambdaQueryWrapper替代
     */
    @Select("SELECT DISTINCT p.permission_code " +
            "FROM sys_user u " +
            "JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 " +
            "JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 " +
            "JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 " +
            "WHERE u.deleted = 0 AND u.id = #{userId}")
    List<String> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 根据用户名查询用户
     * 用于AuthService登录验证
     */
    @Select("SELECT * FROM sys_user WHERE deleted = 0 AND username = #{username}")
    User selectByUsername(@Param("username") String username);

    /**
     * 更新最后登录信息
     * UPDATE语句，无法用LambdaQueryWrapper替代
     */
    @org.apache.ibatis.annotations.Update("UPDATE sys_user SET last_login_time = NOW(), last_login_ip = #{loginIp} WHERE id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);

    /**
     * 带数据权限过滤的用户分页查询
     * 使用XML配置，支持动态数据权限SQL
     *
     * @param page 分页对象
     * @param username 用户名（模糊查询）
     * @param email 邮箱（模糊查询）
     * @param status 状态
     * @param deptId 部门ID
     * @param dataScopeSql 数据权限SQL
     * @return 用户分页数据
     */
    IPage<UserWithRole> selectUserPageWithDataScope(
            Page<UserWithRole> page,
            @Param("username") String username,
            @Param("email") String email,
            @Param("status") Integer status,
            @Param("deptId") Long deptId,
            @Param("dataScopeSql") String dataScopeSql
    );
}