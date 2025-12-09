package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.Notice;
import com.zhangjiajie.system.mapper.NoticeMapper;
import com.zhangjiajie.system.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 通知公告服务实现
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Page<Notice> pageList(Integer current, Integer size, String title, Integer noticeType, Integer status) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title), Notice::getTitle, title)
               .eq(noticeType != null, Notice::getNoticeType, noticeType)
               .eq(status != null, Notice::getStatus, status)
               .orderByDesc(Notice::getCreateTime);

        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public List<Notice> getLatestNotices(Integer limit) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1)
               .orderByDesc(Notice::getCreateTime)
               .last("LIMIT " + (limit != null ? limit : 10));

        return list(wrapper);
    }
}
