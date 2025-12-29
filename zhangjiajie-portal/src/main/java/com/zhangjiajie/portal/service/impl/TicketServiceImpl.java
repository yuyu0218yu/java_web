package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.Ticket;
import com.zhangjiajie.portal.mapper.TicketMapper;
import com.zhangjiajie.portal.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门票Service实现
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    private final TicketMapper ticketMapper;

    @Override
    public List<Ticket> getTicketsByScenicId(Long scenicId) {
        return ticketMapper.selectTicketsByScenicId(scenicId);
    }
}
