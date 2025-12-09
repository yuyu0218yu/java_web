package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.LoginLog;
import com.zhangjiajie.system.mapper.LoginLogMapper;
import com.zhangjiajie.system.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 登录日志服务实现
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public Page<LoginLog> pageList(Integer current, Integer size, String username,
                                   String ip, Integer status,
                                   LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), LoginLog::getUsername, username)
               .like(StringUtils.hasText(ip), LoginLog::getIp, ip)
               .eq(status != null, LoginLog::getStatus, status)
               .ge(startTime != null, LoginLog::getCreateTime, startTime)
               .le(endTime != null, LoginLog::getCreateTime, endTime)
               .orderByDesc(LoginLog::getCreateTime);

        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public boolean cleanAll() {
        LambdaUpdateWrapper<LoginLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(LoginLog::getDeleted, 1)
               .eq(LoginLog::getDeleted, 0);
        return update(wrapper);
    }

    @Override
    public int deleteBeforeDate(LocalDateTime beforeDate) {
        return baseMapper.deleteLogsBeforeDate(beforeDate);
    }

    @Override
    public void recordLoginLog(String username, String ip, String userAgent, boolean success, String errorMsg) {
        LoginLog log = new LoginLog();
        log.setUsername(username);
        log.setIp(ip);
        log.setUserAgent(userAgent);
        log.setStatus(success ? 1 : 0);
        log.setErrorMsg(errorMsg);

        // 解析浏览器和操作系统信息
        parseUserAgent(log, userAgent);

        save(log);
    }

    /**
     * 解析User-Agent获取浏览器和操作系统信息
     */
    private void parseUserAgent(LoginLog log, String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            log.setBrowser("Unknown Browser");
            log.setOs("Unknown OS");
            log.setLocation("Unknown Location");
            return;
        }

        // 简单解析浏览器
        if (userAgent.contains("Chrome")) {
            log.setBrowser("Google Chrome");
        } else if (userAgent.contains("Firefox")) {
            log.setBrowser("Firefox");
        } else if (userAgent.contains("Safari")) {
            log.setBrowser("Safari");
        } else if (userAgent.contains("Edge")) {
            log.setBrowser("Microsoft Edge");
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            log.setBrowser("Internet Explorer");
        } else {
            log.setBrowser("Unknown Browser");
        }

        // 简单解析操作系统
        if (userAgent.contains("Windows NT 10")) {
            log.setOs("Windows 10");
        } else if (userAgent.contains("Windows NT 6.1")) {
            log.setOs("Windows 7");
        } else if (userAgent.contains("Mac OS X")) {
            log.setOs("Mac OS X");
        } else if (userAgent.contains("Linux")) {
            log.setOs("Linux");
        } else if (userAgent.contains("Android")) {
            log.setOs("Android");
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            log.setOs("iOS");
        } else {
            log.setOs("Unknown OS");
        }

        // 位置信息需要通过IP地理位置服务获取，这里暂时设置为未知
        log.setLocation("Unknown Location");
    }
}
