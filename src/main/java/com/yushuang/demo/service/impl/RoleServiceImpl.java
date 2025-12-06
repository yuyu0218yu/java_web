package com.yushuang.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushuang.demo.dto.RoleWithPermissionsDTO;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.mapper.RoleMapper;
import com.yushuang.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色服务实现类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<Role> getEnabledRoles() {
        return lambdaQuery()
                .eq(Role::getStatus, Role.Status.ENABLED.getCode())
                .orderByAsc(Role::getSortOrder)
                .list();
    }

    @Override
    public Role getByRoleCode(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return null;
        }
        return lambdaQuery()
                .eq(Role::getRoleCode, roleCode)
                .one();
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<RoleWithPermissionsDTO> getRolesWithPermissionCount() {
        return roleMapper.selectRolesWithPermissionCount();
    }

    @Override
    public boolean checkRoleCodeExists(String roleCode, Long excludeId) {
        if (!StringUtils.hasText(roleCode)) {
            return false;
        }
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleCode);
        if (excludeId != null) {
            wrapper.ne(Role::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        if (role == null || !StringUtils.hasText(role.getRoleName()) || !StringUtils.hasText(role.getRoleCode())) {
            throw new RuntimeException("角色名称和编码不能为空");
        }

        // 检查角色编码是否重复
        if (checkRoleCodeExists(role.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }

        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(Role.Status.ENABLED.getCode());
        }
        if (role.getSortOrder() == null) {
            role.setSortOrder(0);
        }

        boolean result = save(role);
        if (result) {
            log.info("创建角色成功: {}", role.getRoleName());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Long id, Role role) {
        if (id == null || role == null) {
            throw new RuntimeException("参数不能为空");
        }

        Role existingRole = getById(id);
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }

        // 检查角色编码是否重复
        if (StringUtils.hasText(role.getRoleCode()) && 
            !role.getRoleCode().equals(existingRole.getRoleCode()) &&
            checkRoleCodeExists(role.getRoleCode(), id)) {
            throw new RuntimeException("角色编码已存在");
        }

        role.setId(id);
        boolean result = updateById(role);
        if (result) {
            log.info("更新角色成功: {}", role.getRoleName());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        if (id == null) {
            throw new RuntimeException("角色ID不能为空");
        }

        // 不允许删除超级管理员角色
        if (id == 1L) {
            throw new RuntimeException("不能删除超级管理员角色");
        }

        Role role = getById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        boolean result = removeById(id);
        if (result) {
            log.info("删除角色成功: {}", role.getRoleName());
        }
        return result;
    }
}
