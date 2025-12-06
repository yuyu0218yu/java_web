package com.yushuang.demo.service.impl;

import com.yushuang.demo.dto.GenerateOptions;
import com.yushuang.demo.generator.GeneratorConfig;
import com.yushuang.demo.generator.GeneratorException;
import com.yushuang.demo.generator.GeneratorHelper;
import com.yushuang.demo.generator.TemplateEngine;
import com.yushuang.demo.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成器服务实现
 * 使用 Freemarker 模板引擎 + 配置类
 *
 * @author yushuang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final JdbcTemplate jdbcTemplate;
    private final GeneratorConfig config;
    private final TemplateEngine templateEngine;

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
        Map<String, String> codeMap = new LinkedHashMap<>();
        Map<String, Object> dataModel = buildDataModel(tableName, options);
        
        if (Boolean.TRUE.equals(options.getGenerateEntity())) {
            codeMap.put("Entity", templateEngine.render("entity.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateMapper())) {
            codeMap.put("Mapper", templateEngine.render("mapper.ftl", dataModel));
            codeMap.put("MapperXml", templateEngine.render("mapperXml.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateService())) {
            codeMap.put("Service", templateEngine.render("service.ftl", dataModel));
            codeMap.put("ServiceImpl", templateEngine.render("serviceImpl.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateController())) {
            codeMap.put("Controller", templateEngine.render("controller.ftl", dataModel));
        }
        
        return codeMap;
    }

    @Override
    public void generateCode(String tableName, GenerateOptions options) {
        Map<String, Object> dataModel = buildDataModel(tableName, options);
        String className = (String) dataModel.get("className");
        
        String basePackage = getBasePackage(options);
        String outputPath = config.getOutputPath();
        String packagePath = basePackage.replace(".", "/");
        String basePath = outputPath + "/" + packagePath;
        
        boolean overwrite = config.isOverwrite();
        
        try {
            if (Boolean.TRUE.equals(options.getGenerateEntity())) {
                String filePath = basePath + "/entity/" + className + ".java";
                templateEngine.renderToFile("entity.ftl", dataModel, filePath, overwrite);
            }
            if (Boolean.TRUE.equals(options.getGenerateMapper())) {
                String mapperPath = basePath + "/mapper/" + className + "Mapper.java";
                templateEngine.renderToFile("mapper.ftl", dataModel, mapperPath, overwrite);
                
                String xmlPath = config.getMapperXmlPath() + "/" + className + "Mapper.xml";
                templateEngine.renderToFile("mapperXml.ftl", dataModel, xmlPath, overwrite);
            }
            if (Boolean.TRUE.equals(options.getGenerateService())) {
                String servicePath = basePath + "/service/" + className + "Service.java";
                templateEngine.renderToFile("service.ftl", dataModel, servicePath, overwrite);
                
                String implPath = basePath + "/service/impl/" + className + "ServiceImpl.java";
                templateEngine.renderToFile("serviceImpl.ftl", dataModel, implPath, overwrite);
            }
            if (Boolean.TRUE.equals(options.getGenerateController())) {
                String controllerPath = basePath + "/controller/" + className + "Controller.java";
                templateEngine.renderToFile("controller.ftl", dataModel, controllerPath, overwrite);
            }
            
            log.info("代码生成成功：{}", className);
        } catch (GeneratorException e) {
            log.error("代码生成失败", e);
            throw e;
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 构建模板数据模型
     */
    private Map<String, Object> buildDataModel(String tableName, GenerateOptions options) {
        String tablePrefix = options.getTablePrefix() != null ? options.getTablePrefix() : config.getTablePrefix();
        String className = GeneratorHelper.tableNameToClassName(tableName, tablePrefix);
        String lowerCamelCaseName = GeneratorHelper.toLowerCamelCase(className);
        String kebabCaseName = GeneratorHelper.toKebabCase(className);
        
        List<Map<String, Object>> rawColumns = getTableColumns(tableName);
        List<Map<String, Object>> columns = rawColumns.stream()
            .map(this::convertColumn)
            .collect(Collectors.toList());
        
        // 构建列名列表
        String columnList = rawColumns.stream()
            .map(col -> (String) col.get("columnName"))
            .collect(Collectors.joining(", "));
        
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("basePackage", getBasePackage(options));
        dataModel.put("author", getAuthor(options));
        dataModel.put("date", GeneratorHelper.getCurrentDate());
        dataModel.put("tableName", tableName);
        dataModel.put("className", className);
        dataModel.put("lowerCamelCaseName", lowerCamelCaseName);
        dataModel.put("kebabCaseName", kebabCaseName);
        dataModel.put("columns", columns);
        dataModel.put("columnList", columnList);
        dataModel.put("enableSwagger", getEnableSwagger(options));
        dataModel.put("enableLombok", getEnableLombok(options));
        
        return dataModel;
    }

    /**
     * 转换列信息
     */
    private Map<String, Object> convertColumn(Map<String, Object> rawColumn) {
        Map<String, Object> column = new HashMap<>();
        
        String columnName = (String) rawColumn.get("columnName");
        String dataType = (String) rawColumn.get("dataType");
        String columnKey = (String) rawColumn.get("columnKey");
        String extra = (String) rawColumn.get("extra");
        String comment = (String) rawColumn.get("columnComment");
        
        column.put("columnName", columnName);
        column.put("fieldName", GeneratorHelper.columnNameToFieldName(columnName));
        column.put("javaType", GeneratorHelper.dbTypeToJavaType(dataType));
        column.put("comment", comment);
        column.put("isPrimaryKey", "PRI".equals(columnKey));
        column.put("isAutoIncrement", "auto_increment".equalsIgnoreCase(extra));
        
        return column;
    }

    /**
     * 获取基础包名（优先使用请求参数，否则使用配置）
     */
    private String getBasePackage(GenerateOptions options) {
        if (options.getModuleName() != null && !options.getModuleName().isEmpty()) {
            return config.getBasePackage() + "." + options.getModuleName();
        }
        return config.getBasePackage();
    }

    /**
     * 获取作者
     */
    private String getAuthor(GenerateOptions options) {
        return options.getAuthor() != null ? options.getAuthor() : config.getAuthor();
    }

    /**
     * 是否启用 Swagger
     */
    private boolean getEnableSwagger(GenerateOptions options) {
        return options.getEnableSwagger() != null ? options.getEnableSwagger() : config.isEnableSwagger();
    }

    /**
     * 是否启用 Lombok
     */
    private boolean getEnableLombok(GenerateOptions options) {
        return options.getEnableLombok() != null ? options.getEnableLombok() : config.isEnableLombok();
    }
}
