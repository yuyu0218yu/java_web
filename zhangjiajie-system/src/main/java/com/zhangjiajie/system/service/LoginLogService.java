package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.LoginLog;

import java.time.LocalDateTime;

/**
 * 登录日志服务接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 分页查询登录日志
     */
    Page<LoginLog> pageList(Integer current, Integer size, String username,
                            String ip, Integer status,
                            LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 清空日志（逻辑删除所有）
     */
    boolean cleanAll();

    /**
     * 删除指定日期之前的日志
     */
    int deleteBeforeDate(LocalDateTime beforeDate);

    /**
     * 记录登录日志
     */
    void recordLoginLog(String username, String ip, String userAgent, boolean success, String errorMsg);
}
