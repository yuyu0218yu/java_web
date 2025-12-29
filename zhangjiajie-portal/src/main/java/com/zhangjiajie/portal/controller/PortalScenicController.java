package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;
import com.zhangjiajie.portal.entity.Ticket;
import com.zhangjiajie.portal.service.ScenicService;
import com.zhangjiajie.portal.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点Controller
 */
@RestController
@RequestMapping("/api/portal/scenic")
@RequiredArgsConstructor
@Tag(name = "用户端-景点", description = "景点相关接口")
public class PortalScenicController {

    private final ScenicService scenicService;
    private final TicketService ticketService;

    /**
     * 获取景点列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取景点列表")
    public Result<PageResult<Scenic>> getScenicList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {

        Page<Scenic> page = new Page<>(current, size);
        IPage<Scenic> result = scenicService.getScenicPage(page, categoryId, keyword);

        PageResult<Scenic> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取景点详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取景点详情")
    public Result<Scenic> getScenicDetail(@PathVariable Long id) {
        Scenic scenic = scenicService.getScenicDetail(id);
        if (scenic == null) {
            return Result.notFound("景点不存在");
        }

        // 增加浏览量
        scenicService.incrementViewCount(id);

        return Result.success(scenic);
    }

    /**
     * 获取景点门票列表
     */
    @GetMapping("/{id}/tickets")
    @Operation(summary = "获取景点门票列表")
    public Result<List<Ticket>> getScenicTickets(@PathVariable Long id) {
        List<Ticket> tickets = ticketService.getTicketsByScenicId(id);
        return Result.success(tickets);
    }

    /**
     * 获取单个门票详情
     */
    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "获取门票详情")
    public Result<Ticket> getTicketDetail(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getById(ticketId);
        if (ticket == null) {
            return Result.notFound("门票不存在");
        }
        // 填充景点名称
        Scenic scenic = scenicService.getById(ticket.getScenicId());
        if (scenic != null) {
            ticket.setScenicName(scenic.getScenicName());
        }
        return Result.success(ticket);
    }

    /**
     * 获取景点分类
     */
    @GetMapping("/categories")
    @Operation(summary = "获取景点分类")
    public Result<List<ScenicCategory>> getCategories() {
        List<ScenicCategory> categories = scenicService.getAllCategories();
        return Result.success(categories);
    }

    /**
     * 搜索景点
     */
    @GetMapping("/search")
    @Operation(summary = "搜索景点")
    public Result<PageResult<Scenic>> searchScenics(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "关键词") @RequestParam String keyword) {

        Page<Scenic> page = new Page<>(current, size);
        IPage<Scenic> result = scenicService.searchScenics(page, keyword);

        PageResult<Scenic> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }
}
