package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.OperationLog;
import com.zhangjiajie.system.mapper.OperationLogMapper;
import com.zhangjiajie.system.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务实现
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public Page<OperationLog> pageList(Integer current, Integer size, String username,
                                        String operation, Integer status,
                                        LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), OperationLog::getUsername, username)
               .like(StringUtils.hasText(operation), OperationLog::getOperation, operation)
               .eq(status != null, OperationLog::getStatus, status)
               .ge(startTime != null, OperationLog::getCreateTime, startTime)
               .le(endTime != null, OperationLog::getCreateTime, endTime)
               .orderByDesc(OperationLog::getCreateTime);

        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public List<OperationLog> getByUserId(Long userId, Integer limit) {
        return baseMapper.selectByUserId(userId, limit);
    }

    @Override
    public List<OperationLog> getByUsername(String username, Integer limit) {
        return baseMapper.selectByUsername(username, limit);
    }

    @Override
    public boolean cleanAll() {
        LambdaUpdateWrapper<OperationLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OperationLog::getDeleted, 1)
               .eq(OperationLog::getDeleted, 0);
        return update(wrapper);
    }

    @Override
    public int deleteBeforeDate(LocalDateTime beforeDate) {
        return baseMapper.deleteLogsBeforeDate(beforeDate);
    }
}
