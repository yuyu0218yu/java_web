package com.yushuang.demo.controller;

import com.yushuang.demo.common.Result;
import com.yushuang.demo.dto.RoleRequest;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    /**
     * 获取启用的角色列表
     */
    @GetMapping
    @Operation(summary = "获取角色列表", description = "返回所有启用状态的角色")
    @PreAuthorize("hasAuthority('role:view') or hasRole('ADMIN')")
    public Result<List<Role>> listEnabledRoles() {
        List<Role> roles = roleService.getEnabledRoles();
        return Result.success(roles);
    }

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色", description = "创建新的角色")
    @PreAuthorize("hasAuthority('role:create') or hasRole('ADMIN')")
    public Result<Void> createRole(@Valid @RequestBody RoleRequest request) {
        try {
            Role role = new Role();
            role.setRoleName(request.getRoleName());
            role.setRoleCode(request.getRoleCode());
            role.setDescription(request.getDescription());
            role.setSortOrder(request.getSortOrder());
            role.setStatus(request.getStatus());

            boolean success = roleService.createRole(role);
            if (success) {
                return Result.success("创建角色成功");
            } else {
                return Result.error("创建角色失败");
            }
        } catch (Exception e) {
            log.error("创建角色失败", e);
            return Result.error("创建角色失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "更新指定角色信息")
    @PreAuthorize("hasAuthority('role:update') or hasRole('ADMIN')")
    public Result<Void> updateRole(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Valid @RequestBody RoleRequest request) {
        try {
            Role role = new Role();
            role.setRoleName(request.getRoleName());
            role.setRoleCode(request.getRoleCode());
            role.setDescription(request.getDescription());
            role.setSortOrder(request.getSortOrder());
            role.setStatus(request.getStatus());

            boolean success = roleService.updateRole(id, role);
            if (success) {
                return Result.success("更新角色成功");
            } else {
                return Result.error("更新角色失败");
            }
        } catch (Exception e) {
            log.error("更新角色失败", e);
            return Result.error("更新角色失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "删除指定角色")
    @PreAuthorize("hasAuthority('role:delete') or hasRole('ADMIN')")
    public Result<Void> deleteRole(@Parameter(description = "角色ID") @PathVariable Long id) {
        try {
            boolean success = roleService.deleteRole(id);
            if (success) {
                return Result.success("删除角色成功");
            } else {
                return Result.error("删除角色失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return Result.error("删除角色失败: " + e.getMessage());
        }
    }
}
