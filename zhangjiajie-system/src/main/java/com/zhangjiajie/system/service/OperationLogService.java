package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 分页查询操作日志
     */
    Page<OperationLog> pageList(Integer current, Integer size, String username,
                                 String operation, Integer status,
                                 LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID查询操作日志
     */
    List<OperationLog> getByUserId(Long userId, Integer limit);

    /**
     * 根据用户名查询操作日志
     */
    List<OperationLog> getByUsername(String username, Integer limit);

    /**
     * 清空日志（逻辑删除所有）
     */
    boolean cleanAll();

    /**
     * 删除指定日期之前的日志
     */
    int deleteBeforeDate(LocalDateTime beforeDate);
}
