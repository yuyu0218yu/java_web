package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.Notice;

import java.util.List;

/**
 * 通知公告服务接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询通知公告
     */
    Page<Notice> pageList(Integer current, Integer size, String title, Integer noticeType, Integer status);

    /**
     * 获取最新通知公告
     */
    List<Notice> getLatestNotices(Integer limit);
}
