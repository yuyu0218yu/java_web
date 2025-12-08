package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.system.dto.DashboardStatistics;
import com.zhangjiajie.system.entity.OperationLog;

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

    /**
     * 获取全部操作日志（分页）
     * @param current 页码
     * @param size 每页数量
     */
    Page<OperationLog> getAllActivities(Integer current, Integer size);
}
