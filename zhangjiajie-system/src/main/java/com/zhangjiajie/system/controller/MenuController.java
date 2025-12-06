package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.system.entity.Menu;
import com.zhangjiajie.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 菜单管理控制器
 *
 * @author yushuang
 * @since 2025-12-05
 */
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "菜单管理", description = "菜单权限相关接口")
public class MenuController {

    private final MenuService menuService;

    /**
     * 获取当前用户的菜单树（用于动态渲染侧边栏）
     */
    @GetMapping("/user")
    @Operation(summary = "获取当前用户菜单树", description = "根据当前登录用户的角色获取可访问的菜单")
    public Result<List<Menu>> getUserMenus() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.unauthorized("未登录");
        }
        List<Menu> menus = menuService.getUserMenuTree(userId);
        return Result.success(menus);
    }

    /**
     * 获取当前用户的权限列表
     */
    @GetMapping("/user/permissions")
    @Operation(summary = "获取当前用户权限列表")
    public Result<List<String>> getUserPermissions() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.unauthorized("未登录");
        }
        List<String> permissions = menuService.getUserPermissions(userId);
        return Result.success(permissions);
    }

    /**
     * 获取所有菜单树（管理用）
     */
    @GetMapping("/tree")
    @Operation(summary = "获取所有菜单树", description = "获取完整的菜单树结构，用于菜单管理")
    @PreAuthorize("hasAuthority('menu:view') or hasRole('SUPER_ADMIN')")
    public Result<List<Menu>> getAllMenuTree() {
        List<Menu> menus = menuService.getAllMenuTree();
        return Result.success(menus);
    }

    /**
     * 获取所有菜单列表（平铺）
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有菜单列表")
    @PreAuthorize("hasAuthority('menu:view') or hasRole('SUPER_ADMIN')")
    public Result<List<Menu>> getAllMenuList() {
        List<Menu> menus = menuService.list();
        return Result.success(menus);
    }

    /**
     * 根据角色ID获取菜单ID列表
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "获取角色的菜单ID列表")
    @PreAuthorize("hasAuthority('role:view') or hasRole('SUPER_ADMIN')")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }

    /**
     * 保存角色菜单关联（分配权限）
     */
    @PostMapping("/role/{roleId}")
    @Operation(summary = "分配角色菜单权限", description = "为角色分配菜单权限")
    @PreAuthorize("hasAuthority('role:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> saveRoleMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        try {
            menuService.saveRoleMenus(roleId, menuIds);
            return Result.success("权限分配成功");
        } catch (Exception e) {
            log.error("权限分配失败", e);
            return Result.error("权限分配失败: " + e.getMessage());
        }
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @Operation(summary = "新增菜单")
    @PreAuthorize("hasAuthority('menu:create') or hasRole('SUPER_ADMIN')")
    public Result<Void> createMenu(@Valid @RequestBody Menu menu) {
        try {
            menuService.save(menu);
            return Result.success("菜单创建成功");
        } catch (Exception e) {
            log.error("菜单创建失败", e);
            return Result.error("菜单创建失败: " + e.getMessage());
        }
    }

    /**
     * 修改菜单
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改菜单")
    @PreAuthorize("hasAuthority('menu:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> updateMenu(@PathVariable Long id, @Valid @RequestBody Menu menu) {
        try {
            menu.setId(id);
            menuService.updateById(menu);
            return Result.success("菜单更新成功");
        } catch (Exception e) {
            log.error("菜单更新失败", e);
            return Result.error("菜单更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    @PreAuthorize("hasAuthority('menu:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        try {
            menuService.removeById(id);
            return Result.success("菜单删除成功");
        } catch (Exception e) {
            log.error("菜单删除失败", e);
            return Result.error("菜单删除失败: " + e.getMessage());
        }
    }
}
