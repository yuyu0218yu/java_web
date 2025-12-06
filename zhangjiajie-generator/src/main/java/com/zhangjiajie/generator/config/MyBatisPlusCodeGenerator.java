package com.zhangjiajie.generator.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * MyBatis-Plus代码生成器
 * 根据数据库表自动生成Entity、Mapper、Service、Controller等代码
 *
 * 使用方法：
 * 方式一：独立运行（使用默认配置或通过 configure() 方法配置）
 *   MyBatisPlusCodeGenerator.configure(config); // 可选
 *   MyBatisPlusCodeGenerator.generateCode("table_name");
 *
 * 方式二：Spring环境（自动读取application.yml配置）
 *   @Autowired GeneratorService generatorService;
 *   generatorService.generateCode("table_name", options);
 *
 * @author yushuang
 */
@Slf4j
public class MyBatisPlusCodeGenerator {

    // ==================== 默认配置（可通过 configure 方法覆盖） ====================

    /** 数据库URL */
    private static String dbUrl = "jdbc:mysql://localhost:3306/java_web?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";

    /** 数据库用户名 */
    private static String dbUsername = "root";

    /** 数据库密码 */
    private static String dbPassword = "123456";

    /** 父包名 */
    private static String parentPackage = "com.zhangjiajie.system";

    /** 目标模块 */
    private static String targetModule = "zhangjiajie-system";

    /** 作者名称 */
    private static String author = "zhangjiajie";

    /** 表前缀（生成时移除） */
    private static String[] tablePrefixes = {"t_", "sys_"};

    /** 是否启用Swagger */
    private static boolean enableSwagger = true;

    /** 是否覆盖已有文件 */
    private static boolean overwrite = true;

    // ==================== 配置方法 ====================

    /**
     * 使用 GeneratorConfig 配置
     * 在 Spring 环境下使用
     */
    public static void configure(GeneratorConfig config) {
        if (config != null) {
            parentPackage = config.getBasePackage();
            author = config.getAuthor();
            enableSwagger = config.isEnableSwagger();
            overwrite = config.isOverwrite();

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

            // 设置表前缀
            if (config.getTablePrefix() != null && !config.getTablePrefix().isEmpty()) {
                tablePrefixes = new String[]{config.getTablePrefix()};
            }
        }
    }

    /**
     * 配置数据库连接
     */
    public static void configureDatabase(String url, String username, String password) {
        dbUrl = url;
        dbUsername = username;
        dbPassword = password;
    }

    /**
     * 配置基础信息
     */
    public static void configureBasic(String packageName, String authorName, String module) {
        parentPackage = packageName;
        author = authorName;
        targetModule = module;
    }

    /**
     * 配置表前缀
     */
    public static void configureTablePrefix(String... prefixes) {
        tablePrefixes = prefixes;
    }

    // ==================== 路径计算 ====================

    /** 输出目录 */
    private static String getOutputDir() {
        return System.getProperty("user.dir") + "/" + targetModule + "/src/main/java";
    }

    /** Mapper XML文件输出目录 */
    private static String getMapperXmlDir() {
        return System.getProperty("user.dir") + "/" + targetModule + "/src/main/resources/mapper";
    }

    // ==================================================================

    public static void main(String[] args) {
        // 示例1：使用默认配置生成
        // generateCode("sys_user");

        // 示例2：自定义配置后生成
        // configureBasic("com.zhangjiajie.system", "zhangjiajie", "zhangjiajie-system");
        // configureTablePrefix("sys_", "t_");
        // generateCode("sys_user", "sys_role");

        log.info("=================================================");
        log.info("MyBatis-Plus 代码生成器");
        log.info("=================================================");
        log.info("当前配置:");
        log.info("  数据库URL: {}", dbUrl);
        log.info("  父包名: {}", parentPackage);
        log.info("  目标模块: {}", targetModule);
        log.info("  作者: {}", author);
        log.info("  输出目录: {}", getOutputDir());
        log.info("  Mapper XML目录: {}", getMapperXmlDir());
        log.info("=================================================");
        log.info("使用方式:");
        log.info("  MyBatisPlusCodeGenerator.generateCode(\"table_name\");");
        log.info("=================================================");
    }

