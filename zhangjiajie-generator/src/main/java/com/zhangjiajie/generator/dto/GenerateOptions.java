package com.zhangjiajie.generator.dto;

import lombok.Data;

/**
 * 代码生成选项
 * 控制生成器生成哪些类型的代码
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
public class GenerateOptions {

    // ==================== 基础配置 ====================

    /** 模块名称（子包名） */
    private String moduleName;

    /** 实体中文名 */
    private String entityCnName;

    /** API模块名称（Swagger Tag） */
    private String apiModuleName;

    /** 作者 */
    private String author;

    /** 要移除的表前缀 */
    private String tablePrefix = "";

    /** 是否覆盖已有文件 */
    private Boolean overwrite = true;

    /** 启用 Swagger 注解 */
    private Boolean enableSwagger = true;

    /** 启用 Lombok */
    private Boolean enableLombok = true;

    // ==================== 基础代码生成 ====================

    /** 生成 Entity */
    private Boolean generateEntity = true;

    /** 生成 Mapper */
    private Boolean generateMapper = true;

    /** 生成 Service */
    private Boolean generateService = true;

    /** 生成 Controller */
    private Boolean generateController = true;

    // ==================== DTO/Converter 生成 ====================

    /** 生成 DTO（Request/Response） */
    private Boolean generateDto = false;

    /** 生成 Converter */
    private Boolean generateConverter = false;

    // ==================== 测试代码生成 ====================

    /** 生成单元测试 */
    private Boolean generateTest = false;

    /** 生成集成测试 */
    private Boolean generateIntegrationTest = false;

    // ==================== 扩展功能生成 ====================

    /** 生成 Query 查询对象 */
    private Boolean generateQuery = false;

    /** 生成 VO/BO 分层对象 */
    private Boolean generateVoBo = false;

    /** 生成 SQL 脚本（建表、权限、初始化） */
    private Boolean generateSql = false;

    /** 生成 API 文档（Markdown、Postman） */
    private Boolean generateApiDoc = false;

    /** 生成 Vue 前端代码（API层、列表页、表单、详情、路由） */
    private Boolean generateFrontend = false;

    // ==================== 工厂方法 ====================

    /**
     * 创建默认选项（基础 CRUD）
     */
    public static GenerateOptions defaults() {
        return new GenerateOptions();
    }

    /**
     * 创建完整选项（包含所有扩展功能）
     */
    public static GenerateOptions full() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateDto(true);
        options.setGenerateConverter(true);
        options.setGenerateTest(true);
        options.setGenerateIntegrationTest(true);
        options.setGenerateQuery(true);
        options.setGenerateVoBo(true);
        options.setGenerateSql(true);
        options.setGenerateApiDoc(true);
        options.setGenerateFrontend(true);
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

    /**
     * 创建企业级选项（包含常用扩展）
     */
    public static GenerateOptions enterprise() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateDto(true);
        options.setGenerateConverter(true);
        options.setGenerateTest(true);
        options.setGenerateQuery(true);
        options.setGenerateVoBo(true);
        return options;
    }

    /**
     * 创建文档选项（仅生成文档和SQL）
     */
    public static GenerateOptions docsOnly() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateEntity(false);
        options.setGenerateMapper(false);
        options.setGenerateService(false);
        options.setGenerateController(false);
        options.setGenerateSql(true);
        options.setGenerateApiDoc(true);
        return options;
    }

    /**
     * 创建测试选项（仅生成测试代码）
     */
    public static GenerateOptions testOnly() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateEntity(false);
        options.setGenerateMapper(false);
        options.setGenerateService(false);
        options.setGenerateController(false);
        options.setGenerateTest(true);
        options.setGenerateIntegrationTest(true);
        return options;
    }

    /**
     * 创建分层架构选项（DTO + VO/BO + Query）
     */
    public static GenerateOptions layered() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateDto(true);
        options.setGenerateConverter(true);
        options.setGenerateQuery(true);
        options.setGenerateVoBo(true);
        return options;
    }

    // ==================== Builder 模式 ====================

    public static OptionsBuilder builder() {
        return new OptionsBuilder();
    }

    public static class OptionsBuilder {
        private final GenerateOptions options = new GenerateOptions();

        public OptionsBuilder moduleName(String value) {
            options.setModuleName(value);
            return this;
        }

        public OptionsBuilder entityCnName(String value) {
            options.setEntityCnName(value);
            return this;
        }

        public OptionsBuilder apiModuleName(String value) {
            options.setApiModuleName(value);
            return this;
        }

        public OptionsBuilder author(String value) {
            options.setAuthor(value);
            return this;
        }

        public OptionsBuilder tablePrefix(String value) {
            options.setTablePrefix(value);
            return this;
        }

        public OptionsBuilder overwrite(boolean value) {
            options.setOverwrite(value);
            return this;
        }

        public OptionsBuilder generateEntity(boolean value) {
            options.setGenerateEntity(value);
            return this;
        }

        public OptionsBuilder generateMapper(boolean value) {
            options.setGenerateMapper(value);
            return this;
        }

        public OptionsBuilder generateService(boolean value) {
            options.setGenerateService(value);
            return this;
        }

        public OptionsBuilder generateController(boolean value) {
            options.setGenerateController(value);
            return this;
        }

        public OptionsBuilder generateDto(boolean value) {
            options.setGenerateDto(value);
            return this;
        }

        public OptionsBuilder generateConverter(boolean value) {
            options.setGenerateConverter(value);
            return this;
        }

        public OptionsBuilder generateTest(boolean value) {
            options.setGenerateTest(value);
            return this;
        }

        public OptionsBuilder generateIntegrationTest(boolean value) {
            options.setGenerateIntegrationTest(value);
            return this;
        }

        public OptionsBuilder generateQuery(boolean value) {
            options.setGenerateQuery(value);
            return this;
        }

        public OptionsBuilder generateVoBo(boolean value) {
            options.setGenerateVoBo(value);
            return this;
        }

        public OptionsBuilder generateSql(boolean value) {
            options.setGenerateSql(value);
            return this;
        }

        public OptionsBuilder generateApiDoc(boolean value) {
            options.setGenerateApiDoc(value);
            return this;
        }

        public OptionsBuilder generateFrontend(boolean value) {
            options.setGenerateFrontend(value);
            return this;
        }

        public GenerateOptions build() {
            return options;
        }
    }

    /**
     * 创建前端选项（仅生成前端代码）
     */
    public static GenerateOptions frontendOnly() {
        GenerateOptions options = new GenerateOptions();
        options.setGenerateEntity(false);
        options.setGenerateMapper(false);
        options.setGenerateService(false);
        options.setGenerateController(false);
        options.setGenerateFrontend(true);
        return options;
    }

    /**
     * 创建全栈选项（后端 + 前端）
     */
    public static GenerateOptions fullStack() {
        GenerateOptions options = full();
        options.setGenerateFrontend(true);
        return options;
    }
}
