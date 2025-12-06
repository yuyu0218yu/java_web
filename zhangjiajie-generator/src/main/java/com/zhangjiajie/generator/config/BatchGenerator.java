package com.zhangjiajie.generator.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.zhangjiajie.generator.config.GeneratorHelper.toLowerCamelCase;
import static com.zhangjiajie.generator.config.GeneratorHelper.toKebabCase;

/**
 * 批量代码生成器
 * 一键生成完整模块的所有代码，整合所有生成器
 *
 * 支持的生成器：
 * - CrudTemplateGenerator：Controller/Service/ServiceImpl
 * - DtoConverterGenerator：DTO + Converter
 * - UnitTestGenerator：单元测试
 * - IntegrationTestGenerator：集成测试
 * - QueryGenerator：Query查询对象
 * - VoBoGenerator：VO/BO分层对象
 * - SqlScriptGenerator：SQL脚本
 * - ApiDocGenerator：API文档
 * - VueFrontendGenerator：Vue前端代码（API层、列表页、表单、详情、路由）
 *
 * 使用方法：
 *   // 方式一：生成完整模块
 *   BatchGenerator.generateFullModule("sys_product", "Product", "产品", "产品管理");
 *
 *   // 方式二：使用选项控制
 *   BatchGenerator.Options options = BatchGenerator.Options.full();
 *   BatchGenerator.generate("Product", "产品", "产品管理", options);
 *
 *   // 方式三：企业级生成（常用扩展）
 *   BatchGenerator.generateEnterprise("Product", "产品", "产品管理");
 *
 *   // 方式四：全栈生成（后端 + 前端）
 *   BatchGenerator.generateFullStack("Product", "产品", "产品管理");
 *
 *   // 方式五：仅生成前端代码
 *   BatchGenerator.generateFrontendOnly("Product", "产品", "产品管理");
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Slf4j
public class BatchGenerator {

    /**
     * 生成完整模块（所有代码）
     *
     * @param tableName    数据库表名（用于 MyBatis-Plus 生成器）
     * @param entityName   实体类名
     * @param entityCnName 实体中文名
     * @param moduleName   模块名称
     */
    public static void generateFullModule(String tableName, String entityName,
                                          String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.full());
        log.info("提示：如需从数据库表生成 Entity，请单独运行：");
        log.info("  MyBatisPlusCodeGenerator.generateCode(\"{}\");", tableName);
    }

    /**
     * 生成企业级模块（常用扩展）
     */
    public static void generateEnterprise(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.enterprise());
    }

    /**
     * 生成模块代码（使用选项控制）
     *
     * @param entityName   实体类名
     * @param entityCnName 实体中文名
     * @param moduleName   模块名称
     * @param options      生成选项
     */
    public static void generate(String entityName, String entityCnName,
                                String moduleName, Options options) {
        log.info("========================================");
        log.info("开始批量生成代码: {}", entityName);
        log.info("========================================");

        List<String> generatedFiles = new ArrayList<>();

        try {
            // ==================== 基础代码生成 ====================

            // 生成 Controller, Service, ServiceImpl
            if (options.isGenerateController() || options.isGenerateService()) {
                if (options.isGenerateController() && options.isGenerateService()) {
                    CrudTemplateGenerator.generate(entityName, entityCnName, moduleName);
                    generatedFiles.add(entityName + "Controller.java");
                    generatedFiles.add(entityName + "Service.java");
                    generatedFiles.add(entityName + "ServiceImpl.java");
                } else if (options.isGenerateController()) {
                    CrudTemplateGenerator.generateControllerOnly(entityName, entityCnName, moduleName);
                    generatedFiles.add(entityName + "Controller.java");
                } else {
                    CrudTemplateGenerator.generateServiceOnly(entityName, entityCnName);
                    generatedFiles.add(entityName + "Service.java");
                    generatedFiles.add(entityName + "ServiceImpl.java");
                }
            }

            // 生成 DTO 和 Converter
            if (options.isGenerateDto() || options.isGenerateConverter()) {
                if (options.isGenerateDto() && options.isGenerateConverter()) {
                    DtoConverterGenerator.generate(entityName, entityCnName);
                    generatedFiles.add(entityName + "Converter.java");
                    generatedFiles.add("Create" + entityName + "Request.java");
                    generatedFiles.add("Update" + entityName + "Request.java");
                    generatedFiles.add(entityName + "Response.java");
                } else if (options.isGenerateDto()) {
                    DtoConverterGenerator.generateDtosOnly(entityName, entityCnName);
                    generatedFiles.add("Create" + entityName + "Request.java");
                    generatedFiles.add("Update" + entityName + "Request.java");
                    generatedFiles.add(entityName + "Response.java");
                } else {
                    DtoConverterGenerator.generateConverterOnly(entityName, entityCnName);
                    generatedFiles.add(entityName + "Converter.java");
                }
            }

            // ==================== 测试代码生成 ====================

            // 生成单元测试
            if (options.isGenerateTest()) {
                UnitTestGenerator.generate(entityName, entityCnName);
                generatedFiles.add(entityName + "ControllerTest.java");
                generatedFiles.add(entityName + "ServiceTest.java");
            }

            // 生成集成测试
            if (options.isGenerateIntegrationTest()) {
                IntegrationTestGenerator.generate(entityName, entityCnName);
                generatedFiles.add(entityName + "ControllerIntegrationTest.java");
                generatedFiles.add(entityName + "ServiceIntegrationTest.java");
                generatedFiles.add(entityName + "SecurityTest.java");
                generatedFiles.add(entityName + "TransactionalTest.java");
            }

            // ==================== 扩展功能生成 ====================

            // 生成 Query 查询对象
            if (options.isGenerateQuery()) {
                QueryGenerator.generate(entityName, entityCnName);
                generatedFiles.add(entityName + "Query.java");
                generatedFiles.add(entityName + "QueryWrapper.java");
            }

            // 生成 VO/BO 分层对象
            if (options.isGenerateVoBo()) {
                VoBoGenerator.generate(entityName, entityCnName);
                generatedFiles.add(entityName + "BO.java");
                generatedFiles.add(entityName + "VO.java");
                generatedFiles.add(entityName + "DetailVO.java");
                generatedFiles.add(entityName + "ListVO.java");
                generatedFiles.add(entityName + "ExportVO.java");
                generatedFiles.add(entityName + "VoBoConverter.java");
            }

            // 生成 SQL 脚本
            if (options.isGenerateSql()) {
                SqlScriptGenerator.generatePermissionSql(entityName, entityCnName, moduleName);
                generatedFiles.add(entityName.toLowerCase() + "_permission.sql");
            }

            // 生成 API 文档
            if (options.isGenerateApiDoc()) {
                ApiDocGenerator.generate(entityName, entityCnName, moduleName);
                generatedFiles.add(entityName + "_API.md");
                generatedFiles.add(entityName + "_postman.json");
                generatedFiles.add(entityName + "_curl.sh");
                generatedFiles.add(entityName + "Api.ts");
            }

            // ==================== 前端代码生成 ====================

            // 生成 Vue 前端代码
            if (options.isGenerateFrontend()) {
                VueFrontendGenerator.generate(entityName, entityCnName, moduleName);
                generatedFiles.add(toLowerCamelCase(entityName) + "Api.js");
                generatedFiles.add(entityName + "List.vue");
                generatedFiles.add(entityName + "Form.vue");
                generatedFiles.add(entityName + "Detail.vue");
                generatedFiles.add("router-" + toKebabCase(entityName) + ".js");
            }

            // ==================== 打印汇总 ====================

            log.info("========================================");
            log.info("批量生成完成！");
            log.info("实体: {}", entityName);
            log.info("目标模块: {}", GeneratorHelper.getTargetModule());
            log.info("生成文件数: {}", generatedFiles.size());
            log.info("生成的文件:");
            for (String file : generatedFiles) {
                log.info("  - {}", file);
            }
            log.info("========================================");

        } catch (Exception e) {
            log.error("批量生成失败", e);
            throw new GeneratorException("批量生成失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量生成多个模块
     *
     * @param modules 模块配置列表
     */
    public static void generateBatch(List<ModuleConfig> modules) {
        generateBatch(modules, Options.full());
    }

    /**
     * 批量生成多个模块（使用选项控制）
     *
     * @param modules 模块配置列表
     * @param options 生成选项
     */
    public static void generateBatch(List<ModuleConfig> modules, Options options) {
        log.info("========================================");
        log.info("开始批量生成 {} 个模块", modules.size());
        log.info("========================================");

        int successCount = 0;
        int failCount = 0;

        for (ModuleConfig module : modules) {
            try {
                generate(module.getEntityName(), module.getEntityCnName(),
                        module.getModuleName(), options);
                successCount++;
            } catch (Exception e) {
                log.error("生成模块 {} 失败: {}", module.getEntityName(), e.getMessage());
                failCount++;
            }
        }

        log.info("========================================");
        log.info("批量生成完成！成功: {}, 失败: {}", successCount, failCount);
        log.info("========================================");
    }

    // ==================== 快捷方法 ====================

    /**
     * 快速生成（仅生成 Controller/Service/DTO）
     */
    public static void generateQuick(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.quick());
    }

    /**
     * 仅生成业务层代码（Controller/Service）
     */
    public static void generateBusinessLayer(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.businessOnly());
    }

    /**
     * 生成分层架构代码（DTO + VO/BO + Query）
     */
    public static void generateLayered(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.layered());
    }

    /**
     * 仅生成测试代码
     */
    public static void generateTestOnly(String entityName, String entityCnName) {
        generate(entityName, entityCnName, "", Options.testOnly());
    }

    /**
     * 仅生成文档和SQL
     */
    public static void generateDocsOnly(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.docsOnly());
    }

    /**
     * 全栈生成（后端 + 前端）
     */
    public static void generateFullStack(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.fullStack());
    }

    /**
     * 仅生成前端代码
     */
    public static void generateFrontendOnly(String entityName, String entityCnName, String moduleName) {
        generate(entityName, entityCnName, moduleName, Options.frontendOnly());
    }

    // ==================== 内部类 ====================

    /**
     * 生成选项
     */
    @Data
    public static class Options {
        private boolean generateEntity = false;  // Entity 建议使用 MyBatisPlusCodeGenerator
        private boolean generateMapper = false;  // Mapper 建议使用 MyBatisPlusCodeGenerator
        private boolean generateService = true;
        private boolean generateController = true;
        private boolean generateDto = true;
        private boolean generateConverter = true;
        private boolean generateTest = true;
        private boolean generateIntegrationTest = false;
        private boolean generateQuery = false;
        private boolean generateVoBo = false;
        private boolean generateSql = false;
        private boolean generateApiDoc = false;
        private boolean generateFrontend = false;
        private boolean overwrite = true;

        /**
         * 生成所有代码（完整版）
         */
        public static Options full() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
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
         * 企业级选项（常用扩展）
         */
        public static Options enterprise() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
            options.setGenerateDto(true);
            options.setGenerateConverter(true);
            options.setGenerateTest(true);
            options.setGenerateQuery(true);
            options.setGenerateVoBo(true);
            return options;
        }

        /**
         * 快速生成（Controller/Service/DTO）
         */
        public static Options quick() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
            options.setGenerateDto(true);
            options.setGenerateConverter(false);
            options.setGenerateTest(false);
            return options;
        }

        /**
         * 仅业务层（Controller/Service）
         */
        public static Options businessOnly() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
            options.setGenerateDto(false);
            options.setGenerateConverter(false);
            options.setGenerateTest(false);
            return options;
        }

        /**
         * 分层架构（DTO + VO/BO + Query）
         */
        public static Options layered() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
            options.setGenerateDto(true);
            options.setGenerateConverter(true);
            options.setGenerateQuery(true);
            options.setGenerateVoBo(true);
            options.setGenerateTest(false);
            return options;
        }

        /**
         * 仅 DTO 和 Converter
         */
        public static Options dtoOnly() {
            Options options = new Options();
            options.setGenerateService(false);
            options.setGenerateController(false);
            options.setGenerateDto(true);
            options.setGenerateConverter(true);
            options.setGenerateTest(false);
            return options;
        }

        /**
         * 仅测试代码
         */
        public static Options testOnly() {
            Options options = new Options();
            options.setGenerateService(false);
            options.setGenerateController(false);
            options.setGenerateDto(false);
            options.setGenerateConverter(false);
            options.setGenerateTest(true);
            options.setGenerateIntegrationTest(true);
            return options;
        }

        /**
         * 仅文档和SQL
         */
        public static Options docsOnly() {
            Options options = new Options();
            options.setGenerateService(false);
            options.setGenerateController(false);
            options.setGenerateDto(false);
            options.setGenerateConverter(false);
            options.setGenerateTest(false);
            options.setGenerateSql(true);
            options.setGenerateApiDoc(true);
            return options;
        }

        /**
         * 仅Query和VO/BO
         */
        public static Options queryVoBoOnly() {
            Options options = new Options();
            options.setGenerateService(false);
            options.setGenerateController(false);
            options.setGenerateDto(false);
            options.setGenerateConverter(false);
            options.setGenerateTest(false);
            options.setGenerateQuery(true);
            options.setGenerateVoBo(true);
            return options;
        }

        /**
         * 仅前端代码
         */
        public static Options frontendOnly() {
            Options options = new Options();
            options.setGenerateService(false);
            options.setGenerateController(false);
            options.setGenerateDto(false);
            options.setGenerateConverter(false);
            options.setGenerateTest(false);
            options.setGenerateFrontend(true);
            return options;
        }

        /**
         * 全栈选项（后端 + 前端）
         */
        public static Options fullStack() {
            Options options = enterprise();
            options.setGenerateFrontend(true);
            return options;
        }

        public static OptionsBuilder builder() {
            return new OptionsBuilder();
        }

        public static class OptionsBuilder {
            private final Options options = new Options();

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

            public OptionsBuilder overwrite(boolean value) {
                options.setOverwrite(value);
                return this;
            }

            public Options build() {
                return options;
            }
        }
    }

    /**
     * 模块配置
     */
    @Data
    public static class ModuleConfig {
        private String tableName;
        private String entityName;
        private String entityCnName;
        private String moduleName;

        public ModuleConfig() {
        }

        public ModuleConfig(String tableName, String entityName, String entityCnName, String moduleName) {
            this.tableName = tableName;
            this.entityName = entityName;
            this.entityCnName = entityCnName;
            this.moduleName = moduleName;
        }

        /**
         * 不使用表名的构造方法
         */
        public static ModuleConfig of(String entityName, String entityCnName, String moduleName) {
            return new ModuleConfig(null, entityName, entityCnName, moduleName);
        }
    }

    // ==================== Main 方法示例 ====================

    public static void main(String[] args) {
        log.info("=================================================");
        log.info("BatchGenerator - 批量代码生成器 (增强版)");
        log.info("=================================================");
        log.info("");
        log.info("支持的生成器：");
        log.info("  - CrudTemplateGenerator：Controller/Service/ServiceImpl");
        log.info("  - DtoConverterGenerator：DTO + Converter");
        log.info("  - UnitTestGenerator：单元测试");
        log.info("  - IntegrationTestGenerator：集成测试");
        log.info("  - QueryGenerator：Query查询对象");
        log.info("  - VoBoGenerator：VO/BO分层对象");
        log.info("  - SqlScriptGenerator：SQL脚本");
        log.info("  - ApiDocGenerator：API文档");
        log.info("  - VueFrontendGenerator：Vue前端代码");
        log.info("");
        log.info("使用方式：");
        log.info("");
        log.info("  1. 生成完整模块（所有功能）:");
        log.info("     BatchGenerator.generateFullModule(\"sys_product\", \"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  2. 企业级生成（常用扩展）:");
        log.info("     BatchGenerator.generateEnterprise(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  3. 快速生成（Controller/Service/DTO）:");
        log.info("     BatchGenerator.generateQuick(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  4. 分层架构（DTO + VO/BO + Query）:");
        log.info("     BatchGenerator.generateLayered(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  5. 仅生成测试代码:");
        log.info("     BatchGenerator.generateTestOnly(\"Product\", \"产品\");");
        log.info("");
        log.info("  6. 仅生成文档和SQL:");
        log.info("     BatchGenerator.generateDocsOnly(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  7. 全栈生成（后端 + 前端）:");
        log.info("     BatchGenerator.generateFullStack(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  8. 仅生成前端代码:");
        log.info("     BatchGenerator.generateFrontendOnly(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  9. 自定义选项:");
        log.info("     BatchGenerator.Options options = BatchGenerator.Options.builder()");
        log.info("         .generateController(true)");
        log.info("         .generateService(true)");
        log.info("         .generateQuery(true)");
        log.info("         .generateVoBo(true)");
        log.info("         .generateFrontend(true)");
        log.info("         .build();");
        log.info("     BatchGenerator.generate(\"Product\", \"产品\", \"产品管理\", options);");
        log.info("");
        log.info("  10. 批量生成多个模块:");
        log.info("     List<ModuleConfig> modules = Arrays.asList(");
        log.info("         ModuleConfig.of(\"Product\", \"产品\", \"产品管理\"),");
        log.info("         ModuleConfig.of(\"Category\", \"分类\", \"分类管理\"),");
        log.info("         ModuleConfig.of(\"Order\", \"订单\", \"订单管理\")");
        log.info("     );");
        log.info("     BatchGenerator.generateBatch(modules, BatchGenerator.Options.fullStack());");
        log.info("=================================================");
    }
}
