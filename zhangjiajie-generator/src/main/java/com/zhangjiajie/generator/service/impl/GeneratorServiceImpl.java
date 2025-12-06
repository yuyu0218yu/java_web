package com.zhangjiajie.generator.service.impl;

import com.zhangjiajie.generator.dto.GenerateOptions;
import com.zhangjiajie.generator.config.GeneratorConfig;
import com.zhangjiajie.generator.config.GeneratorException;
import com.zhangjiajie.generator.config.GeneratorHelper;
import com.zhangjiajie.generator.config.TemplateEngine;
import com.zhangjiajie.generator.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        validateTableName(tableName);
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
        validateTableName(tableName);
        Map<String, String> codeMap = new LinkedHashMap<>();
        Map<String, Object> dataModel = buildDataModel(tableName, options);
        String className = (String) dataModel.get("className");

        // 基础代码预览
        if (Boolean.TRUE.equals(options.getGenerateEntity())) {
            codeMap.put(className + ".java (Entity)", templateEngine.render("entity.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateMapper())) {
            codeMap.put(className + "Mapper.java", templateEngine.render("mapper.ftl", dataModel));
            codeMap.put(className + "Mapper.xml", templateEngine.render("mapperXml.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateService())) {
            codeMap.put(className + "Service.java", templateEngine.render("service.ftl", dataModel));
            codeMap.put(className + "ServiceImpl.java", templateEngine.render("serviceImpl.ftl", dataModel));
        }
        if (Boolean.TRUE.equals(options.getGenerateController())) {
            codeMap.put(className + "Controller.java", templateEngine.render("controller.ftl", dataModel));
        }

        // DTO 代码预览
        if (Boolean.TRUE.equals(options.getGenerateDto())) {
            codeMap.put("Create" + className + "Request.java", generateCreateRequestCode(dataModel));
            codeMap.put("Update" + className + "Request.java", generateUpdateRequestCode(dataModel));
            codeMap.put(className + "Response.java", generateResponseCode(dataModel));
        }

        // Converter 代码预览
        if (Boolean.TRUE.equals(options.getGenerateConverter())) {
            codeMap.put(className + "Converter.java", generateConverterCode(dataModel));
        }

        // Test 代码预览
        if (Boolean.TRUE.equals(options.getGenerateTest())) {
            codeMap.put(className + "ControllerTest.java", generateControllerTestCode(dataModel));
            codeMap.put(className + "ServiceTest.java", generateServiceTestCode(dataModel));
        }

        return codeMap;
    }

    @Override
    public void generateCode(String tableName, GenerateOptions options) {
        validateTableName(tableName);
        Map<String, Object> dataModel = buildDataModel(tableName, options);
        String className = (String) dataModel.get("className");

        String basePackage = getBasePackage(options);
        String outputPath = config.getOutputPath();
        String packagePath = basePackage.replace(".", "/");
        String basePath = outputPath + "/" + packagePath;

        boolean overwrite = options.getOverwrite() != null ? options.getOverwrite() : config.isOverwrite();

        try {
            // 生成基础代码
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

            // 生成 DTO
            if (Boolean.TRUE.equals(options.getGenerateDto())) {
                generateDtoFiles(basePath, className, dataModel, overwrite);
            }

            // 生成 Converter
            if (Boolean.TRUE.equals(options.getGenerateConverter())) {
                generateConverterFile(basePath, className, dataModel, overwrite);
            }

            // 生成测试代码
            if (Boolean.TRUE.equals(options.getGenerateTest())) {
                generateTestFiles(className, dataModel, overwrite);
            }

            log.info("代码生成成功：{}", className);
        } catch (GeneratorException e) {
            log.error("代码生成失败", e);
            throw e;
        }
    }

    /**
     * 下载生成的代码（ZIP 格式）
     */
    @Override
    public byte[] downloadCode(String tableName, GenerateOptions options) {
        Map<String, String> codeMap = previewCode(tableName, options);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, String> entry : codeMap.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();

                // 根据文件类型确定目录
                String path = determineFilePath(fileName);

                ZipEntry zipEntry = new ZipEntry(path);
                zos.putNextEntry(zipEntry);
                zos.write(content.getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new GeneratorException("打包代码失败", e);
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

        // 收集需要的 import
        Set<String> imports = columns.stream()
                .map(col -> GeneratorHelper.getImportForJavaType((String) col.get("javaType")))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

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
        dataModel.put("imports", imports);
        dataModel.put("enableSwagger", getEnableSwagger(options));
        dataModel.put("enableLombok", getEnableLombok(options));
        dataModel.put("entityCnName", options.getEntityCnName() != null ? options.getEntityCnName() : className);
        dataModel.put("apiModuleName", options.getApiModuleName() != null ? options.getApiModuleName() : className + "管理");

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
     * 生成 DTO 文件
     */
    private void generateDtoFiles(String basePath, String className, Map<String, Object> dataModel, boolean overwrite) {
        String dtoPath = basePath + "/dto/";

        // Create Request
        writeStringToFile(dtoPath + "Create" + className + "Request.java",
                generateCreateRequestCode(dataModel), overwrite);

        // Update Request
        writeStringToFile(dtoPath + "Update" + className + "Request.java",
                generateUpdateRequestCode(dataModel), overwrite);

        // Response
        writeStringToFile(dtoPath + className + "Response.java",
                generateResponseCode(dataModel), overwrite);
    }

    /**
     * 生成 Converter 文件
     */
    private void generateConverterFile(String basePath, String className, Map<String, Object> dataModel, boolean overwrite) {
        String converterPath = basePath + "/converter/" + className + "Converter.java";
        writeStringToFile(converterPath, generateConverterCode(dataModel), overwrite);
    }

    /**
     * 生成测试文件
     */
    private void generateTestFiles(String className, Map<String, Object> dataModel, boolean overwrite) {
        String testBasePath = config.getFullTestPath();

        // Controller Test
        writeStringToFile(testBasePath + "/controller/" + className + "ControllerTest.java",
                generateControllerTestCode(dataModel), overwrite);

        // Service Test
        writeStringToFile(testBasePath + "/service/" + className + "ServiceTest.java",
                generateServiceTestCode(dataModel), overwrite);
    }

    /**
     * 写入字符串到文件
     */
    private void writeStringToFile(String filePath, String content, boolean overwrite) {
        File file = new File(filePath);
        if (file.exists() && !overwrite) {
            log.warn("文件已存在，跳过生成: {}", filePath);
            return;
        }

        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
            log.info("生成文件: {}", filePath);
        } catch (IOException e) {
            throw new GeneratorException("写入文件失败: " + filePath, e);
        }
    }

    /**
     * 根据文件名确定在 ZIP 中的路径
     */
    private String determineFilePath(String fileName) {
        if (fileName.contains("Controller") && fileName.endsWith(".java")) {
            if (fileName.contains("Test")) {
                return "test/controller/" + fileName;
            }
            return "main/controller/" + fileName;
        }
        if (fileName.contains("Service") && fileName.endsWith(".java")) {
            if (fileName.contains("Test")) {
                return "test/service/" + fileName;
            }
            if (fileName.contains("Impl")) {
                return "main/service/impl/" + fileName;
            }
            return "main/service/" + fileName;
        }
        if (fileName.contains("Mapper")) {
            if (fileName.endsWith(".xml")) {
                return "resources/mapper/" + fileName;
            }
            return "main/mapper/" + fileName;
        }
        if (fileName.contains("Request") || fileName.contains("Response")) {
            return "main/dto/" + fileName;
        }
        if (fileName.contains("Converter")) {
            return "main/converter/" + fileName;
        }
        if (fileName.endsWith(".java (Entity)")) {
            return "main/entity/" + fileName.replace(" (Entity)", "");
        }
        return fileName;
    }

    // ==================== 代码生成方法（内联模板） ====================

    private String generateCreateRequestCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String entityCnName = (String) dataModel.get("entityCnName");
        String author = (String) dataModel.get("author");
        String date = (String) dataModel.get("date");

        return String.format("""
                package %s.dto;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;

                /**
                 * 创建%s请求DTO
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "创建%s请求")
                public class Create%sRequest implements Serializable {

                    private static final long serialVersionUID = 1L;

                    // TODO: 根据实际业务需求添加字段

                }
                """, basePackage, entityCnName, author, date, entityCnName, className);
    }

    private String generateUpdateRequestCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String entityCnName = (String) dataModel.get("entityCnName");
        String author = (String) dataModel.get("author");
        String date = (String) dataModel.get("date");

        return String.format("""
                package %s.dto;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import jakarta.validation.constraints.NotNull;
                import java.io.Serializable;

                /**
                 * 更新%s请求DTO
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "更新%s请求")
                public class Update%sRequest implements Serializable {

                    private static final long serialVersionUID = 1L;

                    @Schema(description = "%sID", requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "%sID不能为空")
                    private Long id;

                    // TODO: 根据实际业务需求添加字段

                }
                """, basePackage, entityCnName, author, date, entityCnName, className, entityCnName, entityCnName);
    }

    private String generateResponseCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String entityCnName = (String) dataModel.get("entityCnName");
        String author = (String) dataModel.get("author");
        String date = (String) dataModel.get("date");

        return String.format("""
                package %s.dto;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s响应DTO
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "%s响应")
                public class %sResponse implements Serializable {

                    private static final long serialVersionUID = 1L;

                    @Schema(description = "%sID")
                    private Long id;

                    // TODO: 根据实际业务需求添加字段

                    @Schema(description = "创建时间")
                    private LocalDateTime createTime;

                    @Schema(description = "更新时间")
                    private LocalDateTime updateTime;
                }
                """, basePackage, entityCnName, author, date, entityCnName, className, entityCnName);
    }

    private String generateConverterCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String entityCnName = (String) dataModel.get("entityCnName");
        String author = (String) dataModel.get("author");
        String date = (String) dataModel.get("date");

        return String.format("""
                package %s.converter;

                import %s.dto.Create%sRequest;
                import %s.dto.Update%sRequest;
                import %s.dto.%sResponse;
                import %s.entity.%s;
                import org.springframework.beans.BeanUtils;

                import java.util.ArrayList;
                import java.util.List;
                import java.util.stream.Collectors;

                /**
                 * %s转换器
                 *
                 * @author %s
                 * @since %s
                 */
                public final class %sConverter {

                    private %sConverter() {}

                    public static %s toEntity(Create%sRequest request) {
                        if (request == null) return null;
                        %s entity = new %s();
                        BeanUtils.copyProperties(request, entity);
                        return entity;
                    }

                    public static %s toEntity(Update%sRequest request) {
                        if (request == null) return null;
                        %s entity = new %s();
                        BeanUtils.copyProperties(request, entity);
                        return entity;
                    }

                    public static %sResponse toResponse(%s entity) {
                        if (entity == null) return null;
                        %sResponse response = new %sResponse();
                        BeanUtils.copyProperties(entity, response);
                        return response;
                    }

                    public static List<%sResponse> toResponseList(List<%s> entities) {
                        if (entities == null) return new ArrayList<>();
                        return entities.stream().map(%sConverter::toResponse).collect(Collectors.toList());
                    }
                }
                """, basePackage, basePackage, className, basePackage, className,
                basePackage, className, basePackage, className,
                entityCnName, author, date, className, className,
                className, className, className, className,
                className, className, className, className,
                className, className, className, className,
                className, className, className);
    }

    private String generateControllerTestCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String lowerCamelCaseName = (String) dataModel.get("lowerCamelCaseName");
        String kebabCaseName = (String) dataModel.get("kebabCaseName");

        return String.format("""
                package %s.controller;

                import org.junit.jupiter.api.DisplayName;
                import org.junit.jupiter.api.Test;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
                import org.springframework.boot.test.context.SpringBootTest;
                import org.springframework.http.MediaType;
                import org.springframework.security.test.context.support.WithMockUser;
                import org.springframework.test.web.servlet.MockMvc;

                import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
                import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

                /**
                 * %sController 测试类
                 */
                @SpringBootTest
                @AutoConfigureMockMvc
                @DisplayName("%sController测试")
                class %sControllerTest {

                    @Autowired
                    private MockMvc mockMvc;

                    @Test
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试分页查询")
                    void testGetPage() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .param("current", "1")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                    }
                }
                """, basePackage, className, className, className, lowerCamelCaseName, kebabCaseName);
    }

    private String generateServiceTestCode(Map<String, Object> dataModel) {
        String basePackage = (String) dataModel.get("basePackage");
        String className = (String) dataModel.get("className");
        String lowerCamelCaseName = (String) dataModel.get("lowerCamelCaseName");

        return String.format("""
                package %s.service;

                import org.junit.jupiter.api.DisplayName;
                import org.junit.jupiter.api.Test;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.context.SpringBootTest;

                import static org.assertj.core.api.Assertions.assertThat;

                /**
                 * %sService 测试类
                 */
                @SpringBootTest
                @DisplayName("%sService测试")
                class %sServiceTest {

                    @Autowired
                    private %sService %sService;

                    @Test
                    @DisplayName("测试查询列表")
                    void testList() {
                        assertThat(%sService).isNotNull();
                    }
                }
                """, basePackage, className, className, className, className, lowerCamelCaseName, lowerCamelCaseName);
    }

    // ==================== 辅助方法 ====================

    /**
     * 验证表名格式（防止SQL注入和无效输入）
     */
    private void validateTableName(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new GeneratorException("表名不能为空");
        }
        // 表名只允许字母、数字和下划线
        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new GeneratorException("表名格式无效，只允许字母、数字和下划线");
        }
        // 表名长度限制
        if (tableName.length() > 64) {
            throw new GeneratorException("表名长度不能超过64个字符");
        }
    }

    private String getBasePackage(GenerateOptions options) {
        if (options.getModuleName() != null && !options.getModuleName().isEmpty()) {
            return config.getBasePackage() + "." + options.getModuleName();
        }
        return config.getBasePackage();
    }

    private String getAuthor(GenerateOptions options) {
        return options.getAuthor() != null ? options.getAuthor() : config.getAuthor();
    }

    private boolean getEnableSwagger(GenerateOptions options) {
        return options.getEnableSwagger() != null ? options.getEnableSwagger() : config.isEnableSwagger();
    }

    private boolean getEnableLombok(GenerateOptions options) {
        return options.getEnableLombok() != null ? options.getEnableLombok() : config.isEnableLombok();
    }
}
