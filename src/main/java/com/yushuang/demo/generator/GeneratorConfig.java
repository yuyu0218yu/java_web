package com.yushuang.demo.generator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成器配置类
 * 支持从配置文件读取，也支持 Builder 模式构建
 *
 * @author yushuang
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfig {

    /** 基础包名 */
    private String basePackage = "com.yushuang.demo";

    /** 作者 */
    private String author = "yushuang";

    /** 输出路径 */
    private String outputPath = System.getProperty("user.dir") + "/src/main/java";

    /** Mapper XML 输出路径 */
    private String mapperXmlPath = System.getProperty("user.dir") + "/src/main/resources/mapper";

    /** 是否覆盖已有文件 */
    private boolean overwrite = true;

    /** 是否启用 Swagger */
    private boolean enableSwagger = true;

    /** 是否启用 Lombok */
    private boolean enableLombok = true;

    /** 表前缀（生成时移除） */
    private String tablePrefix = "";

    /** 数据库类型 */
    private String dbType = "mysql";

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

        public GeneratorConfigBuilder outputPath(String outputPath) {
            config.setOutputPath(outputPath);
            return this;
        }

        public GeneratorConfigBuilder mapperXmlPath(String mapperXmlPath) {
            config.setMapperXmlPath(mapperXmlPath);
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

        public GeneratorConfig build() {
            return config;
        }
    }
}
