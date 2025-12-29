package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.dto.OrderCreateRequest;
import com.zhangjiajie.portal.entity.Order;

/**
 * 订单Service接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     */
    Order createOrder(Long userId, OrderCreateRequest request);

    /**
     * 获取用户订单列表
     */
    IPage<Order> getUserOrders(Page<Order> page, Long userId, Integer status);

    /**
     * 获取订单详情
     */
    Order getOrderDetail(Long id, Long userId);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long id, Long userId);

    /**
     * 根据订单号查询
     */
    Order getByOrderNo(String orderNo);

    /**
     * 统计用户订单
     */
    int countUserOrders(Long userId);
}
