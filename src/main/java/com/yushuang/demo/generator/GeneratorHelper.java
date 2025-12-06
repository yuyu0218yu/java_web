package com.yushuang.demo.generator;

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
 * @author yushuang
 * @since 2025-12-06
 */
@Slf4j
public final class GeneratorHelper {

    public static final String BASE_PACKAGE = "com.yushuang.demo";
    public static final String AUTHOR = "yushuang";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private GeneratorHelper() {
        // 工具类不应被实例化
    }

    /**
     * 获取Java源码基础路径
     */
    public static String getMainJavaPath() {
        return System.getProperty("user.dir") + "/src/main/java/com/yushuang/demo";
    }

    /**
     * 获取测试源码基础路径
     */
    public static String getTestJavaPath() {
        return System.getProperty("user.dir") + "/src/test/java/com/yushuang/demo";
    }

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
        String dirPath = basePath + "/" + packageName.replace(".", "/");
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
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
