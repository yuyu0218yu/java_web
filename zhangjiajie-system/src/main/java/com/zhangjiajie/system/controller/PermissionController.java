package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.dto.PermissionTreeNode;
import com.zhangjiajie.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "提供权限树等接口")
public class PermissionController {

    private final MenuService menuService;

    /**
     * 获取权限树（含目录/菜单/按钮）
     */
    @GetMapping("/tree")
    @Operation(summary = "获取权限树", description = "返回完整的权限树结构")
    @PreAuthorize("hasAuthority('permission:view') or hasRole('SUPER_ADMIN')")
    public Result<List<PermissionTreeNode>> getPermissionTree() {
        return Result.success(menuService.getPermissionTree());
    }
}
