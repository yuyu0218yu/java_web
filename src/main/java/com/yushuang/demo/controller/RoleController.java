package com.yushuang.demo.controller;

import com.yushuang.demo.common.Result;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.mapper.RoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {

    private final RoleMapper roleMapper;

    /**
     * 获取启用的角色列表
     */
    @GetMapping
    @Operation(summary = "获取角色列表", description = "返回所有启用状态的角色")
    @PreAuthorize("hasAuthority('role:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Result<List<Role>> listEnabledRoles() {
        List<Role> roles = roleMapper.selectEnabledRoles();
        return Result.success(roles);
    }
}
