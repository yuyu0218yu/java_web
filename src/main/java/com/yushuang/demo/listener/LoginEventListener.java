package com.yushuang.demo.listener;

import com.yushuang.demo.entity.LoginLog;
import com.yushuang.demo.event.LoginEvent;
import com.yushuang.demo.mapper.LoginLogMapper;
import com.yushuang.demo.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 登录事件监听器
 * 异步记录登录日志
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginEventListener {

    private final LoginLogMapper loginLogMapper;

    /**
     * 监听登录事件
     */
    @EventListener
    @Async("logExecutor")
    public void handleLoginEvent(LoginEvent event) {
        try {
            LoginLog loginLog = new LoginLog();

            // 基本信息
            loginLog.setUsername(event.getUsername());
            loginLog.setIp(event.getIp());
            loginLog.setUserAgent(event.getUserAgent());

            // 解析用户代理信息
            UserAgentUtil.UserAgentInfo userAgentInfo = UserAgentUtil.parseUserAgent(event.getUserAgent());
            if (userAgentInfo != null) {
                loginLog.setBrowser(userAgentInfo.getBrowser());
                loginLog.setOs(userAgentInfo.getOperatingSystem());
                loginLog.setLocation(userAgentInfo.getLocation());
            }

            // 登录状态
            if (event.getSuccess() != null && event.getSuccess()) {
                loginLog.setStatus(LoginLog.Status.SUCCESS.getCode());
                // 成功登录时不设置错误信息
            } else {
                loginLog.setStatus(LoginLog.Status.FAILURE.getCode());
                loginLog.setErrorMsg(event.getErrorMessage());
            }

            // 时间戳
            loginLog.setCreateTime(LocalDateTime.now());
            loginLog.setUpdateTime(LocalDateTime.now());

            // 保存日志
            loginLogMapper.insert(loginLog);

            log.debug("登录日志记录成功: {} - {} - {}",
                     event.getUsername(),
                     event.getIp(),
                     event.getSuccess() ? "成功" : "失败");

        } catch (Exception e) {
            log.error("保存登录日志失败: {}", e.getMessage(), e);
        }
    }
}