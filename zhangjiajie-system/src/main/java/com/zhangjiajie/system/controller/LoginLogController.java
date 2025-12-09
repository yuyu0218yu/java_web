package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.LoginLog;
import com.zhangjiajie.system.service.LoginLogService;
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
 * 登录日志管理控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@RestController
@RequestMapping("/api/logs/login")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "登录日志管理", description = "登录日志相关接口")
public class LoginLogController {

    private final LoginLogService loginLogService;

    /**
     * 分页查询登录日志
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询登录日志")
    @PreAuthorize("hasAuthority('log:login:view') or hasRole('SUPER_ADMIN')")
    public Result<Page<LoginLog>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        Page<LoginLog> page = loginLogService.pageList(current, size, username, ip, status, startTime, endTime);
        return Result.success(page);
    }

    /**
     * 获取登录日志详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取登录日志详情")
    @PreAuthorize("hasAuthority('log:login:view') or hasRole('SUPER_ADMIN')")
    public Result<LoginLog> getById(@PathVariable Long id) {
        LoginLog loginLog = loginLogService.getById(id);
        if (loginLog == null) {
            return Result.error("日志不存在");
        }
        return Result.success(loginLog);
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除登录日志")
    @PreAuthorize("hasAuthority('log:login:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = loginLogService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除登录日志
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除登录日志")
    @PreAuthorize("hasAuthority('log:login:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        boolean success = loginLogService.removeByIds(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 清空登录日志
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空登录日志")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Void> clean() {
        boolean success = loginLogService.cleanAll();
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
        int count = loginLogService.deleteBeforeDate(beforeDate);
        return Result.success("已删除 " + count + " 条日志");
    }
}
