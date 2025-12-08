package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.annotation.AuditLog;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.converter.DeptConverter;
import com.zhangjiajie.system.dto.CreateDeptRequest;
import com.zhangjiajie.system.dto.UpdateDeptRequest;
import com.zhangjiajie.system.entity.Dept;
import com.zhangjiajie.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 *
 * @author yushuang
 * @since 2025-12-06
 */
@RestController
@RequestMapping("/api/depts")
@RequiredArgsConstructor
@Tag(name = "部门管理", description = "部门组织结构管理接口")
public class DeptController {

    private final DeptService deptService;

    /**
     * 获取部门树
     */
    @GetMapping("/tree")
    @Operation(summary = "获取部门树", description = "获取完整的部门树形结构")
    public Result<List<Dept>> getDeptTree() {
        return Result.success(deptService.getDeptTree());
    }

    /**
     * 获取部门下拉选项
     */
    @GetMapping("/options")
    @Operation(summary = "获取部门下拉选项", description = "获取启用状态的部门列表，用于下拉选择")
    public Result<List<Dept>> getDeptOptions() {
        return Result.success(deptService.getDeptOptions());
    }

    /**
     * 根据ID获取部门详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取详细信息")
    @PreAuthorize("hasAuthority('dept:view') or hasRole('SUPER_ADMIN')")
    public Result<Dept> getById(@PathVariable Long id) {
        return Result.success(deptService.getDeptById(id));
    }

    /**
     * 创建部门
     */
    @PostMapping
    @Operation(summary = "创建部门", description = "创建新的部门")
    @PreAuthorize("hasAuthority('dept:create') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "创建部门", module = "部门管理")
    public Result<Void> create(@Valid @RequestBody CreateDeptRequest request) {
        Dept dept = DeptConverter.toEntity(request);
        deptService.createDept(dept);
        return Result.success("创建成功");
    }

    /**
     * 更新部门
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新部门", description = "更新部门信息")
    @PreAuthorize("hasAuthority('dept:edit') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "更新部门", module = "部门管理")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateDeptRequest request) {
        Dept dept = DeptConverter.toEntity(request);
        deptService.updateDept(id, dept);
        return Result.success("更新成功");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "删除指定部门")
    @PreAuthorize("hasAuthority('dept:delete') or hasRole('SUPER_ADMIN')")
    @AuditLog(operation = "删除部门", module = "部门管理")
    public Result<Void> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success("删除成功");
    }

    /**
     * 获取子部门ID列表
     */
    @GetMapping("/{id}/children/ids")
    @Operation(summary = "获取子部门ID列表", description = "获取指定部门及所有子部门的ID")
    public Result<List<Long>> getChildDeptIds(@PathVariable Long id) {
        return Result.success(deptService.getChildDeptIds(id));
    }
}
