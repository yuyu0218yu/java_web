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
     * @param period 图表周期：week（周）、month（月）、year（年）
     */
    DashboardStatistics getStatistics(String period);
}
