package com.yushuang.demo.service.impl;

import com.yushuang.demo.entity.Permission;
import com.yushuang.demo.mapper.PermissionMapper;
import com.yushuang.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getPermissionTree() {
        // 查询所有权限
        List<Permission> allPermissions = permissionMapper.selectList(null);

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
                    node.put("permissionType", p.getPermissionType());
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
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPermission(Permission permission) {
        // 检查权限编码是否重复
        if (permissionMapper.checkPermissionCodeExists(permission.getPermissionCode(), null) > 0) {
            throw new RuntimeException("权限编码已存在");
        }

        int result = permissionMapper.insert(permission);
        if (result != 1) {
            throw new RuntimeException("创建权限失败");
        }

        log.info("创建权限成功: {}", permission.getPermissionName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Long id, Permission permission) {
        // 检查权限是否存在
        Permission existPermission = permissionMapper.selectById(id);
        if (existPermission == null) {
            throw new RuntimeException("权限不存在");
        }

        // 检查权限编码是否重复
        if (permissionMapper.checkPermissionCodeExists(permission.getPermissionCode(), id) > 0) {
            throw new RuntimeException("权限编码已存在");
        }

        permission.setId(id);
        int result = permissionMapper.updateById(permission);
        if (result != 1) {
            throw new RuntimeException("更新权限失败");
        }

        log.info("更新权限成功: {}", permission.getPermissionName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        // 检查权限是否存在
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }

        // 检查是否有子权限
        List<Permission> children = permissionMapper.selectPermissionsByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该权限下有子权限，无法删除");
        }

        int result = permissionMapper.deleteById(id);
        if (result != 1) {
            throw new RuntimeException("删除权限失败");
        }

        log.info("删除权限成功: {}", permission.getPermissionName());
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
}
