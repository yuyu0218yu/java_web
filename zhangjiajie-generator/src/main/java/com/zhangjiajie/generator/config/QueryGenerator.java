package com.zhangjiajie.generator.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * Query查询对象生成器
 * 生成带条件的查询对象，支持多种查询方式
 *
 * 使用方法：
 * 方式一：使用默认配置
 *   QueryGenerator.generate("User", "用户");
 *
 * 方式二：根据数据库表列信息生成
 *   List<Map<String, Object>> columns = ...;
 *   QueryGenerator.generateFromColumns("User", "用户", columns);
 *
 * 生成的查询对象支持：
 * - like 模糊查询（String类型字段）
 * - eq 精确查询（Integer/Long/枚举类型）
 * - ge/le 范围查询（时间/数值类型）
 * - in 多值查询（状态、类型等）
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class QueryGenerator {

    public static void main(String[] args) {
        // 示例：生成User实体的Query查询对象
        generate("User", "用户");
    }

    /**
     * 生成Query查询对象（基础模板）
     *
     * @param entityName   实体类名（如：User, Product）
     * @param entityCnName 实体中文名（如：用户、产品）
     */
    public static void generate(String entityName, String entityCnName) {
        try {
            generateQuery(entityName, entityCnName);
            generateQueryWrapper(entityName, entityCnName);

            printSuccess("Query查询对象生成", entityName,
                    entityName + "Query.java",
                    entityName + "QueryWrapper.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成Query对象
     */
    public static void generateQueryOnly(String entityName, String entityCnName) {
        try {
            generateQuery(entityName, entityCnName);
            printSuccess("Query对象生成", entityName, entityName + "Query.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 根据数据库列信息生成Query对象（智能生成字段）
     *
     * @param entityName   实体类名
     * @param entityCnName 实体中文名
     * @param columns      列信息列表
     */
    public static void generateFromColumns(String entityName, String entityCnName,
                                           List<Map<String, Object>> columns) {
        try {
            generateQueryWithColumns(entityName, entityCnName, columns);
            generateQueryWrapper(entityName, entityCnName);

            printSuccess("Query查询对象生成（智能）", entityName,
                    entityName + "Query.java",
                    entityName + "QueryWrapper.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 生成Query查询对象
     */
    private static void generateQuery(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.query;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                import com.zhangjiajie.common.core.PageRequest;

                import java.io.Serializable;
                import java.time.LocalDateTime;
                import java.util.List;

                /**
                 * %s查询条件
                 * 继承PageRequest获得分页和排序能力
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @EqualsAndHashCode(callSuper = true)
                @Schema(description = "%s查询条件")
                public class %sQuery extends PageRequest implements Serializable {

                    private static final long serialVersionUID = 1L;

                    // ==================== 精确查询 (eq) ====================

                    @Schema(description = "ID")
                    private Long id;

                    @Schema(description = "状态 (0-禁用, 1-启用)")
                    private Integer status;

                    // ==================== 模糊查询 (like) ====================

                    @Schema(description = "名称 (模糊查询)")
                    private String nameLike;

                    // ==================== 范围查询 (ge/le) ====================

                    @Schema(description = "创建时间-开始")
                    private LocalDateTime createTimeStart;

                    @Schema(description = "创建时间-结束")
                    private LocalDateTime createTimeEnd;

                    @Schema(description = "更新时间-开始")
                    private LocalDateTime updateTimeStart;

                    @Schema(description = "更新时间-结束")
                    private LocalDateTime updateTimeEnd;

                    // ==================== 多值查询 (in) ====================

                    @Schema(description = "ID列表 (批量查询)")
                    private List<Long> ids;

                    @Schema(description = "状态列表 (批量查询)")
                    private List<Integer> statuses;

                    // ==================== 其他条件 ====================

                    @Schema(description = "是否已删除 (默认false)")
                    private Boolean deleted = false;

                    // TODO: 根据实际业务需求添加更多查询字段
                    // 命名规范：
                    //   - 精确查询：fieldName
                    //   - 模糊查询：fieldNameLike
                    //   - 范围查询：fieldNameStart / fieldNameEnd
                    //   - 多值查询：fieldNames (复数)

                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName
        );

        writeToFile(getMainJavaPath(), "query", entityName + "Query.java", content);
    }

    /**
     * 根据列信息生成Query对象（智能生成）
     */
    private static void generateQueryWithColumns(String entityName, String entityCnName,
                                                  List<Map<String, Object>> columns) throws IOException {
        String basePackage = getBasePackage();
        StringBuilder fields = new StringBuilder();
        StringBuilder imports = new StringBuilder();

        boolean hasLocalDateTime = false;
        boolean hasBigDecimal = false;
        boolean hasLocalDate = false;

        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("columnName");
            String dataType = (String) column.get("dataType");
            String comment = (String) column.get("columnComment");
            String fieldName = columnNameToFieldName(columnName);
            String javaType = dbTypeToJavaType(dataType);

            // 跳过主键和系统字段的直接添加
            if ("id".equals(fieldName) || "deleted".equals(fieldName)) {
                continue;
            }

            // 收集import
            if ("LocalDateTime".equals(javaType)) hasLocalDateTime = true;
            if ("BigDecimal".equals(javaType)) hasBigDecimal = true;
            if ("LocalDate".equals(javaType)) hasLocalDate = true;

            // 根据类型生成不同的查询字段
            switch (javaType) {
                case "String" -> {
                    // 字符串类型：生成模糊查询
                    fields.append(String.format("""

                        @Schema(description = "%s (模糊查询)")
                        private String %sLike;
                    """, comment != null ? comment : fieldName, fieldName));

                    // 如果是特定字段也生成精确查询
                    if (isExactMatchField(fieldName)) {
                        fields.append(String.format("""

                        @Schema(description = "%s (精确查询)")
                        private String %s;
                    """, comment != null ? comment : fieldName, fieldName));
                    }
                }
                case "Integer", "Long" -> {
                    // 数值类型：生成精确查询 + 范围查询
                    fields.append(String.format("""

                        @Schema(description = "%s")
                        private %s %s;

                        @Schema(description = "%s-最小值")
                        private %s %sMin;

                        @Schema(description = "%s-最大值")
                        private %s %sMax;
                    """, comment != null ? comment : fieldName, javaType, fieldName,
                            comment != null ? comment : fieldName, javaType, fieldName,
                            comment != null ? comment : fieldName, javaType, fieldName));
                }
                case "LocalDateTime", "LocalDate" -> {
                    // 时间类型：生成范围查询
                    fields.append(String.format("""

                        @Schema(description = "%s-开始")
                        private %s %sStart;

                        @Schema(description = "%s-结束")
                        private %s %sEnd;
                    """, comment != null ? comment : fieldName, javaType, fieldName,
                            comment != null ? comment : fieldName, javaType, fieldName));
                }
                case "Boolean" -> {
                    // 布尔类型：精确查询
                    fields.append(String.format("""

                        @Schema(description = "%s")
                        private Boolean %s;
                    """, comment != null ? comment : fieldName, fieldName));
                }
                case "BigDecimal" -> {
                    // 金额类型：范围查询
                    fields.append(String.format("""

                        @Schema(description = "%s-最小值")
                        private BigDecimal %sMin;

                        @Schema(description = "%s-最大值")
                        private BigDecimal %sMax;
                    """, comment != null ? comment : fieldName, fieldName,
                            comment != null ? comment : fieldName, fieldName));
                }
                default -> {
                    // 其他类型：精确查询
                    fields.append(String.format("""

                        @Schema(description = "%s")
                        private %s %s;
                    """, comment != null ? comment : fieldName, javaType, fieldName));
                }
            }
        }

        // 构建imports
        if (hasLocalDateTime) imports.append("import java.time.LocalDateTime;\n");
        if (hasLocalDate) imports.append("import java.time.LocalDate;\n");
        if (hasBigDecimal) imports.append("import java.math.BigDecimal;\n");

        String content = String.format("""
                package %s.query;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                import com.zhangjiajie.common.core.PageRequest;

                import java.io.Serializable;
                import java.util.List;
                %s
                /**
                 * %s查询条件
                 * 根据数据库表结构自动生成
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @EqualsAndHashCode(callSuper = true)
                @Schema(description = "%s查询条件")
                public class %sQuery extends PageRequest implements Serializable {

                    private static final long serialVersionUID = 1L;

                    // ==================== 精确查询 ====================

                    @Schema(description = "ID")
                    private Long id;

                    @Schema(description = "ID列表 (批量查询)")
                    private List<Long> ids;
                %s
                    // ==================== 系统字段 ====================

                    @Schema(description = "是否已删除")
                    private Boolean deleted = false;
                }
                """,
                basePackage, imports.toString(),
                entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName, fields.toString()
        );

        writeToFile(getMainJavaPath(), "query", entityName + "Query.java", content);
    }

    /**
     * 生成QueryWrapper构建器
     */
    private static void generateQueryWrapper(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                package %s.query;

                import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
                import %s.entity.%s;
                import org.springframework.util.StringUtils;

                import java.util.List;

                /**
                 * %s查询条件构建器
                 * 将Query对象转换为MyBatis-Plus的LambdaQueryWrapper
                 *
                 * @author %s
                 * @since %s
                 */
                public final class %sQueryWrapper {

                    private %sQueryWrapper() {
                    }

                    /**
                     * 根据查询条件构建LambdaQueryWrapper
                     *
                     * @param query 查询条件
                     * @return LambdaQueryWrapper
                     */
                    public static LambdaQueryWrapper<%s> build(%sQuery query) {
                        LambdaQueryWrapper<%s> wrapper = new LambdaQueryWrapper<>();

                        if (query == null) {
                            return wrapper;
                        }

                        // ==================== 精确查询 ====================
                        wrapper.eq(query.getId() != null, %s::getId, query.getId());
                        wrapper.eq(query.getStatus() != null, %s::getStatus, query.getStatus());
                        wrapper.eq(query.getDeleted() != null, %s::getDeleted, query.getDeleted());

                        // ==================== 模糊查询 ====================
                        wrapper.like(StringUtils.hasText(query.getNameLike()), %s::getName, query.getNameLike());

                        // ==================== 范围查询 ====================
                        wrapper.ge(query.getCreateTimeStart() != null, %s::getCreateTime, query.getCreateTimeStart());
                        wrapper.le(query.getCreateTimeEnd() != null, %s::getCreateTime, query.getCreateTimeEnd());
                        wrapper.ge(query.getUpdateTimeStart() != null, %s::getUpdateTime, query.getUpdateTimeStart());
                        wrapper.le(query.getUpdateTimeEnd() != null, %s::getUpdateTime, query.getUpdateTimeEnd());

                        // ==================== 多值查询 ====================
                        wrapper.in(query.getIds() != null && !query.getIds().isEmpty(), %s::getId, query.getIds());
                        wrapper.in(query.getStatuses() != null && !query.getStatuses().isEmpty(), %s::getStatus, query.getStatuses());

                        // ==================== 排序 ====================
                        // 默认按创建时间降序
                        wrapper.orderByDesc(%s::getCreateTime);

                        return wrapper;
                    }

                    /**
                     * 构建计数查询（不含排序）
                     */
                    public static LambdaQueryWrapper<%s> buildForCount(%sQuery query) {
                        LambdaQueryWrapper<%s> wrapper = build(query);
                        wrapper.orderBy(false, false);  // 清除排序
                        return wrapper;
                    }

                    /**
                     * 构建只查询ID的Wrapper
                     */
                    public static LambdaQueryWrapper<%s> buildForIds(%sQuery query) {
                        LambdaQueryWrapper<%s> wrapper = build(query);
                        wrapper.select(%s::getId);
                        return wrapper;
                    }
                }
                """,
                basePackage, basePackage, entityName,
                entityCnName, getAuthor(), getCurrentDate(),
                entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName, entityName
        );

        writeToFile(getMainJavaPath(), "query", entityName + "QueryWrapper.java", content);
    }

    /**
     * 判断是否需要精确匹配的字段
     */
    private static boolean isExactMatchField(String fieldName) {
        // 这些字段通常需要精确匹配
        return fieldName.endsWith("Code") ||
               fieldName.endsWith("No") ||
               fieldName.endsWith("Number") ||
               fieldName.equals("username") ||
               fieldName.equals("email") ||
               fieldName.equals("phone") ||
               fieldName.equals("mobile");
    }

    /**
     * 列名转字段名
     */
    private static String columnNameToFieldName(String columnName) {
        return toCamelCase(columnName, false);
    }

    /**
     * 数据库类型转Java类型
     */
    private static String dbTypeToJavaType(String dbType) {
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
            default -> "String";
        };
    }
}
