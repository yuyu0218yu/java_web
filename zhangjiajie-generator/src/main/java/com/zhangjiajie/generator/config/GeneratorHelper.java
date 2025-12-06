package com.zhangjiajie.generator.config;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 代码生成器共享工具类
 * 提供字符串转换、文件写入等通用方法
 *
 * 支持两种使用方式：
 * 1. Spring 环境：通过 GeneratorConfig 注入配置
 * 2. 独立运行：使用静态配置或默认值
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Slf4j
public final class GeneratorHelper {

    // ==================== 默认配置（可通过 configure 方法覆盖） ====================

    private static String basePackage = "com.zhangjiajie.system";
    private static String author = "zhangjiajie";
    private static String targetModule = "zhangjiajie-system";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private GeneratorHelper() {
        // 工具类不应被实例化
    }

    // ==================== 配置方法 ====================

    /**
     * 从 GeneratorConfig 初始化配置
     * 在 Spring 环境下使用
     */
    public static void configure(GeneratorConfig config) {
        if (config != null) {
            basePackage = config.getBasePackage();
            author = config.getAuthor();
            // 从 outputPath 推断目标模块
            String outputPath = config.getOutputPath();
            if (outputPath != null && outputPath.contains("/")) {
                String[] parts = outputPath.split("/");
                for (String part : parts) {
                    if (part.startsWith("zhangjiajie-")) {
                        targetModule = part;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 静态配置方法（用于独立运行时）
     */
    public static void configure(String packageName, String authorName, String module) {
        basePackage = packageName;
        author = authorName;
        targetModule = module;
    }

    // ==================== Getter 方法 ====================

    public static String getBasePackage() {
        return basePackage;
    }

    public static String getAuthor() {
        return author;
    }

    public static String getTargetModule() {
        return targetModule;
    }

    /**
     * 获取Java源码基础路径
     */
    public static String getMainJavaPath() {
        return System.getProperty("user.dir") + "/" + targetModule + "/src/main/java/"
               + basePackage.replace(".", "/");
    }

    /**
     * 获取测试源码基础路径
     */
    public static String getTestJavaPath() {
        return System.getProperty("user.dir") + "/" + targetModule + "/src/test/java/"
               + basePackage.replace(".", "/");
    }

    /**
     * 获取资源文件基础路径
     */
    public static String getMainResourcesPath() {
        return System.getProperty("user.dir") + "/" + targetModule + "/src/main/resources";
    }

    // ==================== 字符串转换方法 ====================

    /**
     * 转换为小驼峰命名
     * UserRole -> userRole
     */
    public static String toLowerCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 转换为kebab-case命名（用于URL）
     * UserRole -> user-role
     */
    public static String toKebabCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('-');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 获取当前日期字符串
     */
    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }

    /**
     * 表名转类名（大驼峰）
     * sys_user -> User (移除前缀后)
     * user_role -> UserRole
     */
    public static String tableNameToClassName(String tableName, String tablePrefix) {
        if (tablePrefix != null && !tablePrefix.isEmpty() && tableName.startsWith(tablePrefix)) {
            tableName = tableName.substring(tablePrefix.length());
        }
        return toCamelCase(tableName, true);
    }

    /**
     * 列名转字段名（小驼峰）
     * user_name -> userName
     */
    public static String columnNameToFieldName(String columnName) {
        return toCamelCase(columnName, false);
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param name            原名称
     * @param capitalizeFirst 首字母是否大写
     */
    public static String toCamelCase(String name, boolean capitalizeFirst) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        StringBuilder result = new StringBuilder();
        String[] parts = name.toLowerCase().split("_");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;
            if (i == 0 && !capitalizeFirst) {
                result.append(parts[i]);
            } else {
                result.append(Character.toUpperCase(parts[i].charAt(0)));
                result.append(parts[i].substring(1));
            }
        }
        return result.toString();
    }

    /**
     * 数据库类型转Java类型
     */
    public static String dbTypeToJavaType(String dbType) {
        if (dbType == null) return "String";
        return switch (dbType.toLowerCase()) {
            case "bigint" -> "Long";
            case "int", "integer", "tinyint", "smallint", "mediumint" -> "Integer";
            case "decimal", "numeric" -> "BigDecimal";
            case "float" -> "Float";
            case "double" -> "Double";
            case "bit", "boolean" -> "Boolean";
            case "datetime", "timestamp" -> "LocalDateTime";
            case "date" -> "LocalDate";
            case "time" -> "LocalTime";
            case "blob", "longblob", "mediumblob", "tinyblob" -> "byte[]";
            case "json" -> "String"; // 或者可以使用特定的JSON库类型
            default -> "String";
        };
    }

    /**
     * 获取Java类型对应的import语句
     */
    public static String getImportForJavaType(String javaType) {
        return switch (javaType) {
            case "BigDecimal" -> "java.math.BigDecimal";
            case "LocalDateTime" -> "java.time.LocalDateTime";
            case "LocalDate" -> "java.time.LocalDate";
            case "LocalTime" -> "java.time.LocalTime";
            default -> null; // 基本类型和String不需要import
        };
    }

    // ==================== 文件操作方法 ====================

    /**
     * 将内容写入文件
     *
     * @param basePath    基础路径
     * @param packageName 包名（如 "controller" 或 "service/impl"）
     * @param fileName    文件名
     * @param content     文件内容
     * @throws IOException 如果写入失败
     */
    public static void writeToFile(String basePath, String packageName, String fileName, String content)
            throws IOException {
        writeToFile(basePath, packageName, fileName, content, true);
    }

    /**
     * 将内容写入文件（带覆盖控制）
     *
     * @param basePath    基础路径
     * @param packageName 包名（如 "controller" 或 "service/impl"）
     * @param fileName    文件名
     * @param content     文件内容
     * @param overwrite   是否覆盖已有文件
     * @throws IOException 如果写入失败
     */
    public static void writeToFile(String basePath, String packageName, String fileName,
                                   String content, boolean overwrite) throws IOException {
        String dirPath = basePath + "/" + packageName.replace(".", "/");
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        if (file.exists() && !overwrite) {
            log.warn("文件已存在，跳过生成: {}", file.getAbsolutePath());
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        log.info("文件已生成: {}", file.getAbsolutePath());
    }

    /**
     * 打印生成完成信息
     *
     * @param title       标题
     * @param entityName  实体名
     * @param files       生成的文件列表
     */
    public static void printSuccess(String title, String entityName, String... files) {
        log.info("========================================");
        log.info("{}完成！", title);
        log.info("实体: {}", entityName);
        log.info("目标模块: {}", targetModule);
        log.info("生成文件:");
        for (String file : files) {
            log.info("  - {}", file);
        }
        log.info("========================================");
    }

    /**
     * 打印生成失败信息
     */
    public static void printError(String message, Exception e) {
        log.error("生成失败: {}", message, e);
    }
}
