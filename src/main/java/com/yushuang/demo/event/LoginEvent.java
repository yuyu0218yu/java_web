package com.yushuang.demo.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * 登录事件
 * 用于触发登录日志记录
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginEvent extends ApplicationEvent {

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 登录状态：true-成功，false-失败
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    public LoginEvent(Object source) {
        super(source);
    }

    public LoginEvent(Object source, String username, String ip, String userAgent, Boolean success, String errorMessage) {
        super(source);
        this.username = username;
        this.ip = ip;
        this.userAgent = userAgent;
        this.success = success;
        this.errorMessage = errorMessage;
    }
}