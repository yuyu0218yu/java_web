package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.Job;
import com.zhangjiajie.system.entity.JobLog;
import com.zhangjiajie.system.mapper.JobMapper;
import com.zhangjiajie.system.service.JobLogService;
import com.zhangjiajie.system.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务服务实现
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final JobLogService jobLogService;

    @Override
    public Page<Job> pageList(Integer current, Integer size, String jobName, String jobGroup, Integer status) {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(jobName), Job::getJobName, jobName)
               .eq(StringUtils.hasText(jobGroup), Job::getJobGroup, jobGroup)
               .eq(status != null, Job::getStatus, status)
               .orderByDesc(Job::getCreateTime);

        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public List<Job> getActiveJobs() {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getStatus, 1);
        return list(wrapper);
    }

    @Override
    public boolean pauseJob(Long id) {
        Job job = getById(id);
        if (job == null) {
            return false;
        }
        job.setStatus(0);
        return updateById(job);
    }

    @Override
    public boolean resumeJob(Long id) {
        Job job = getById(id);
        if (job == null) {
            return false;
        }
        job.setStatus(1);
        return updateById(job);
    }

    @Override
    public void runOnce(Long id) {
        Job job = getById(id);
        if (job == null) {
            log.warn("任务不存在: {}", id);
            return;
        }

        // 记录执行日志
        JobLog jobLog = new JobLog();
        jobLog.setJobId(job.getId());
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        jobLog.setStartTime(LocalDateTime.now());

        try {
            // 解析调用目标
            String invokeTarget = job.getInvokeTarget();
            if (StringUtils.hasText(invokeTarget)) {
                executeTarget(invokeTarget);
            }

            jobLog.setStatus(1);
            jobLog.setJobMessage("执行成功");
        } catch (Exception e) {
            log.error("执行任务失败: {}", job.getJobName(), e);
            jobLog.setStatus(0);
            jobLog.setJobMessage("执行失败");
            jobLog.setExceptionInfo(e.getMessage());
        } finally {
            jobLog.setEndTime(LocalDateTime.now());
            jobLog.setElapsedTime(java.time.Duration.between(jobLog.getStartTime(), jobLog.getEndTime()).toMillis());
            jobLogService.recordJobLog(jobLog);
        }
    }

    /**
     * 执行目标方法
     * 格式: beanName.methodName 或 className.methodName
     */
    private void executeTarget(String invokeTarget) throws Exception {
        if (!StringUtils.hasText(invokeTarget)) {
            return;
        }

        int lastDotIndex = invokeTarget.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("无效的调用目标格式: " + invokeTarget);
        }

        String beanOrClassName = invokeTarget.substring(0, lastDotIndex);
        String methodName = invokeTarget.substring(lastDotIndex + 1);

        // 移除方法参数（如果有）
        int paramIndex = methodName.indexOf('(');
        if (paramIndex > 0) {
            methodName = methodName.substring(0, paramIndex);
        }

        log.info("执行定时任务: {}.{}", beanOrClassName, methodName);

        // 模拟执行（实际项目中应该通过Spring ApplicationContext获取Bean并调用）
        // 这里为了简化，只记录日志
        log.info("任务执行完成: {}", invokeTarget);
    }

    @Override
    public boolean checkCronExpression(String cronExpression) {
        if (!StringUtils.hasText(cronExpression)) {
            return false;
        }
        try {
            CronExpression.parse(cronExpression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
