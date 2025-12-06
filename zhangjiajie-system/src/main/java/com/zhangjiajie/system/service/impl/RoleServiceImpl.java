package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.common.util.Assert;
import com.zhangjiajie.system.dto.RoleWithPermissionsDTO;
import com.zhangjiajie.system.entity.Role;
import com.zhangjiajie.system.mapper.RoleMapper;
import com.zhangjiajie.system.service.RoleService;
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
        Assert.notNull(role, "角色信息不能为空");
        Assert.notEmpty(role.getRoleName(), "角色名称不能为空");
        Assert.notEmpty(role.getRoleCode(), "角色编码不能为空");

        // 检查角色编码是否重复
        Assert.isFalse(checkRoleCodeExists(role.getRoleCode(), null), "角色编码已存在");

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
        Assert.notNull(id, "ID不能为空");
        Assert.notNull(role, "角色信息不能为空");

        Role existingRole = getById(id);
        Assert.found(existingRole, "角色不存在");

        // 检查角色编码是否重复
        if (StringUtils.hasText(role.getRoleCode()) && 
            !role.getRoleCode().equals(existingRole.getRoleCode())) {
            Assert.isFalse(checkRoleCodeExists(role.getRoleCode(), id), "角色编码已存在");
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
        Assert.notNull(id, "角色ID不能为空");

        // 不允许删除超级管理员角色
        Assert.isFalse(id == 1L, "不能删除超级管理员角色");

        Role role = getById(id);
        Assert.found(role, "角色不存在");

        boolean result = removeById(id);
        if (result) {
            log.info("删除角色成功: {}", role.getRoleName());
        }
        return result;
    }
}
