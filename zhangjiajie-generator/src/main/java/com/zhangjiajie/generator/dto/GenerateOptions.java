package com.zhangjiajie.generator.dto;

import lombok.Data;

/**
 * 代码生成选项
 */
@Data
public class GenerateOptions {
    
    /** 模块名称 */
    private String moduleName;
    
    /** 作者 */
    private String author = "yushuang";
    
    /** 生成 Entity */
    private Boolean generateEntity = true;
    
    /** 生成 Mapper */
    private Boolean generateMapper = true;
    
    /** 生成 Service */
    private Boolean generateService = true;
    
    /** 生成 Controller */
    private Boolean generateController = true;
    
    /** 启用 Swagger 注解 */
    private Boolean enableSwagger = true;
    
    /** 启用 Lombok */
    private Boolean enableLombok = true;
    
    /** 要移除的表前缀 */
    private String tablePrefix = "";
}
