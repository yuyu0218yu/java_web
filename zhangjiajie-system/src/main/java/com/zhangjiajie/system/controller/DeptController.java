package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.annotation.AuditLog;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.Dept;
import com.zhangjiajie.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 结构管理控制器
 *
 * @author yushuang
 * @since 2025-12-06
 */
@RestController
@RequestMapping("/api/depts")
@RequiredArgsConstructor
@Tag(name = "结构管理")
public class DeptController {

    private final DeptService deptService;

    /**
     * 获取结构树
     */
    @GetMapping("/tree")
    @Operation(summary = "获取结构树", description = "获取完整的结构树形结构")
    public Result<List<Dept>> getDeptTree() {
        return Result.success(deptService.getDeptTree());
    }

    /**
     * 获取结构下拉选项
     */
    @GetMapping("/options")
    @Operation(summary = "获取结构下拉选项", description = "获取启用状态的结构列表，用于下拉选择")
    public Result<List<Dept>> getDeptOptions() {
        List<Dept> options = deptService.getDeptOptions();
        return Result.success(options);
    }

    /**
     * 根据ID获取结构
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取结构详情")
    public Result<Dept> getById(@PathVariable Long id) {
        return Result.success(deptService.getDeptById(id));
    }

    /**
     * 创建结构
     */
    @PostMapping
    @Operation(summary = "创建结构")
    @PreAuthorize("hasAuthority('dept:create') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "创建结构", module = "结构管理")
    public Result<Void> create(@RequestBody Dept dept) {
        deptService.createDept(dept);
        return Result.success("创建成功");
    }

    /**
     * 更新结构
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新结构")
    @PreAuthorize("hasAuthority('dept:edit') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "更新结构", module = "结构管理")
    public Result<Void> update(@PathVariable Long id, @RequestBody Dept dept) {
        deptService.updateDept(id, dept);
        return Result.success("更新成功");
    }

    /**
     * 删除结构
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除结构")
    @PreAuthorize("hasAuthority('dept:delete') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "删除结构", module = "结构管理")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success("删除成功");
    }

    /**
     * 获取子结构ID列表
     */
    @GetMapping("/{id}/children/ids")
    @Operation(summary = "获取子结构ID列表", description = "获取指定结构及所有子结构的ID")
    public Result<List<Long>> getChildDeptIds(@PathVariable Long id) {
        return Result.success(deptService.getChildDeptIds(id));
    }
}
