package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.JobLog;
import com.zhangjiajie.system.mapper.JobLogMapper;
import com.zhangjiajie.system.service.JobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 定时任务日志服务实现
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Service
@RequiredArgsConstructor
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    @Override
    public Page<JobLog> pageList(Integer current, Integer size, String jobName, String jobGroup,
                                  Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<JobLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(jobName), JobLog::getJobName, jobName)
               .eq(StringUtils.hasText(jobGroup), JobLog::getJobGroup, jobGroup)
               .eq(status != null, JobLog::getStatus, status)
               .ge(startTime != null, JobLog::getCreateTime, startTime)
               .le(endTime != null, JobLog::getCreateTime, endTime)
               .orderByDesc(JobLog::getCreateTime);

        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public Page<JobLog> getByJobId(Long jobId, Integer current, Integer size) {
        LambdaQueryWrapper<JobLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobLog::getJobId, jobId)
               .orderByDesc(JobLog::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public boolean cleanAll() {
        LambdaUpdateWrapper<JobLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(JobLog::getDeleted, 1)
               .eq(JobLog::getDeleted, 0);
        return update(wrapper);
    }

    @Override
    public int deleteBeforeDate(LocalDateTime beforeDate) {
        return baseMapper.deleteLogsBeforeDate(beforeDate);
    }

    @Override
    @Async
    public void recordJobLog(JobLog jobLog) {
        save(jobLog);
    }
}
