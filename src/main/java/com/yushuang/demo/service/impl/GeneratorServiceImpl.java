package com.yushuang.demo.service.impl;

import com.yushuang.demo.dto.GenerateOptions;
import com.yushuang.demo.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 代码生成器服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final JdbcTemplate jdbcTemplate;

    // 基础包名
    private static final String BASE_PACKAGE = "com.yushuang.demo";
    
    // 输出目录
    private static final String OUTPUT_DIR = System.getProperty("user.dir") + "/src/main/java/";

    @Override
    public List<Map<String, Object>> getTableList() {
        String sql = """
            SELECT 
                TABLE_NAME as tableName,
                TABLE_COMMENT as tableComment,
                CREATE_TIME as createTime,
                UPDATE_TIME as updateTime
            FROM information_schema.TABLES 
            WHERE TABLE_SCHEMA = DATABASE()
            ORDER BY TABLE_NAME
            """;
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getTableColumns(String tableName) {
        String sql = """
            SELECT 
                COLUMN_NAME as columnName,
                DATA_TYPE as dataType,
                COLUMN_TYPE as columnType,
                COLUMN_COMMENT as columnComment,
                COLUMN_KEY as columnKey,
                IS_NULLABLE as isNullable,
                EXTRA as extra
            FROM information_schema.COLUMNS 
            WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?
            ORDER BY ORDINAL_POSITION
            """;
        return jdbcTemplate.queryForList(sql, tableName);
    }

    @Override
    public Map<String, String> previewCode(String tableName, GenerateOptions options) {
        Map<String, String> codeMap = new HashMap<>();
        
        String className = tableNameToClassName(tableName, options.getTablePrefix());
        String lowerClassName = toLowerCaseFirstLetter(className);
        List<Map<String, Object>> columns = getTableColumns(tableName);
        
        if (Boolean.TRUE.equals(options.getGenerateEntity())) {
            codeMap.put("Entity", generateEntityCode(tableName, className, columns, options));
        }
        if (Boolean.TRUE.equals(options.getGenerateMapper())) {
            codeMap.put("Mapper", generateMapperCode(className, options));
            codeMap.put("MapperXml", generateMapperXmlCode(tableName, className, columns, options));
        }
        if (Boolean.TRUE.equals(options.getGenerateService())) {
            codeMap.put("Service", generateServiceCode(className, options));
            codeMap.put("ServiceImpl", generateServiceImplCode(className, options));
        }
        if (Boolean.TRUE.equals(options.getGenerateController())) {
            codeMap.put("Controller", generateControllerCode(className, lowerClassName, options));
        }
        
        return codeMap;
    }

    @Override
    public void generateCode(String tableName, GenerateOptions options) {
        String className = tableNameToClassName(tableName, options.getTablePrefix());
        String lowerClassName = toLowerCaseFirstLetter(className);
        List<Map<String, Object>> columns = getTableColumns(tableName);
        
        String packagePath = BASE_PACKAGE.replace(".", "/");
        
        try {
            if (Boolean.TRUE.equals(options.getGenerateEntity())) {
                String code = generateEntityCode(tableName, className, columns, options);
                writeFile(OUTPUT_DIR + packagePath + "/entity/" + className + ".java", code);
            }
            if (Boolean.TRUE.equals(options.getGenerateMapper())) {
                String code = generateMapperCode(className, options);
                writeFile(OUTPUT_DIR + packagePath + "/mapper/" + className + "Mapper.java", code);
                
                // 生成 Mapper XML 文件
                String xmlCode = generateMapperXmlCode(tableName, className, columns, options);
                String xmlPath = System.getProperty("user.dir") + "/src/main/resources/mapper/" + className + "Mapper.xml";
                writeFile(xmlPath, xmlCode);
            }
            if (Boolean.TRUE.equals(options.getGenerateService())) {
                String serviceCode = generateServiceCode(className, options);
                writeFile(OUTPUT_DIR + packagePath + "/service/" + className + "Service.java", serviceCode);
                
                String implCode = generateServiceImplCode(className, options);
                writeFile(OUTPUT_DIR + packagePath + "/service/impl/" + className + "ServiceImpl.java", implCode);
            }
            if (Boolean.TRUE.equals(options.getGenerateController())) {
                String code = generateControllerCode(className, lowerClassName, options);
                writeFile(OUTPUT_DIR + packagePath + "/controller/" + className + "Controller.java", code);
            }
            
            log.info("代码生成成功：{}", className);
        } catch (IOException e) {
            log.error("代码生成失败", e);
            throw new RuntimeException("代码生成失败：" + e.getMessage());
        }
    }

    // ==================== 代码生成方法 ====================

    private String generateEntityCode(String tableName, String className, 
                                       List<Map<String, Object>> columns, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String author = options.getAuthor() != null ? options.getAuthor() : "generator";
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        sb.append("package ").append(BASE_PACKAGE).append(".entity;\n\n");
        
        // Imports
        sb.append("import com.baomidou.mybatisplus.annotation.*;\n");
        if (Boolean.TRUE.equals(options.getEnableLombok())) {
            sb.append("import lombok.Data;\n");
        }
        if (Boolean.TRUE.equals(options.getEnableSwagger())) {
            sb.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        }
        sb.append("import java.io.Serializable;\n");
        sb.append("import java.time.LocalDateTime;\n");
        sb.append("import java.math.BigDecimal;\n\n");
        
        // Class comment
        sb.append("/**\n");
        sb.append(" * ").append(className).append(" 实体类\n");
        sb.append(" * 对应表：").append(tableName).append("\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" * @date ").append(now).append("\n");
        sb.append(" */\n");
        
        if (Boolean.TRUE.equals(options.getEnableLombok())) {
            sb.append("@Data\n");
        }
        sb.append("@TableName(\"").append(tableName).append("\")\n");
        if (Boolean.TRUE.equals(options.getEnableSwagger())) {
            sb.append("@Schema(description = \"").append(className).append("实体\")\n");
        }
        sb.append("public class ").append(className).append(" implements Serializable {\n\n");
        sb.append("    private static final long serialVersionUID = 1L;\n\n");
        
        // Fields
        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("columnName");
            String dataType = (String) column.get("dataType");
            String columnComment = (String) column.get("columnComment");
            String columnKey = (String) column.get("columnKey");
            String extra = (String) column.get("extra");
            
            String fieldName = columnNameToFieldName(columnName);
            String javaType = dbTypeToJavaType(dataType);
            
            // 注释
            if (columnComment != null && !columnComment.isEmpty()) {
                sb.append("    /** ").append(columnComment).append(" */\n");
            }
            
            // Swagger注解
            if (Boolean.TRUE.equals(options.getEnableSwagger()) && columnComment != null) {
                sb.append("    @Schema(description = \"").append(columnComment).append("\")\n");
            }
            
            // 主键注解
            if ("PRI".equals(columnKey)) {
                sb.append("    @TableId(");
                if ("auto_increment".equalsIgnoreCase(extra)) {
                    sb.append("type = IdType.AUTO");
                } else {
                    sb.append("type = IdType.ASSIGN_ID");
                }
                sb.append(")\n");
            }
            
            // 逻辑删除
            if ("deleted".equalsIgnoreCase(columnName)) {
                sb.append("    @TableLogic\n");
            }
            
            // 自动填充
            if ("create_time".equalsIgnoreCase(columnName)) {
                sb.append("    @TableField(fill = FieldFill.INSERT)\n");
            } else if ("update_time".equalsIgnoreCase(columnName)) {
                sb.append("    @TableField(fill = FieldFill.INSERT_UPDATE)\n");
            }
            
            sb.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n\n");
        }
        
        sb.append("}\n");
        return sb.toString();
    }

    private String generateMapperCode(String className, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String author = options.getAuthor() != null ? options.getAuthor() : "generator";
        
        sb.append("package ").append(BASE_PACKAGE).append(".mapper;\n\n");
        sb.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n");
        sb.append("import ").append(BASE_PACKAGE).append(".entity.").append(className).append(";\n");
        sb.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(className).append(" Mapper接口\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" */\n");
        sb.append("@Mapper\n");
        sb.append("public interface ").append(className).append("Mapper extends BaseMapper<").append(className).append("> {\n\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    /**
     * 生成 Mapper XML 映射文件
     */
    private String generateMapperXmlCode(String tableName, String className, 
                                          List<Map<String, Object>> columns, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String namespace = BASE_PACKAGE + ".mapper." + className + "Mapper";
        String entityClass = BASE_PACKAGE + ".entity." + className;
        
        // XML 头部
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"").append(namespace).append("\">\n\n");
        
        // BaseResultMap
        sb.append("    <!-- 通用结果映射 -->\n");
        sb.append("    <resultMap id=\"BaseResultMap\" type=\"").append(entityClass).append("\">\n");
        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("columnName");
            String columnKey = (String) column.get("columnKey");
            String fieldName = columnNameToFieldName(columnName);
            
            if ("PRI".equals(columnKey)) {
                sb.append("        <id column=\"").append(columnName).append("\" property=\"").append(fieldName).append("\" />\n");
            } else {
                sb.append("        <result column=\"").append(columnName).append("\" property=\"").append(fieldName).append("\" />\n");
            }
        }
        sb.append("    </resultMap>\n\n");
        
        // Base_Column_List
        sb.append("    <!-- 通用列列表 -->\n");
        sb.append("    <sql id=\"Base_Column_List\">\n");
        sb.append("        ");
        List<String> columnNames = new ArrayList<>();
        for (Map<String, Object> column : columns) {
            columnNames.add((String) column.get("columnName"));
        }
        sb.append(String.join(", ", columnNames));
        sb.append("\n");
        sb.append("    </sql>\n\n");
        
        // 示例查询方法
        sb.append("    <!-- 根据ID查询（示例，可删除，BaseMapper已提供） -->\n");
        sb.append("    <!--\n");
        sb.append("    <select id=\"selectById\" parameterType=\"java.lang.Long\" resultMap=\"BaseResultMap\">\n");
        sb.append("        SELECT <include refid=\"Base_Column_List\" />\n");
        sb.append("        FROM ").append(tableName).append("\n");
        sb.append("        WHERE id = #{id} AND deleted = 0\n");
        sb.append("    </select>\n");
        sb.append("    -->\n\n");
        
        // 分页查询示例
        sb.append("    <!-- 分页查询（示例，可根据业务扩展） -->\n");
        sb.append("    <!--\n");
        sb.append("    <select id=\"selectPage\" resultMap=\"BaseResultMap\">\n");
        sb.append("        SELECT <include refid=\"Base_Column_List\" />\n");
        sb.append("        FROM ").append(tableName).append("\n");
        sb.append("        WHERE deleted = 0\n");
        sb.append("        <if test=\"keyword != null and keyword != ''\">\n");
        sb.append("            AND (name LIKE CONCAT('%', #{keyword}, '%'))\n");
        sb.append("        </if>\n");
        sb.append("        ORDER BY create_time DESC\n");
        sb.append("    </select>\n");
        sb.append("    -->\n\n");
        
        sb.append("</mapper>\n");
        
        return sb.toString();
    }

    private String generateServiceCode(String className, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String author = options.getAuthor() != null ? options.getAuthor() : "generator";
        
        sb.append("package ").append(BASE_PACKAGE).append(".service;\n\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.IService;\n");
        sb.append("import ").append(BASE_PACKAGE).append(".entity.").append(className).append(";\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(className).append(" Service接口\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" */\n");
        sb.append("public interface ").append(className).append("Service extends IService<").append(className).append("> {\n\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    private String generateServiceImplCode(String className, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String author = options.getAuthor() != null ? options.getAuthor() : "generator";
        
        sb.append("package ").append(BASE_PACKAGE).append(".service.impl;\n\n");
        sb.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n");
        sb.append("import ").append(BASE_PACKAGE).append(".entity.").append(className).append(";\n");
        sb.append("import ").append(BASE_PACKAGE).append(".mapper.").append(className).append("Mapper;\n");
        sb.append("import ").append(BASE_PACKAGE).append(".service.").append(className).append("Service;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(className).append(" Service实现类\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" */\n");
        sb.append("@Slf4j\n");
        sb.append("@Service\n");
        sb.append("public class ").append(className).append("ServiceImpl extends ServiceImpl<")
          .append(className).append("Mapper, ").append(className).append("> implements ")
          .append(className).append("Service {\n\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    private String generateControllerCode(String className, String lowerClassName, GenerateOptions options) {
        StringBuilder sb = new StringBuilder();
        String author = options.getAuthor() != null ? options.getAuthor() : "generator";
        
        sb.append("package ").append(BASE_PACKAGE).append(".controller;\n\n");
        sb.append("import ").append(BASE_PACKAGE).append(".common.BaseController;\n");
        sb.append("import ").append(BASE_PACKAGE).append(".entity.").append(className).append(";\n");
        sb.append("import ").append(BASE_PACKAGE).append(".service.").append(className).append("Service;\n");
        if (Boolean.TRUE.equals(options.getEnableSwagger())) {
            sb.append("import io.swagger.v3.oas.annotations.tags.Tag;\n");
        }
        sb.append("import org.springframework.web.bind.annotation.*;\n\n");
        sb.append("/**\n");
        sb.append(" * ").append(className).append(" 控制器\n");
        sb.append(" * 继承BaseController，自动拥有CRUD接口\n");
        sb.append(" *\n");
        sb.append(" * @author ").append(author).append("\n");
        sb.append(" */\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/api/").append(toKebabCase(className)).append("\")\n");
        if (Boolean.TRUE.equals(options.getEnableSwagger())) {
            sb.append("@Tag(name = \"").append(className).append("管理\")\n");
        }
        sb.append("public class ").append(className).append("Controller extends BaseController<")
          .append(className).append("Service, ").append(className).append("> {\n\n");
        sb.append("    @Override\n");
        sb.append("    protected String getPermissionPrefix() {\n");
        sb.append("        return \"").append(lowerClassName).append("\";\n");
        sb.append("    }\n\n");
        sb.append("    // 自动继承以下接口：\n");
        sb.append("    // GET  /api/").append(toKebabCase(className)).append("/page - 分页查询\n");
        sb.append("    // GET  /api/").append(toKebabCase(className)).append("/{id} - ID查询\n");
        sb.append("    // GET  /api/").append(toKebabCase(className)).append("/list - 列表查询\n");
        sb.append("    // POST /api/").append(toKebabCase(className)).append(" - 创建\n");
        sb.append("    // PUT  /api/").append(toKebabCase(className)).append("/{id} - 更新\n");
        sb.append("    // DELETE /api/").append(toKebabCase(className)).append("/{id} - 删除\n");
        sb.append("    // DELETE /api/").append(toKebabCase(className)).append("/batch - 批量删除\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    // ==================== 工具方法 ====================

    private String tableNameToClassName(String tableName, String prefix) {
        if (prefix != null && !prefix.isEmpty() && tableName.startsWith(prefix)) {
            tableName = tableName.substring(prefix.length());
        }
        return toCamelCase(tableName, true);
    }

    private String columnNameToFieldName(String columnName) {
        return toCamelCase(columnName, false);
    }

    private String toCamelCase(String name, boolean capitalizeFirst) {
        StringBuilder result = new StringBuilder();
        String[] parts = name.toLowerCase().split("_");
        for (int i = 0; i < parts.length; i++) {
            if (i == 0 && !capitalizeFirst) {
                result.append(parts[i]);
            } else {
                result.append(Character.toUpperCase(parts[i].charAt(0)));
                result.append(parts[i].substring(1));
            }
        }
        return result.toString();
    }

    private String toLowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    private String toKebabCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

    private String dbTypeToJavaType(String dbType) {
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
            default -> "String";
        };
    }

    private void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        log.info("文件已生成：{}", filePath);
    }
}
