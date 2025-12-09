package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.OperationLog;
import com.zhangjiajie.system.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志管理控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@RestController
@RequestMapping("/api/logs/operation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "操作日志管理", description = "操作日志相关接口")
public class OperationLogController {

    private final OperationLogService operationLogService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询操作日志")
    @PreAuthorize("hasAuthority('log:operation:view') or hasRole('SUPER_ADMIN')")
    public Result<Page<OperationLog>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        Page<OperationLog> page = operationLogService.pageList(current, size, username, operation, status, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取操作日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取操作日志详情")
    @PreAuthorize("hasAuthority('log:operation:view') or hasRole('SUPER_ADMIN')")
    public Result<OperationLog> getById(@PathVariable Long id) {
        OperationLog operationLog = operationLogService.getById(id);
        if (operationLog == null) {
            return Result.error("日志不存在");
        }
        return Result.success(operationLog);
    }

    /**
     * 根据用户名查询操作日志
     */
    @GetMapping("/user/{username}")
    @Operation(summary = "根据用户名查询操作日志")
    @PreAuthorize("hasAuthority('log:operation:view') or hasRole('SUPER_ADMIN')")
    public Result<List<OperationLog>> getByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "100") Integer limit) {
        List<OperationLog> list = operationLogService.getByUsername(username, limit);
        return Result.success(list);
    }

    /**
     * 删除操作日志
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志")
    @PreAuthorize("hasAuthority('log:operation:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = operationLogService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除操作日志
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除操作日志")
    @PreAuthorize("hasAuthority('log:operation:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        boolean success = operationLogService.removeByIds(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空操作日志")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> clean() {
        boolean success = operationLogService.cleanAll();
        return success ? Result.success("清空成功") : Result.error("清空失败");
    }

    /**
     * 删除指定天数之前的日志
     */
    @DeleteMapping("/clean/{days}")
    @Operation(summary = "删除指定天数之前的日志")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> cleanBeforeDays(@PathVariable Integer days) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(days);
        int count = operationLogService.deleteBeforeDate(beforeDate);
        return Result.success("已删除 " + count + " 条日志");
    }
}
