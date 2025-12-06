package com.zhangjiajie.common.exception;

/**
 * 资源不存在异常
 * 用于找不到指定资源时抛出
 *
 * @author yushuang
 * @since 2025-12-05
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(resourceType + "不存在: " + id);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
