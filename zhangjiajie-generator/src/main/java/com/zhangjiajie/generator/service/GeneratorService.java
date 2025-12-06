package com.zhangjiajie.generator.service;

import com.zhangjiajie.generator.dto.GenerateOptions;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器服务接口
 */
public interface GeneratorService {

    /**
     * 获取所有数据库表列表
     */
    List<Map<String, Object>> getTableList();

    /**
     * 获取表的列信息
     */
    List<Map<String, Object>> getTableColumns(String tableName);

    /**
     * 预览生成的代码
     */
    Map<String, String> previewCode(String tableName, GenerateOptions options);

    /**
     * 执行代码生成
     */
    void generateCode(String tableName, GenerateOptions options);
}