    /**
     * 执行代码生成
     *
     * @param tableNames 表名数组，支持多个表
     */
    public static void generateCode(String... tableNames) {
        FastAutoGenerator.create(dbUrl, dbUsername, dbPassword)
            // 全局配置
            .globalConfig(builder -> {
                builder.author(author)                    // 作者
                    .outputDir(getOutputDir())            // 输出目录
                    .dateType(DateType.TIME_PACK)         // 使用java.time包
                    .commentDate("yyyy-MM-dd HH:mm:ss")   // 注释日期格式
                    .disableOpenDir();                    // 禁止打开输出目录

                if (enableSwagger) {
                    builder.enableSwagger();              // 开启Swagger注解
                }
            })
            // 包配置
            .packageConfig(builder -> {
                builder.parent(parentPackage)             // 父包名
                    .entity("entity")                     // Entity包名
                    .mapper("mapper")                     // Mapper包名
                    .service("service")                   // Service包名
                    .serviceImpl("service.impl")          // ServiceImpl包名
                    .controller("controller")             // Controller包名
                    .pathInfo(Collections.singletonMap(
                        OutputFile.xml, getMapperXmlDir() // Mapper XML文件路径
                    ));
            })
            // 策略配置
            .strategyConfig(builder -> {
                builder.addInclude(tableNames)            // 指定要生成的表名
                    .addTablePrefix(tablePrefixes)        // 过滤表前缀

                    // Entity策略配置
                    .entityBuilder()
                    .enableLombok()                       // 启用Lombok
                    .enableTableFieldAnnotation()         // 启用字段注解
                    .logicDeleteColumnName("deleted")     // 逻辑删除字段
                    .naming(NamingStrategy.underline_to_camel)        // 下划线转驼峰
                    .columnNaming(NamingStrategy.underline_to_camel)  // 列名转驼峰
                    .enableFileOverride()                 // 覆盖已有文件

                    // Mapper策略配置
                    .mapperBuilder()
                    .enableMapperAnnotation()             // 启用@Mapper注解
                    .enableBaseResultMap()                // 启用BaseResultMap
                    .enableBaseColumnList()               // 启用BaseColumnList
                    .enableFileOverride()                 // 覆盖已有文件

                    // Service策略配置
                    .serviceBuilder()
                    .formatServiceFileName("%sService")   // Service文件名格式
                    .formatServiceImplFileName("%sServiceImpl") // ServiceImpl文件名格式
                    .enableFileOverride()                 // 覆盖已有文件

                    // Controller策略配置
                    .controllerBuilder()
                    .enableRestStyle()                    // 启用REST风格
                    .enableHyphenStyle()                  // 启用驼峰转连字符
                    .enableFileOverride();                // 覆盖已有文件
            })
            // 模板引擎配置
            .templateEngine(new FreemarkerTemplateEngine())
            // 执行生成
            .execute();

        log.info("========================================");
        log.info("代码生成完成！");
        log.info("生成的表: {}", String.join(", ", tableNames));
        log.info("输出目录: {}", getOutputDir());
        log.info("目标模块: {}", targetModule);
        log.info("========================================");
    }

    /**
     * 根据表名前缀批量生成代码
     *
     * @param tablePrefix 表名前缀，如"sys_"会生成所有以sys_开头的表
     */
    public static void generateByPrefix(String tablePrefix) {
        FastAutoGenerator.create(dbUrl, dbUsername, dbPassword)
            .globalConfig(builder -> {
                builder.author(author)
                    .outputDir(getOutputDir())
                    .dateType(DateType.TIME_PACK)
                    .commentDate("yyyy-MM-dd HH:mm:ss")
                    .disableOpenDir();

                if (enableSwagger) {
                    builder.enableSwagger();
                }
            })
            .packageConfig(builder -> {
                builder.parent(parentPackage)
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .controller("controller")
                    .pathInfo(Collections.singletonMap(
                        OutputFile.xml, getMapperXmlDir()
                    ));
            })
            .strategyConfig(builder -> {
                builder.addTablePrefix(tablePrefix)       // 按前缀生成
                    .entityBuilder()
                    .enableLombok()
                    .enableTableFieldAnnotation()
                    .logicDeleteColumnName("deleted")
                    .naming(NamingStrategy.underline_to_camel)
                    .columnNaming(NamingStrategy.underline_to_camel)

                    .mapperBuilder()
                    .enableMapperAnnotation()
                    .enableBaseResultMap()
                    .enableBaseColumnList()

                    .serviceBuilder()
                    .formatServiceFileName("%sService")
                    .formatServiceImplFileName("%sServiceImpl")

                    .controllerBuilder()
                    .enableRestStyle()
                    .enableHyphenStyle();
            })
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();

        log.info("========================================");
        log.info("代码生成完成！（按前缀：{}）", tablePrefix);
        log.info("目标模块: {}", targetModule);
        log.info("========================================");
    }

    /**
     * 使用自定义配置生成代码
     *
     * @param config     生成器配置
     * @param tableNames 表名数组
     */
    public static void generateWithConfig(GeneratorConfig config, String... tableNames) {
        configure(config);
        generateCode(tableNames);
    }
}
