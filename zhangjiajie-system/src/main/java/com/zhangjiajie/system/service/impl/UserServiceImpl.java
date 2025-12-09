package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.dto.UserExportDTO;
import com.zhangjiajie.system.dto.UserImportDTO;
import com.zhangjiajie.system.dto.UserWithRole;
import com.zhangjiajie.system.entity.Dept;
import com.zhangjiajie.system.entity.Role;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.entity.UserRole;
import com.zhangjiajie.system.mapper.DeptMapper;
import com.zhangjiajie.system.mapper.RoleMapper;
import com.zhangjiajie.system.mapper.UserMapper;
import com.zhangjiajie.system.mapper.UserRoleMapper;
import com.zhangjiajie.system.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final DeptMapper deptMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public IPage<UserWithRole> getUserPageWithRole(Page<UserWithRole> page) {
        return userMapper.selectUserPageWithRole(page);
    }

    @Override
    public UserWithRole getUserWithRoleByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.selectUserWithRoleByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(String username, String password, String loginIp) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }

        UserWithRole user = getUserWithRoleByUsername(username);
        if (user == null || user.getStatus() != User.Status.ENABLED.getCode()) {
            return null;
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // 更新最后登录信息
        userMapper.updateLastLoginInfo(user.getId(), loginIp);

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user, Long roleId) {
        if (user == null || !StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            return false;
        }

        // 检查用户名是否存在
        if (checkUsernameExists(user.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否存在
        if (StringUtils.hasText(user.getEmail()) && checkEmailExists(user.getEmail(), null)) {
            throw new RuntimeException("邮箱已存在");
        }

        // 检查手机号是否存在
        if (StringUtils.hasText(user.getPhone()) && checkPhoneExists(user.getPhone(), null)) {
            throw new RuntimeException("手机号已存在");
        }

        // 检查角色是否存在
        if (roleId != null) {
            Role role = roleMapper.selectById(roleId);
            if (role == null || role.getDeleted() == 1 || role.getStatus() != Role.Status.ENABLED.getCode()) {
                throw new RuntimeException("指定的角色不存在或已禁用");
            }
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(User.Status.ENABLED.getCode());
        }

        // 保存用户
        boolean result = save(user);
        if (result && roleId != null) {
            // 分配角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user, List<Long> roleIds) {
        if (user == null || user.getId() == null) {
            return false;
        }

        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否存在
        if (StringUtils.hasText(user.getUsername()) &&
            !user.getUsername().equals(existingUser.getUsername()) &&
            checkUsernameExists(user.getUsername(), user.getId())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否存在
        if (StringUtils.hasText(user.getEmail()) &&
            !user.getEmail().equals(existingUser.getEmail()) &&
            checkEmailExists(user.getEmail(), user.getId())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 检查手机号是否存在
        if (StringUtils.hasText(user.getPhone()) &&
            !user.getPhone().equals(existingUser.getPhone()) &&
            checkPhoneExists(user.getPhone(), user.getId())) {
            throw new RuntimeException("手机号已存在");
        }

        // 更新用户基本信息
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);

        // 更新用户角色
        if (result && roleIds != null) {
            updateUserRoles(user.getId(), roleIds);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 加密新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());

        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        if (userId == null || !StringUtils.hasText(newPassword)) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 加密新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());

        return updateById(user);
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());

        return updateById(user);
    }

    @Override
    public boolean checkUsernameExists(String username, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return checkFieldExists(User::getUsername, username, excludeId);
    }

    @Override
    public boolean checkEmailExists(String email, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return checkFieldExists(User::getEmail, email, excludeId);
    }

    @Override
    public boolean checkPhoneExists(String phone, Long excludeId) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return checkFieldExists(User::getPhone, phone, excludeId);
    }

    /**
     * 通用字段唯一性检查方法
     * 使用LambdaQueryWrapper替换自定义SQL
     * 
     * @param field 要检查的字段
     * @param value 字段值
     * @param excludeId 要排除的ID（用于更新时排除自身，传null则不排除任何记录）
     * @return 是否存在
     */
    private <V> boolean checkFieldExists(SFunction<User, V> field, V value, Long excludeId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(field, value);
        if (excludeId != null) {
            wrapper.ne(User::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return userMapper.selectUserPermissions(userId);
    }

    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        if (userId == null || !StringUtils.hasText(permissionCode)) {
            return false;
        }

        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            return false;
        }

        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 删除现有角色关联
        userRoleMapper.deleteByUserId(userId);

        // 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            // 检查所有角色是否存在且启用
            for (Long roleId : roleIds) {
                Role role = roleMapper.selectById(roleId);
                if (role == null || role.getDeleted() == 1 || role.getStatus() != Role.Status.ENABLED.getCode()) {
                    throw new RuntimeException("角色ID " + roleId + " 不存在或已禁用");
                }
            }

            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList());

            for (UserRole userRole : userRoles) {
                userRoleMapper.insert(userRole);
            }
        }

        return true;
    }

    @Override
    public List<UserExportDTO> getExportData() {
        // 获取所有用户（带角色信息）
        List<UserWithRole> users = userMapper.selectAllUsersWithRole();

        // 获取所有部门映射
        Map<Long, String> deptMap = new HashMap<>();
        List<Dept> depts = deptMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getDeleted, 0));
        for (Dept dept : depts) {
            deptMap.put(dept.getId(), dept.getDeptName());
        }

        // 转换为导出DTO
        return users.stream().map(user -> {
            UserExportDTO dto = new UserExportDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setRealName(user.getRealName());
            dto.setNickname(user.getNickname());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setDeptName(user.getDeptId() != null ? deptMap.get(user.getDeptId()) : null);
            dto.setGenderText(getGenderText(user.getGender()));
            dto.setStatusText(user.getStatus() != null && user.getStatus() == 1 ? "启用" : "禁用");
            dto.setBirthday(user.getBirthday());
            dto.setLastLoginTime(user.getLastLoginTime());
            dto.setLastLoginIp(user.getLastLoginIp());
            dto.setCreateTime(user.getCreateTime());
            dto.setRemark(user.getRemark());
            return dto;
        }).collect(Collectors.toList());
    }

    private String getGenderText(Integer gender) {
        if (gender == null) return "未知";
        return switch (gender) {
            case 1 -> "男";
            case 2 -> "女";
            default -> "未知";
        };
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importUsers(List<UserImportDTO> importData) {
        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();

        // 获取部门名称到ID的映射
        Map<String, Long> deptNameToIdMap = new HashMap<>();
        List<Dept> depts = deptMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getDeleted, 0));
        for (Dept dept : depts) {
            deptNameToIdMap.put(dept.getDeptName(), dept.getId());
        }

        for (int i = 0; i < importData.size(); i++) {
            UserImportDTO dto = importData.get(i);
            int rowNum = i + 2; // Excel从第2行开始（第1行是表头）

            try {
                // 验证必填字段
                if (!StringUtils.hasText(dto.getUsername())) {
                    failCount++;
                    failMessages.add("第" + rowNum + "行：用户名不能为空");
                    continue;
                }

                // 检查用户名是否已存在
                if (checkUsernameExists(dto.getUsername(), null)) {
                    failCount++;
                    failMessages.add("第" + rowNum + "行：用户名 [" + dto.getUsername() + "] 已存在");
                    continue;
                }

                // 检查邮箱是否已存在
                if (StringUtils.hasText(dto.getEmail()) && checkEmailExists(dto.getEmail(), null)) {
                    failCount++;
                    failMessages.add("第" + rowNum + "行：邮箱 [" + dto.getEmail() + "] 已存在");
                    continue;
                }

                // 检查手机号是否已存在
                if (StringUtils.hasText(dto.getPhone()) && checkPhoneExists(dto.getPhone(), null)) {
                    failCount++;
                    failMessages.add("第" + rowNum + "行：手机号 [" + dto.getPhone() + "] 已存在");
                    continue;
                }

                // 创建用户
                User user = new User();
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode("123456")); // 默认密码
                user.setRealName(dto.getRealName());
                user.setNickname(dto.getNickname());
                user.setEmail(dto.getEmail());
                user.setPhone(dto.getPhone());
                user.setBirthday(dto.getBirthday());
                user.setRemark(dto.getRemark());
                user.setStatus(1); // 默认启用

                // 解析性别
                if (StringUtils.hasText(dto.getGender())) {
                    user.setGender(parseGender(dto.getGender()));
                }

                // 解析部门
                if (StringUtils.hasText(dto.getDeptName())) {
                    Long deptId = deptNameToIdMap.get(dto.getDeptName());
                    if (deptId != null) {
                        user.setDeptId(deptId);
                    }
                }

                save(user);
                successCount++;

            } catch (Exception e) {
                failCount++;
                failMessages.add("第" + rowNum + "行：导入失败 - " + e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failMessages", failMessages);
        result.put("message", String.format("导入完成，成功 %d 条，失败 %d 条", successCount, failCount));

        return result;
    }

    private Integer parseGender(String gender) {
        if ("男".equals(gender)) return 1;
        if ("女".equals(gender)) return 2;
        return 0;
    }
}