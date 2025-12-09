package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.Notice;
import com.zhangjiajie.system.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Tag(name = "通知公告管理", description = "通知公告管理接口")
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页查询通知公告")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('notice:view') or hasAuthority('*')")
    public Result<PageResult<Notice>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer noticeType,
            @RequestParam(required = false) Integer status) {

        Page<Notice> pageResult = noticeService.pageList(current, size, title, noticeType, status);
        return Result.success(PageResult.of(pageResult));
    }

    @Operation(summary = "获取通知公告详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('notice:view') or hasAuthority('*')")
    public Result<Notice> getById(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        if (notice == null) {
            return Result.error("通知公告不存在");
        }
        return Result.success(notice);
    }

    @Operation(summary = "获取最新通知公告列表")
    @GetMapping("/latest")
    public Result<List<Notice>> getLatest(@RequestParam(defaultValue = "10") Integer limit) {
        List<Notice> notices = noticeService.getLatestNotices(limit);
        return Result.success(notices);
    }

    @Operation(summary = "创建通知公告")
    @PostMapping
    @PreAuthorize("hasAuthority('notice:create') or hasAuthority('*')")
    public Result<Void> create(@RequestBody Notice notice) {
        // 设置创建者
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            notice.setCreateBy(authentication.getName());
        }

        boolean success = noticeService.save(notice);
        return success ? Result.success() : Result.error("创建失败");
    }

    @Operation(summary = "更新通知公告")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('notice:update') or hasAuthority('*')")
    public Result<Void> update(@PathVariable Long id, @RequestBody Notice notice) {
        notice.setId(id);
        boolean success = noticeService.updateById(notice);
        return success ? Result.success() : Result.error("更新失败");
    }

    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('notice:delete') or hasAuthority('*')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = noticeService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Operation(summary = "批量删除通知公告")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('notice:delete') or hasAuthority('*')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的记录");
        }
        boolean success = noticeService.removeByIds(ids);
        return success ? Result.success() : Result.error("删除失败");
    }
}
