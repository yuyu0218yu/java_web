package com.zhangjiajie.generator.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量代码生成器
 * 一键生成完整模块的所有代码
 *
 * 使用方法：
 * 方式一：生成完整模块
 *   BatchGenerator.generateFullModule("sys_product", "Product", "产品", "产品管理");
 *
 * 方式二：使用选项控制生成内容
 *   BatchGenerator.Options options = BatchGenerator.Options.builder()
 *       .generateEntity(true)
 *       .generateMapper(true)
 *       .generateService(true)
 *       .generateController(true)
 *       .generateDto(true)
 *       .generateTest(false)
 *       .build();
 *   BatchGenerator.generate("Product", "产品", "产品管理", options);
 *
 * 方式三：批量生成多个模块
 *   List<BatchGenerator.ModuleConfig> modules = Arrays.asList(
 *       new ModuleConfig("sys_product", "Product", "产品", "产品管理"),
 *       new ModuleConfig("sys_category", "Category", "分类", "分类管理")
 *   );
 *   BatchGenerator.generateBatch(modules);
 *
 * @author yushuang
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
        generate(entityName, entityCnName, moduleName, Options.all());
        log.info("提示：如需从数据库表生成 Entity，请单独运行：");
        log.info("  MyBatisPlusCodeGenerator.generateCode(\"{}\");", tableName);
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

            // 生成单元测试
            if (options.isGenerateTest()) {
                UnitTestGenerator.generate(entityName, entityCnName);
                generatedFiles.add(entityName + "ControllerTest.java");
                generatedFiles.add(entityName + "ServiceTest.java");
            }

            // 打印汇总
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
        generateBatch(modules, Options.all());
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
        private boolean overwrite = true;

        /**
         * 生成所有代码（不含 Entity/Mapper）
         */
        public static Options all() {
            Options options = new Options();
            options.setGenerateService(true);
            options.setGenerateController(true);
            options.setGenerateDto(true);
            options.setGenerateConverter(true);
            options.setGenerateTest(true);
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
        // 示例1：生成完整模块
        // generateFullModule("sys_product", "Product", "产品", "产品管理");

        // 示例2：快速生成
        // generateQuick("Product", "产品", "产品管理");

        // 示例3：批量生成
        // List<ModuleConfig> modules = Arrays.asList(
        //     ModuleConfig.of("Product", "产品", "产品管理"),
        //     ModuleConfig.of("Category", "分类", "分类管理"),
        //     ModuleConfig.of("Order", "订单", "订单管理")
        // );
        // generateBatch(modules);

        log.info("=================================================");
        log.info("BatchGenerator - 批量代码生成器");
        log.info("=================================================");
        log.info("使用方式:");
        log.info("  1. 生成完整模块:");
        log.info("     BatchGenerator.generateFullModule(\"sys_product\", \"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  2. 快速生成（Controller/Service/DTO）:");
        log.info("     BatchGenerator.generateQuick(\"Product\", \"产品\", \"产品管理\");");
        log.info("");
        log.info("  3. 批量生成多个模块:");
        log.info("     List<ModuleConfig> modules = Arrays.asList(");
        log.info("         ModuleConfig.of(\"Product\", \"产品\", \"产品管理\"),");
        log.info("         ModuleConfig.of(\"Category\", \"分类\", \"分类管理\")");
        log.info("     );");
        log.info("     BatchGenerator.generateBatch(modules);");
        log.info("=================================================");
    }
}
