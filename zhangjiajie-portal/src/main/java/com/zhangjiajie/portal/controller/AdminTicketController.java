package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Ticket;
import com.zhangjiajie.portal.mapper.TicketMapper;
import com.zhangjiajie.portal.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门票管理Controller（管理端）
 */
@RestController
@RequestMapping("/api/admin/ticket")
@RequiredArgsConstructor
@Tag(name = "管理端-门票", description = "门票管理接口")
public class AdminTicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    /**
     * 获取门票列表（包含景点名称）
     */
    @GetMapping("/list")
    @Operation(summary = "获取门票列表")
    public Result<PageResult<Ticket>> getTicketList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long scenicId,
            @RequestParam(required = false) Integer ticketType,
            @RequestParam(required = false) Integer status) {

        Page<Ticket> page = new Page<>(current, size);
        IPage<Ticket> result = ticketMapper.selectTicketPage(page, scenicId, ticketType, status);

        PageResult<Ticket> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取门票详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取门票详情")
    public Result<Ticket> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getById(id);
        if (ticket == null) {
            return Result.notFound("门票不存在");
        }
        return Result.success(ticket);
    }

    /**
     * 新增门票
     */
    @PostMapping
    @Operation(summary = "新增门票")
    public Result<Void> addTicket(@RequestBody Ticket ticket) {
        ticket.setDeleted(0);
        ticketService.save(ticket);
        return Result.success("新增成功");
    }

    /**
     * 修改门票
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改门票")
    public Result<Void> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        ticket.setId(id);
        ticketService.updateById(ticket);
        return Result.success("修改成功");
    }

    /**
     * 删除门票
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除门票")
    public Result<Void> deleteTicket(@PathVariable Long id) {
        ticketService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除门票
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除门票")
    public Result<Void> batchDeleteTickets(@RequestBody List<Long> ids) {
        ticketService.removeByIds(ids);
        return Result.success("批量删除成功");
    }
}
