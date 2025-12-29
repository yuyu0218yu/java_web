package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Order;
import com.zhangjiajie.portal.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理Controller（管理端）
 */
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
@Tag(name = "管理端-订单", description = "订单管理接口")
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * 获取订单列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取订单列表")
    public Result<PageResult<Order>> getOrderList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) Long userId) {

        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(orderNo), Order::getOrderNo, orderNo)
               .eq(orderStatus != null, Order::getOrderStatus, orderStatus)
               .eq(userId != null, Order::getUserId, userId)
               .eq(Order::getDeleted, 0)
               .orderByDesc(Order::getCreateTime);

        IPage<Order> result = orderService.page(page, wrapper);

        PageResult<Order> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情")
    public Result<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.notFound("订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新订单状态")
    public Result<Void> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(status);
        orderService.updateById(order);
        return Result.success("状态更新成功");
    }

    /**
     * 核销订单（使用门票）
     */
    @PutMapping("/{id}/verify")
    @Operation(summary = "核销订单")
    public Result<Void> verifyOrder(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.notFound("订单不存在");
        }
        if (order.getOrderStatus() != 1) {
            return Result.error("订单状态不正确，无法核销");
        }
        order.setOrderStatus(2); // 已完成
        orderService.updateById(order);
        return Result.success("核销成功");
    }

    /**
     * 退款
     */
    @PutMapping("/{id}/refund")
    @Operation(summary = "退款")
    public Result<Void> refundOrder(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.notFound("订单不存在");
        }
        if (order.getOrderStatus() != 1) {
            return Result.error("订单状态不正确，无法退款");
        }
        order.setOrderStatus(4); // 已退款
        orderService.updateById(order);
        return Result.success("退款成功");
    }
}
