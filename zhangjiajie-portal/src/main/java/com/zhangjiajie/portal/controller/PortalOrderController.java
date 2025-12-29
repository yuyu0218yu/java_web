package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.portal.dto.OrderCreateRequest;
import com.zhangjiajie.portal.entity.Order;
import com.zhangjiajie.portal.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/api/portal/order")
@RequiredArgsConstructor
@Tag(name = "用户端-订单", description = "订单相关接口")
public class PortalOrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        try {
            Long userId = SecurityUtils.requireUserId();
            Order order = orderService.createOrder(userId, request);
            return Result.success("下单成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的订单列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取我的订单列表")
    public Result<PageResult<Order>> getMyOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer status) {

        Long userId = SecurityUtils.requireUserId();
        Page<Order> page = new Page<>(current, size);
        IPage<Order> result = orderService.getUserOrders(page, userId, status);

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
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        Order order = orderService.getOrderDetail(id, userId);
        if (order == null) {
            return Result.notFound("订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        try {
            Long userId = SecurityUtils.requireUserId();
            orderService.cancelOrder(id, userId);
            return Result.success("订单已取消");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据订单号查询
     */
    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据订单号查询")
    public Result<Order> getByOrderNo(@PathVariable String orderNo) {
        Order order = orderService.getByOrderNo(orderNo);
        if (order == null) {
            return Result.notFound("订单不存在");
        }
        return Result.success(order);
    }
}
