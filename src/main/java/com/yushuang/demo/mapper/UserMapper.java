package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.entity.User;
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
     * 分页查询用户列表（包含角色信息）
     */
    @Select("SELECT u.*, r.role_name, r.role_code " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 " +
            "WHERE u.deleted = 0 " +
            "ORDER BY u.create_time DESC")
    IPage<UserWithRole> selectUserPageWithRole(Page<UserWithRole> page);

    /**
     * 根据用户名查询用户（包含角色信息）
     */
    @Select("SELECT u.*, r.role_name, r.role_code " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 " +
            "WHERE u.deleted = 0 AND u.username = #{username}")
    UserWithRole selectUserWithRoleByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户权限列表
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
     */
    @Select("SELECT * FROM sys_user WHERE deleted = 0 AND username = #{username}")
    User selectByUsername(@Param("username") String username);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE deleted = 0 AND username = #{username} AND id != #{excludeId}")
    int checkUsernameExists(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE deleted = 0 AND email = #{email} AND id != #{excludeId}")
    int checkEmailExists(@Param("email") String email, @Param("excludeId") Long excludeId);

    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE deleted = 0 AND phone = #{phone} AND id != #{excludeId}")
    int checkPhoneExists(@Param("phone") String phone, @Param("excludeId") Long excludeId);

    /**
     * 更新最后登录信息
     */
    @Select("UPDATE sys_user SET last_login_time = NOW(), last_login_ip = #{loginIp} WHERE id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);

    /**
     * 用户角色信息VO
     */
    class UserWithRole extends User {
        private String roleName;
        private String roleCode;

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }
    }
}