package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.JobLog;
import com.zhangjiajie.system.service.JobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务日志控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Tag(name = "定时任务日志管理", description = "定时任务日志管理接口")
@RestController
@RequestMapping("/api/jobs/logs")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @Operation(summary = "分页查询任务日志")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<PageResult<JobLog>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) String jobGroup,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        Page<JobLog> pageResult = jobLogService.pageList(current, size, jobName, jobGroup, status, startTime, endTime);
        return Result.success(PageResult.of(pageResult));
    }

    @Operation(summary = "获取任务日志详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<JobLog> getById(@PathVariable Long id) {
        JobLog jobLog = jobLogService.getById(id);
        if (jobLog == null) {
            return Result.error("日志不存在");
        }
        return Result.success(jobLog);
    }

    @Operation(summary = "根据任务ID查询日志")
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<PageResult<JobLog>> getByJobId(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<JobLog> pageResult = jobLogService.getByJobId(jobId, current, size);
        return Result.success(PageResult.of(pageResult));
    }

    @Operation(summary = "删除任务日志")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = jobLogService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "批量删除任务日志")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的记录");
        }
        boolean success = jobLogService.removeByIds(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "清空任务日志")
    @DeleteMapping("/clean")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Void> clean() {
        boolean success = jobLogService.cleanAll();
        return success ? Result.success() : Result.error("清空失败");
    }

    @Operation(summary = "删除指定天数之前的日志")
    @DeleteMapping("/clean/{days}")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Integer> cleanBeforeDays(@PathVariable Integer days) {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(days);
        int count = jobLogService.deleteBeforeDate(beforeDate);
        return Result.success(count);
    }
}
