package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.Job;
import com.zhangjiajie.system.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定时任务控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Tag(name = "定时任务管理", description = "定时任务管理接口")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(summary = "分页查询定时任务")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<PageResult<Job>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) String jobGroup,
            @RequestParam(required = false) Integer status) {

        Page<Job> pageResult = jobService.pageList(current, size, jobName, jobGroup, status);
        return Result.success(PageResult.of(pageResult));
    }

    @Operation(summary = "获取定时任务详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<Job> getById(@PathVariable Long id) {
        Job job = jobService.getById(id);
        if (job == null) {
            return Result.error("定时任务不存在");
        }
        return Result.success(job);
    }

    @Operation(summary = "获取所有正常状态的任务")
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('job:view') or hasAuthority('*')")
    public Result<List<Job>> getActiveJobs() {
        return Result.success(jobService.getActiveJobs());
    }

    @Operation(summary = "创建定时任务")
    @PostMapping
    @PreAuthorize("hasAuthority('job:create') or hasAuthority('*')")
    public Result<Void> create(@RequestBody Job job) {
        // 校验cron表达式
        if (!jobService.checkCronExpression(job.getCronExpression())) {
            return Result.error("无效的cron表达式");
        }

        // 设置创建者
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            job.setCreateBy(authentication.getName());
        }

        boolean success = jobService.save(job);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新定时任务")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('job:update') or hasAuthority('*')")
    public Result<Void> update(@PathVariable Long id, @RequestBody Job job) {
        // 校验cron表达式
        if (!jobService.checkCronExpression(job.getCronExpression())) {
            return Result.error("无效的cron表达式");
        }

        job.setId(id);
        boolean success = jobService.updateById(job);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除定时任务")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = jobService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "批量删除定时任务")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('job:delete') or hasAuthority('*')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的记录");
        }
        boolean success = jobService.removeByIds(ids);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "暂停任务")
    @PutMapping("/{id}/pause")
    @PreAuthorize("hasAuthority('job:update') or hasAuthority('*')")
    public Result<Void> pauseJob(@PathVariable Long id) {
        boolean success = jobService.pauseJob(id);
        return success ? Result.success() : Result.error("暂停失败");
    }

    @Operation(summary = "恢复任务")
    @PutMapping("/{id}/resume")
    @PreAuthorize("hasAuthority('job:update') or hasAuthority('*')")
    public Result<Void> resumeJob(@PathVariable Long id) {
        boolean success = jobService.resumeJob(id);
        return success ? Result.success() : Result.error("恢复失败");
    }

    @Operation(summary = "立即执行一次任务")
    @PostMapping("/{id}/run")
    @PreAuthorize("hasAuthority('job:run') or hasAuthority('*')")
    public Result<Void> runOnce(@PathVariable Long id) {
        jobService.runOnce(id);
        return Result.success();
    }

    @Operation(summary = "校验cron表达式")
    @PostMapping("/check-cron")
    public Result<Map<String, Object>> checkCron(@RequestBody Map<String, String> params) {
        String cronExpression = params.get("cronExpression");
        boolean valid = jobService.checkCronExpression(cronExpression);
        return Result.success(Map.of("valid", valid));
    }
}
