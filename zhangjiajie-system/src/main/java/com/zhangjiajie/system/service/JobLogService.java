package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.JobLog;

import java.time.LocalDateTime;

/**
 * 定时任务日志服务接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
public interface JobLogService extends IService<JobLog> {

    /**
     * 分页查询任务日志
     *
     * @param current   当前页
     * @param size      每页大小
     * @param jobName   任务名称
     * @param jobGroup  任务组
     * @param status    状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 分页数据
     */
    Page<JobLog> pageList(Integer current, Integer size, String jobName, String jobGroup,
                          Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据任务ID查询日志
     *
     * @param jobId 任务ID
     * @param limit 限制数量
     * @return 分页数据
     */
    Page<JobLog> getByJobId(Long jobId, Integer current, Integer size);

    /**
     * 清空所有日志
     *
     * @return 是否成功
     */
    boolean cleanAll();

    /**
     * 删除指定天数之前的日志
     *
     * @param beforeDate 日期
     * @return 删除数量
     */
    int deleteBeforeDate(LocalDateTime beforeDate);

    /**
     * 记录任务执行日志
     *
     * @param jobLog 日志信息
     */
    void recordJobLog(JobLog jobLog);
}
