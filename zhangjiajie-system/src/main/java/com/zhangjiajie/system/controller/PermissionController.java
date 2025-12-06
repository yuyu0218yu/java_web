package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.Permission;
import com.zhangjiajie.system.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "权限管理", description = "权限管理相关接口")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取所有权限列表
     */
    @GetMapping
    @Operation(summary = "获取权限列表", description = "获取所有权限列表")
    @PreAuthorize("hasAuthority('permission:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<Permission>> getPermissionList() {
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            return Result.success(permissions);
        } catch (Exception e) {
            log.error("获取权限列表失败", e);
            return Result.error("获取权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限树形结构
     */
    @GetMapping("/tree")
    @Operation(summary = "获取权限树", description = "获取权限树形结构")
    @PreAuthorize("hasAuthority('permission:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<Map<String, Object>>> getPermissionTree() {
        try {
            List<Map<String, Object>> tree = permissionService.getPermissionTree();
            return Result.success(tree);
        } catch (Exception e) {
            log.error("获取权限树失败", e);
            return Result.error("获取权限树失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取权限
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取权限详情", description = "根据ID获取权限详情")
    @PreAuthorize("hasAuthority('permission:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Permission> getPermissionById(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            return Result.success(permission);
        } catch (Exception e) {
            log.error("获取权限详情失败", e);
            return Result.error("获取权限详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建权限
     */
    @PostMapping
    @Operation(summary = "创建权限", description = "创建新的权限")
    @PreAuthorize("hasAuthority('permission:create') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> createPermission(@Valid @RequestBody Permission permission) {
        try {
            permissionService.createPermission(permission);
            return Result.success("创建权限成功");
        } catch (Exception e) {
            log.error("创建权限失败", e);
            return Result.error("创建权限失败: " + e.getMessage());
        }
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新权限", description = "更新指定权限信息")
    @PreAuthorize("hasAuthority('permission:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> updatePermission(@PathVariable Long id, @Valid @RequestBody Permission permission) {
        try {
            permissionService.updatePermission(id, permission);
            return Result.success("更新权限成功");
        } catch (Exception e) {
            log.error("更新权限失败", e);
            return Result.error("更新权限失败: " + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限", description = "删除指定权限")
    @PreAuthorize("hasAuthority('permission:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<Void> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return Result.success("删除权限成功");
        } catch (Exception e) {
            log.error("删除权限失败", e);
            return Result.error("删除权限失败: " + e.getMessage());
        }
    }
}
