package com.yushuang.demo.dto;

import lombok.Data;

/**
 * 代码生成请求参数
 */
@Data
public class GenerateRequest {
    
    /** 表名 */
    private String tableName;
    
    /** 生成选项 */
    private GenerateOptions options;
}
