package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.common.util.Assert;
import com.zhangjiajie.system.entity.Permission;
import com.zhangjiajie.system.mapper.PermissionMapper;
import com.zhangjiajie.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return list();
    }

    @Override
    public List<Map<String, Object>> getPermissionTree() {
        // 查询所有权限
        List<Permission> allPermissions = list();

        // 构建树形结构
        return buildTree(allPermissions, 0L);
    }

    /**
     * 递归构建权限树
     */
    private List<Map<String, Object>> buildTree(List<Permission> allPermissions, Long parentId) {
        return allPermissions.stream()
                .filter(p -> {
                    Long pid = p.getParentId();
                    return (pid == null && parentId == 0L) || (pid != null && pid.equals(parentId));
                })
                .map(p -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", p.getId());
                    node.put("label", p.getPermissionName());
                    node.put("permissionName", p.getPermissionName());
                    node.put("permissionCode", p.getPermissionCode());
                    node.put("resourceType", p.getResourceType());
                    node.put("parentId", p.getParentId());
                    node.put("path", p.getPath());
                    node.put("component", p.getComponent());
                    node.put("icon", p.getIcon());
                    node.put("sortOrder", p.getSortOrder());
                    node.put("status", p.getStatus());
                    node.put("remark", p.getRemark());

                    // 递归获取子节点
                    List<Map<String, Object>> children = buildTree(allPermissions, p.getId());
                    if (!children.isEmpty()) {
                        node.put("children", children);
                    }

                    return node;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Permission getPermissionById(Long id) {
        Permission permission = getById(id);
        Assert.found(permission, "权限不存在");
        return permission;
    }

    @Override
    public boolean checkPermissionCodeExists(String permissionCode, Long excludeId) {
        if (!StringUtils.hasText(permissionCode)) {
            return false;
        }
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPermissionCode, permissionCode);
        if (excludeId != null) {
            wrapper.ne(Permission::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public List<Permission> getPermissionsByParentId(Long parentId) {
        return lambdaQuery()
                .eq(Permission::getParentId, parentId)
                .orderByAsc(Permission::getSortOrder)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPermission(Permission permission) {
        // 检查权限编码是否重复
        Assert.isFalse(checkPermissionCodeExists(permission.getPermissionCode(), null), "权限编码已存在");

        boolean result = save(permission);
        Assert.isTrue(result, "创建权限失败");

        log.info("创建权限成功: {}", permission.getPermissionName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Long id, Permission permission) {
        // 检查权限是否存在
        Permission existPermission = getById(id);
        Assert.found(existPermission, "权限不存在");

        // 检查权限编码是否重复
        Assert.isFalse(checkPermissionCodeExists(permission.getPermissionCode(), id), "权限编码已存在");

        permission.setId(id);
        boolean result = updateById(permission);
        Assert.isTrue(result, "更新权限失败");

        log.info("更新权限成功: {}", permission.getPermissionName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        // 检查权限是否存在
        Permission permission = getById(id);
        Assert.found(permission, "权限不存在");

        // 检查是否有子权限
        List<Permission> children = getPermissionsByParentId(id);
        Assert.isTrue(children.isEmpty(), "该权限下有子权限，无法删除");

        boolean result = removeById(id);
        Assert.isTrue(result, "删除权限失败");

        log.info("删除权限成功: {}", permission.getPermissionName());
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
}
