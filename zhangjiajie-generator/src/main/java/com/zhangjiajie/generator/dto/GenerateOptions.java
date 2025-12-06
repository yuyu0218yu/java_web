package com.zhangjiajie.generator.dto;

import lombok.Data;

/**
 * 代码生成选项
 */
@Data
public class GenerateOptions {

    /** 模块名称（子包名） */
    private String moduleName;

    /** 实体中文名 */
    private String entityCnName;

    /** API模块名称（Swagger Tag） */
    private String apiModuleName;

    /** 作者 */
    private String author;

    /** 生成 Entity */
    private Boolean generateEntity = true;

    /** 生成 Mapper */
    private Boolean generateMapper = true;

    /** 生成 Service */
    private Boolean generateService = true;

    /** 生成 Controller */
    private Boolean generateController = true;

    /** 生成 DTO（Request/Response） */
    private Boolean generateDto = false;

    /** 生成 Converter */
    private Boolean generateConverter = false;

    /** 生成单元测试 */
    private Boolean generateTest = false;

    /** 启用 Swagger 注解 */
    private Boolean enableSwagger = true;

    /** 启用 Lombok */
    private Boolean enableLombok = true;

    /** 要移除的表前缀 */
    private String tablePrefix = "";

    /** 是否覆盖已有文件 */
    private Boolean overwrite = true;

    /**
     * 创建默认选项（基础 CRUD）
     */
    public static GenerateOptions defaults() {
        return new GenerateOptions();
    }

    /**
     * 创建完整选项（包含 DTO、Converter、Test）
     */
    public static GenerateOptions full() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateDto(true);
        options.setGenerateConverter(true);
        options.setGenerateTest(true);
        return options;
    }

    /**
     * 创建快速选项（仅 Controller/Service）
     */
    public static GenerateOptions quick() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateEntity(false);
        options.setGenerateMapper(false);
        return options;
    }
}
