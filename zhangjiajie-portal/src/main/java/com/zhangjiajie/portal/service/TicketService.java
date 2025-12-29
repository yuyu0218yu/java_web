package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.Ticket;

import java.util.List;

/**
 * 门票Service接口
 */
public interface TicketService extends IService<Ticket> {

    /**
     * 根据景点ID获取门票列表
     */
    List<Ticket> getTicketsByScenicId(Long scenicId);
}
