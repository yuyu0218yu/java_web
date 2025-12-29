package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.dto.OrderCreateRequest;
import com.zhangjiajie.portal.entity.Order;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.Ticket;
import com.zhangjiajie.portal.mapper.OrderMapper;
import com.zhangjiajie.portal.mapper.ScenicMapper;
import com.zhangjiajie.portal.mapper.TicketMapper;
import com.zhangjiajie.portal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 订单Service实现
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;
    private final ScenicMapper scenicMapper;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, OrderCreateRequest request) {
        // 查询景点信息
        Scenic scenic = scenicMapper.selectById(request.getScenicId());
        if (scenic == null || scenic.getStatus() != 1) {
            throw new RuntimeException("景点不存在或已下架");
        }

        // 查询门票信息
        Ticket ticket = ticketMapper.selectById(request.getTicketId());
        if (ticket == null || ticket.getStatus() != 1) {
            throw new RuntimeException("门票不存在或已下架");
        }

        // 检查库存
        if (ticket.getStock() < request.getQuantity()) {
            throw new RuntimeException("门票库存不足");
        }

        // 减少库存
        int affected = ticketMapper.decreaseStock(ticket.getId(), request.getQuantity());
        if (affected == 0) {
            throw new RuntimeException("门票库存不足");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setScenicId(scenic.getId());
        order.setScenicName(scenic.getScenicName());
        order.setTicketId(ticket.getId());
        order.setTicketName(ticket.getTicketName());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(ticket.getSellingPrice());
        order.setTotalAmount(ticket.getSellingPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setContactName(request.getContactName());
        order.setContactPhone(request.getContactPhone());
        order.setContactIdCard(request.getContactIdCard());
        order.setVisitDate(request.getVisitDate());
        order.setRemark(request.getRemark());

        // 跳过支付，直接标记为已支付
        order.setOrderStatus(1);
        order.setPayTime(LocalDateTime.now());
        order.setPayType(1);

        // 生成电子票二维码（简化处理）
        order.setQrCode("TICKET-" + order.getOrderNo());

        save(order);
        return order;
    }

    @Override
    public IPage<Order> getUserOrders(Page<Order> page, Long userId, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);

        if (status != null) {
            wrapper.eq(Order::getOrderStatus, status);
        }

        wrapper.orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Order getOrderDetail(Long id, Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, id)
               .eq(Order::getUserId, userId);
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long id, Long userId) {
        Order order = getOrderDetail(id, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 只有待支付和已支付状态可以取消
        if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
            throw new RuntimeException("当前订单状态无法取消");
        }

        // 恢复库存
        ticketMapper.increaseStock(order.getTicketId(), order.getQuantity());

        // 更新订单状态
        order.setOrderStatus(3);
        return updateById(order);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public int countUserOrders(Long userId) {
        return orderMapper.countByUserId(userId);
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "ORD" + timestamp + uuid;
    }
}
