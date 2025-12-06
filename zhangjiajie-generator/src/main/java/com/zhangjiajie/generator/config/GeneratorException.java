package com.zhangjiajie.generator.config;

import com.zhangjiajie.common.exception.BusinessException;

/**
 * 代码生成器异常
 * 继承自 BusinessException，统一处理机制
 *
 * @author yushuang
 */
public class GeneratorException extends BusinessException {

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 表不存在
     */
    public static GeneratorException tableNotFound(String tableName) {
        return new GeneratorException("表不存在: " + tableName);
    }

    /**
     * 文件写入失败
     */
    public static GeneratorException writeFileFailed(String filePath, Throwable cause) {
        return new GeneratorException("文件写入失败: " + filePath, cause);
    }

    /**
     * 模板渲染失败
     */
    public static GeneratorException templateRenderFailed(String templateName, Throwable cause) {
        return new GeneratorException("模板渲染失败: " + templateName, cause);
    }

    /**
     * 配置无效
     */
    public static GeneratorException invalidConfig(String message) {
        return new GeneratorException("配置无效: " + message);
    }
}
