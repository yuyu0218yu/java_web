package com.yushuang.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yushuang.demo.entity.User;
import com.yushuang.demo.mapper.UserMapper;
import com.yushuang.demo.mapper.UserMapper.UserWithRole;
import com.yushuang.demo.util.PasswordUtil;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户列表（包含角色信息）
     */
    IPage<UserWithRole> getUserPageWithRole(Page<UserWithRole> page);

    /**
     * 根据用户名查询用户（包含角色信息）
     */
    UserWithRole getUserWithRoleByUsername(String username);

    /**
     * 用户登录
     */
    User login(String username, String password, String loginIp);

    /**
     * 创建用户
     */
    boolean createUser(User user, Long roleId);

    /**
     * 更新用户
     */
    boolean updateUser(User user, List<Long> roleIds);

    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 禁用/启用用户
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 检查用户名是否存在
     */
    boolean checkUsernameExists(String username, Long excludeId);

    /**
     * 检查邮箱是否存在
     */
    boolean checkEmailExists(String email, Long excludeId);

    /**
     * 检查手机号是否存在
     */
    boolean checkPhoneExists(String phone, Long excludeId);

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 修改用户角色
     */
    boolean updateUserRoles(Long userId, List<Long> roleIds);
}