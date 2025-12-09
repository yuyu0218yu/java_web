package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.Job;

import java.util.List;

/**
 * 定时任务服务接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
public interface JobService extends IService<Job> {

    /**
     * 分页查询定时任务
     *
     * @param current  当前页
     * @param size     每页大小
     * @param jobName  任务名称
     * @param jobGroup 任务组
     * @param status   状态
     * @return 分页数据
     */
    Page<Job> pageList(Integer current, Integer size, String jobName, String jobGroup, Integer status);

    /**
     * 获取所有正常状态的任务
     *
     * @return 任务列表
     */
    List<Job> getActiveJobs();

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    boolean pauseJob(Long id);

    /**
     * 恢复任务
     *
     * @param id 任务ID
     * @return 是否成功
     */
    boolean resumeJob(Long id);

    /**
     * 立即执行一次任务
     *
     * @param id 任务ID
     */
    void runOnce(Long id);

    /**
     * 校验cron表达式
     *
     * @param cronExpression cron表达式
     * @return 是否有效
     */
    boolean checkCronExpression(String cronExpression);
}
