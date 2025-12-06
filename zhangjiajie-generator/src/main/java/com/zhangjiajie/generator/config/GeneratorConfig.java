package com.zhangjiajie.generator.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 代码生成器配置类
 * 支持从配置文件读取，也支持 Builder 模式构建
 *
 * 配置示例（application.yml）：
 * <pre>
 * generator:
 *   base-package: com.zhangjiajie.system
 *   author: zhangjiajie
 *   target-module: zhangjiajie-system
 *   overwrite: true
 *   enable-swagger: true
 *   enable-lombok: true
 *   table-prefix: sys_
 *   table-prefixes:
 *     - sys_
 *     - t_
 * </pre>
 *
 * @author yushuang
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfig {

    /** 基础包名 */
    private String basePackage = "com.zhangjiajie.system";

    /** 作者 */
    private String author = "zhangjiajie";

    /** 目标模块名称（生成代码到哪个模块） */
    private String targetModule = "zhangjiajie-system";

    /** 输出路径（Java源码） */
    private String outputPath;

    /** Mapper XML 输出路径 */
    private String mapperXmlPath;

    /** 测试代码输出路径 */
    private String testOutputPath;

    /** 是否覆盖已有文件 */
    private boolean overwrite = true;

    /** 是否启用 Swagger */
    private boolean enableSwagger = true;

    /** 是否启用 Lombok */
    private boolean enableLombok = true;

    /** 表前缀（生成时移除），单个前缀 */
    private String tablePrefix = "";

    /** 表前缀列表（生成时移除），多个前缀 */
    private List<String> tablePrefixes = Arrays.asList("sys_", "t_");

    /** 数据库类型 */
    private String dbType = "mysql";

    /** 是否生成 DTO */
    private boolean generateDto = true;

    /** 是否生成单元测试 */
    private boolean generateTest = true;

    /** 是否生成 Converter */
    private boolean generateConverter = true;

    /**
     * 初始化：根据 targetModule 计算默认路径
     */
    @PostConstruct
    public void init() {
        String baseDir = System.getProperty("user.dir");

        if (outputPath == null || outputPath.isEmpty()) {
            outputPath = baseDir + "/" + targetModule + "/src/main/java";
        }

        if (mapperXmlPath == null || mapperXmlPath.isEmpty()) {
            mapperXmlPath = baseDir + "/" + targetModule + "/src/main/resources/mapper";
        }

        if (testOutputPath == null || testOutputPath.isEmpty()) {
            testOutputPath = baseDir + "/" + targetModule + "/src/test/java";
        }

        // 同步配置到 GeneratorHelper
        GeneratorHelper.configure(this);
    }

    /**
     * 获取表前缀数组（合并单个前缀和前缀列表）
     */
    public String[] getTablePrefixArray() {
        if (tablePrefix != null && !tablePrefix.isEmpty()) {
            if (tablePrefixes == null || tablePrefixes.isEmpty()) {
                return new String[]{tablePrefix};
            }
            // 合并
            List<String> merged = new java.util.ArrayList<>(tablePrefixes);
            if (!merged.contains(tablePrefix)) {
                merged.add(0, tablePrefix);
            }
            return merged.toArray(new String[0]);
        }
        if (tablePrefixes != null && !tablePrefixes.isEmpty()) {
            return tablePrefixes.toArray(new String[0]);
        }
        return new String[0];
    }

    /**
     * 获取 Java 源码完整路径（包含包路径）
     */
    public String getFullJavaPath() {
        return outputPath + "/" + basePackage.replace(".", "/");
    }

    /**
     * 获取测试源码完整路径（包含包路径）
     */
    public String getFullTestPath() {
        return testOutputPath + "/" + basePackage.replace(".", "/");
    }

    /**
     * Builder 模式构建配置
     */
    public static GeneratorConfigBuilder builder() {
        return new GeneratorConfigBuilder();
    }

    /**
     * 配置构建器
     */
    public static class GeneratorConfigBuilder {
        private final GeneratorConfig config = new GeneratorConfig();

        public GeneratorConfigBuilder basePackage(String basePackage) {
            config.setBasePackage(basePackage);
            return this;
        }

        public GeneratorConfigBuilder author(String author) {
            config.setAuthor(author);
            return this;
        }

        public GeneratorConfigBuilder targetModule(String targetModule) {
            config.setTargetModule(targetModule);
            return this;
        }

        public GeneratorConfigBuilder outputPath(String outputPath) {
            config.setOutputPath(outputPath);
            return this;
        }

        public GeneratorConfigBuilder mapperXmlPath(String mapperXmlPath) {
            config.setMapperXmlPath(mapperXmlPath);
            return this;
        }

        public GeneratorConfigBuilder testOutputPath(String testOutputPath) {
            config.setTestOutputPath(testOutputPath);
            return this;
        }

        public GeneratorConfigBuilder overwrite(boolean overwrite) {
            config.setOverwrite(overwrite);
            return this;
        }

        public GeneratorConfigBuilder enableSwagger(boolean enableSwagger) {
            config.setEnableSwagger(enableSwagger);
            return this;
        }

        public GeneratorConfigBuilder enableLombok(boolean enableLombok) {
            config.setEnableLombok(enableLombok);
            return this;
        }

        public GeneratorConfigBuilder tablePrefix(String tablePrefix) {
            config.setTablePrefix(tablePrefix);
            return this;
        }

        public GeneratorConfigBuilder tablePrefixes(List<String> tablePrefixes) {
            config.setTablePrefixes(tablePrefixes);
            return this;
        }

        public GeneratorConfigBuilder generateDto(boolean generateDto) {
            config.setGenerateDto(generateDto);
            return this;
        }

        public GeneratorConfigBuilder generateTest(boolean generateTest) {
            config.setGenerateTest(generateTest);
            return this;
        }

        public GeneratorConfigBuilder generateConverter(boolean generateConverter) {
            config.setGenerateConverter(generateConverter);
            return this;
        }

        public GeneratorConfig build() {
            config.init();
            return config;
        }
    }
}
