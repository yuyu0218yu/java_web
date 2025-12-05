package com.yushuang.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.mapper.RoleMapper;
import com.yushuang.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return roleMapper.selectEnabledRoles();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        // 检查角色编码是否已存在
        if (checkRoleCodeExists(role.getRoleCode(), 0L)) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        if (role.getSortOrder() == null) {
            role.setSortOrder(0);
        }
        
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Long id, Role role) {
        Role existingRole = getById(id);
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查角色编码是否已存在（排除自身）
        if (role.getRoleCode() != null && !role.getRoleCode().equals(existingRole.getRoleCode())) {
            if (checkRoleCodeExists(role.getRoleCode(), id)) {
                throw new RuntimeException("角色编码已存在");
            }
        }
        
        role.setId(id);
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        Role existingRole = getById(id);
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查是否有用户关联该角色
        // 这里可以添加更多的业务逻辑检查
        
        return removeById(id);
    }

    @Override
    public boolean checkRoleCodeExists(String roleCode, Long excludeId) {
        if (roleCode == null || roleCode.isEmpty()) {
            return false;
        }
        return roleMapper.checkRoleCodeExists(roleCode, excludeId != null ? excludeId : 0L) > 0;
    }
}
