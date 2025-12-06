package com.yushuang.demo.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus代码生成器
 * 根据数据库表自动生成Entity、Mapper、Service、Controller等代码
 *
 * 使用方法：
 * 1. 配置数据库连接信息
 * 2. 指定要生成代码的表名
 * 3. 运行main方法
 *
 * @author yushuang
 */
public class MyBatisPlusCodeGenerator {

    // ==================== 配置区域（根据需要修改） ====================

    /** 数据库URL */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java_web?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";

    /** 数据库用户名 */
    private static final String DB_USERNAME = "root";

    /** 数据库密码 */
    private static final String DB_PASSWORD = "123456";

    /** 父包名 */
    private static final String PARENT_PACKAGE = "com.yushuang.demo";

    /** 作者名称 */
    private static final String AUTHOR = "yushuang";

    /** 输出目录 */
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java";

    /** Mapper XML文件输出目录 */
    private static final String MAPPER_XML_DIR = System.getProperty("user.dir") + "/src/main/resources/mapper";

    // ==================================================================

    public static void main(String[] args) {
        // 示例：生成指定表的代码
        generateCode("example_table");

        // 生成多个表
        // generateCode("table1", "table2", "table3");
    }

    /**
     * 执行代码生成
     *
     * @param tableNames 表名数组，支持多个表
     */
    public static void generateCode(String... tableNames) {
        FastAutoGenerator.create(DB_URL, DB_USERNAME, DB_PASSWORD)
            // 全局配置
            .globalConfig(builder -> {
                builder.author(AUTHOR)                    // 作者
                    .enableSwagger()                      // 开启Swagger注解
                    .outputDir(OUTPUT_DIR)                // 输出目录
                    .dateType(DateType.TIME_PACK)         // 使用java.time包
                    .commentDate("yyyy-MM-dd HH:mm:ss")   // 注释日期格式
                    .disableOpenDir();                    // 禁止打开输出目录
            })
            // 包配置
            .packageConfig(builder -> {
                builder.parent(PARENT_PACKAGE)            // 父包名
                    .entity("entity")                     // Entity包名
                    .mapper("mapper")                     // Mapper包名
                    .service("service")                   // Service包名
                    .serviceImpl("service.impl")          // ServiceImpl包名
                    .controller("controller")             // Controller包名
                    .pathInfo(Collections.singletonMap(
                        OutputFile.xml, MAPPER_XML_DIR    // Mapper XML文件路径
                    ));
            })
            // 策略配置
            .strategyConfig(builder -> {
                builder.addInclude(tableNames)            // 指定要生成的表名
                    .addTablePrefix("t_", "sys_")         // 过滤表前缀

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

        System.out.println("========================================");
        System.out.println("代码生成完成！");
        System.out.println("生成的表: " + String.join(", ", tableNames));
        System.out.println("输出目录: " + OUTPUT_DIR);
        System.out.println("========================================");
    }

    /**
     * 根据表名前缀批量生成代码
     *
     * @param tablePrefix 表名前缀，如"sys_"会生成所有以sys_开头的表
     */
    public static void generateByPrefix(String tablePrefix) {
        FastAutoGenerator.create(DB_URL, DB_USERNAME, DB_PASSWORD)
            .globalConfig(builder -> {
                builder.author(AUTHOR)
                    .enableSwagger()
                    .outputDir(OUTPUT_DIR)
                    .dateType(DateType.TIME_PACK)
                    .commentDate("yyyy-MM-dd HH:mm:ss")
                    .disableOpenDir();
            })
            .packageConfig(builder -> {
                builder.parent(PARENT_PACKAGE)
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .controller("controller")
                    .pathInfo(Collections.singletonMap(
                        OutputFile.xml, MAPPER_XML_DIR
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

        System.out.println("========================================");
        System.out.println("代码生成完成！（按前缀：" + tablePrefix + "）");
        System.out.println("========================================");
    }
}
