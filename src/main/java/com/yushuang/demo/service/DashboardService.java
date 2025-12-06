package com.yushuang.demo.service;

import com.yushuang.demo.dto.DashboardStatistics;

/**
 * 仪表板服务接口
 *
 * @author yushuang
 * @since 2025-12-06
 */
public interface DashboardService {

    /**
     * 获取仪表板统计数据
     */
    DashboardStatistics getStatistics();
}
