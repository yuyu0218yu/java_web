package com.yushuang.demo.controller;

import com.yushuang.demo.common.Result;
import com.yushuang.demo.dto.RoleWithPermissionsDTO;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.service.MenuService;
import com.yushuang.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {

    private final RoleService roleService;
    private final MenuService menuService;

    /**
     * 获取启用的角色列表（包含权限数量）
     */
    @GetMapping
    @Operation(summary = "获取角色列表", description = "返回所有角色及其权限数量")
    @PreAuthorize("hasAuthority('role:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<RoleWithPermissionsDTO>> listRolesWithPermissions() {
        List<RoleWithPermissionsDTO> roles = roleService.getRolesWithPermissionCount();
        return Result.success(roles);
    }

    /**
     * 获取启用的角色列表（简单版本，不含权限数量）
     */
    @GetMapping("/simple")
    @Operation(summary = "获取角色列表（简单版本）", description = "返回所有启用状态的角色，不含权限数量")
    @PreAuthorize("hasAuthority('role:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<Role>> listEnabledRoles() {
        List<Role> roles = roleService.getEnabledRoles();
        return Result.success(roles);
    }

    /**
     * 获取所有角色（包括禁用的）
     */
    @GetMapping("/all")
    @Operation(summary = "获取所有角色")
    @PreAuthorize("hasAuthority('role:view') or hasRole('SUPER_ADMIN')")
    public Result<List<Role>> listAllRoles() {
        List<Role> roles = roleService.list();
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情")
    @PreAuthorize("hasAuthority('role:view') or hasRole('SUPER_ADMIN')")
    public Result<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) {
            return Result.error("角色不存在");
        }
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasAuthority('role:create') or hasRole('SUPER_ADMIN')")
    public Result<Void> createRole(@Valid @RequestBody Role role) {
        try {
            roleService.createRole(role);
            return Result.success("角色创建成功");
        } catch (Exception e) {
            log.error("角色创建失败", e);
            return Result.error("角色创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    @PreAuthorize("hasAuthority('role:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        try {
            roleService.updateRole(id, role);
            return Result.success("角色更新成功");
        } catch (Exception e) {
            log.error("角色更新失败", e);
            return Result.error("角色更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('role:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return Result.success("角色删除成功");
        } catch (Exception e) {
            log.error("角色删除失败", e);
            return Result.error("角色删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取角色的菜单ID列表
     */
    @GetMapping("/{id}/menus")
    @Operation(summary = "获取角色菜单", description = "获取角色已分配的菜单ID列表")
    @PreAuthorize("hasAuthority('role:view') or hasRole('SUPER_ADMIN')")
    public Result<List<Long>> getRoleMenus(@PathVariable Long id) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(id);
        return Result.success(menuIds);
    }

    /**
     * 分配角色菜单权限
     */
    @PutMapping("/{id}/menus")
    @Operation(summary = "分配菜单权限", description = "为角色分配菜单权限")
    @PreAuthorize("hasAuthority('role:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> assignRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        try {
            // 不允许修改超级管理员的权限
            if (id == 1L) {
                return Result.error("不能修改超级管理员的权限");
            }
            menuService.saveRoleMenus(id, menuIds);
            return Result.success("权限分配成功");
        } catch (Exception e) {
            log.error("权限分配失败", e);
            return Result.error("权限分配失败: " + e.getMessage());
        }
    }
}
