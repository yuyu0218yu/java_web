package com.zhangjiajie.generator.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * VO/BO分层对象生成器
 * 生成业务对象(BO)和视图对象(VO)，实现清晰的分层架构
 *
 * 分层说明：
 *   Entity  → 数据库实体，与表结构一一对应
 *   BO      → 业务对象，用于Service层业务逻辑处理
 *   VO      → 视图对象，用于Controller层数据展示
 *   DTO     → 数据传输对象，用于接收请求参数
 *
 * 数据流向：
 *   请求：DTO → BO → Entity → 数据库
 *   响应：数据库 → Entity → BO → VO → 前端
 *
 * 使用方法：
 *   VoBoGenerator.generate("User", "用户");
 *   VoBoGenerator.generateFromColumns("User", "用户", columns);
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class VoBoGenerator {

    public static void main(String[] args) {
        // 示例：生成User实体的VO/BO对象
        generate("User", "用户");
    }

    /**
     * 生成完整的VO/BO分层对象
     */
    public static void generate(String entityName, String entityCnName) {
        try {
            generateBO(entityName, entityCnName);
            generateVO(entityName, entityCnName);
            generateDetailVO(entityName, entityCnName);
            generateListVO(entityName, entityCnName);
            generateExportVO(entityName, entityCnName);
            generateVoBoConverter(entityName, entityCnName);

            printSuccess("VO/BO分层对象生成", entityName,
                    entityName + "BO.java",
                    entityName + "VO.java",
                    entityName + "DetailVO.java",
                    entityName + "ListVO.java",
                    entityName + "ExportVO.java",
                    entityName + "VoBoConverter.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 根据数据库列生成VO/BO对象
     */
    public static void generateFromColumns(String entityName, String entityCnName,
                                           List<Map<String, Object>> columns) {
        try {
            generateBOWithColumns(entityName, entityCnName, columns);
            generateVOWithColumns(entityName, entityCnName, columns);
            generateDetailVO(entityName, entityCnName);
            generateListVOWithColumns(entityName, entityCnName, columns);
            generateExportVOWithColumns(entityName, entityCnName, columns);
            generateVoBoConverter(entityName, entityCnName);

            printSuccess("VO/BO分层对象生成（智能）", entityName,
                    entityName + "BO.java",
                    entityName + "VO.java",
                    entityName + "DetailVO.java",
                    entityName + "ListVO.java",
                    entityName + "ExportVO.java",
                    entityName + "VoBoConverter.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成BO
     */
    public static void generateBOOnly(String entityName, String entityCnName) {
        try {
            generateBO(entityName, entityCnName);
            printSuccess("BO生成", entityName, entityName + "BO.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成VO
     */
    public static void generateVOOnly(String entityName, String entityCnName) {
        try {
            generateVO(entityName, entityCnName);
            generateDetailVO(entityName, entityCnName);
            generateListVO(entityName, entityCnName);
            printSuccess("VO生成", entityName,
                    entityName + "VO.java",
                    entityName + "DetailVO.java",
                    entityName + "ListVO.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    // ==================== BO 生成 ====================

    /**
     * 生成业务对象 (BO)
     */
    private static void generateBO(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.bo;

                import lombok.Data;
                import lombok.experimental.Accessors;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s业务对象 (Business Object)
                 * 用于Service层业务逻辑处理，可包含业务计算字段
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Accessors(chain = true)
                public class %sBO implements Serializable {

                    private static final long serialVersionUID = 1L;

                    // ==================== 基础字段 ====================

                    /** 主键ID */
                    private Long id;

                    /** 名称 */
                    private String name;

                    /** 编码 */
                    private String code;

                    /** 描述 */
                    private String description;

                    /** 状态 (0-禁用, 1-启用) */
                    private Integer status;

                    /** 排序 */
                    private Integer sort;

                    // ==================== 业务扩展字段 ====================

                    /** 状态名称（业务计算字段） */
                    private String statusName;

                    /** 是否可编辑（业务权限字段） */
                    private Boolean editable;

                    /** 是否可删除（业务权限字段） */
                    private Boolean deletable;

                    // ==================== 关联对象 ====================

                    /** 创建人信息 */
                    private String createByName;

                    /** 更新人信息 */
                    private String updateByName;

                    // ==================== 系统字段 ====================

                    /** 是否删除 */
                    private Boolean deleted;

                    /** 创建时间 */
                    private LocalDateTime createTime;

                    /** 更新时间 */
                    private LocalDateTime updateTime;

                    /** 创建人 */
                    private String createBy;

                    /** 更新人 */
                    private String updateBy;

                    // ==================== 业务方法 ====================

                    /**
                     * 判断是否启用
                     */
                    public boolean isEnabled() {
                        return Integer.valueOf(1).equals(this.status);
                    }

                    /**
                     * 获取状态名称
                     */
                    public String getStatusName() {
                        if (this.statusName != null) {
                            return this.statusName;
                        }
                        return Integer.valueOf(1).equals(this.status) ? "启用" : "禁用";
                    }

                    // TODO: 根据实际业务需求添加更多字段和方法
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(), entityName
        );

        writeToFile(getMainJavaPath(), "bo", entityName + "BO.java", content);
    }

    /**
     * 根据列信息生成BO
     */
    private static void generateBOWithColumns(String entityName, String entityCnName,
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

            // 收集import
            if ("LocalDateTime".equals(javaType)) hasLocalDateTime = true;
            if ("BigDecimal".equals(javaType)) hasBigDecimal = true;
            if ("LocalDate".equals(javaType)) hasLocalDate = true;

            fields.append(String.format("""

                    /** %s */
                    private %s %s;
                """, comment != null ? comment : fieldName, javaType, fieldName));
        }

        // 添加业务扩展字段
        fields.append("""

                // ==================== 业务扩展字段 ====================

                /** 状态名称（业务计算字段） */
                private String statusName;

                /** 是否可编辑 */
                private Boolean editable;

                /** 是否可删除 */
                private Boolean deletable;
            """);

        // 构建imports
        if (hasLocalDateTime) imports.append("import java.time.LocalDateTime;\n");
        if (hasLocalDate) imports.append("import java.time.LocalDate;\n");
        if (hasBigDecimal) imports.append("import java.math.BigDecimal;\n");

        String content = String.format("""
                package %s.bo;

                import lombok.Data;
                import lombok.experimental.Accessors;

                import java.io.Serializable;
                %s
                /**
                 * %s业务对象 (Business Object)
                 * 根据数据库表结构自动生成
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Accessors(chain = true)
                public class %sBO implements Serializable {

                    private static final long serialVersionUID = 1L;
                %s
                }
                """,
                basePackage, imports.toString(),
                entityCnName, getAuthor(), getCurrentDate(),
                entityName, fields.toString()
        );

        writeToFile(getMainJavaPath(), "bo", entityName + "BO.java", content);
    }

    // ==================== VO 生成 ====================

    /**
     * 生成视图对象 (VO) - 基础版
     */
    private static void generateVO(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.vo;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s视图对象 (View Object)
                 * 用于Controller层数据展示，对外暴露的数据结构
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "%s信息")
                public class %sVO implements Serializable {

                    private static final long serialVersionUID = 1L;

                    @Schema(description = "ID")
                    private Long id;

                    @Schema(description = "名称")
                    private String name;

                    @Schema(description = "编码")
                    private String code;

                    @Schema(description = "描述")
                    private String description;

                    @Schema(description = "状态 (0-禁用, 1-启用)")
                    private Integer status;

                    @Schema(description = "状态名称")
                    private String statusName;

                    @Schema(description = "排序")
                    private Integer sort;

                    @Schema(description = "创建时间")
                    private LocalDateTime createTime;

                    @Schema(description = "更新时间")
                    private LocalDateTime updateTime;

                    // TODO: 根据实际展示需求添加更多字段
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "VO.java", content);
    }

    /**
     * 根据列信息生成VO
     */
    private static void generateVOWithColumns(String entityName, String entityCnName,
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

            // 跳过敏感字段
            if (isSensitiveField(fieldName)) continue;

            // 收集import
            if ("LocalDateTime".equals(javaType)) hasLocalDateTime = true;
            if ("BigDecimal".equals(javaType)) hasBigDecimal = true;
            if ("LocalDate".equals(javaType)) hasLocalDate = true;

            fields.append(String.format("""

                    @Schema(description = "%s")
                    private %s %s;
                """, comment != null ? comment : fieldName, javaType, fieldName));
        }

        // 构建imports
        if (hasLocalDateTime) imports.append("import java.time.LocalDateTime;\n");
        if (hasLocalDate) imports.append("import java.time.LocalDate;\n");
        if (hasBigDecimal) imports.append("import java.math.BigDecimal;\n");

        String content = String.format("""
                package %s.vo;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;
                %s
                /**
                 * %s视图对象 (View Object)
                 * 根据数据库表结构自动生成
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "%s信息")
                public class %sVO implements Serializable {

                    private static final long serialVersionUID = 1L;
                %s
                }
                """,
                basePackage, imports.toString(),
                entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName, fields.toString()
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "VO.java", content);
    }

    /**
     * 生成详情VO
     */
    private static void generateDetailVO(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.vo;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;
                import lombok.EqualsAndHashCode;

                import java.io.Serializable;
                import java.time.LocalDateTime;
                import java.util.List;

                /**
                 * %s详情视图对象
                 * 用于展示单条记录的完整信息，包含关联数据
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @EqualsAndHashCode(callSuper = true)
                @Schema(description = "%s详情")
                public class %sDetailVO extends %sVO implements Serializable {

                    private static final long serialVersionUID = 1L;

                    // ==================== 扩展字段 ====================

                    @Schema(description = "创建人名称")
                    private String createByName;

                    @Schema(description = "更新人名称")
                    private String updateByName;

                    // ==================== 关联对象 ====================

                    // @Schema(description = "关联的子对象列表")
                    // private List<RelatedVO> relatedList;

                    // ==================== 操作权限 ====================

                    @Schema(description = "是否可编辑")
                    private Boolean editable;

                    @Schema(description = "是否可删除")
                    private Boolean deletable;

                    // TODO: 根据实际详情页需求添加更多字段
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName, entityName
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "DetailVO.java", content);
    }

    /**
     * 生成列表VO
     */
    private static void generateListVO(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.vo;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s列表视图对象
                 * 用于列表展示，只包含必要字段，优化传输性能
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "%s列表项")
                public class %sListVO implements Serializable {

                    private static final long serialVersionUID = 1L;

                    @Schema(description = "ID")
                    private Long id;

                    @Schema(description = "名称")
                    private String name;

                    @Schema(description = "编码")
                    private String code;

                    @Schema(description = "状态")
                    private Integer status;

                    @Schema(description = "状态名称")
                    private String statusName;

                    @Schema(description = "排序")
                    private Integer sort;

                    @Schema(description = "创建时间")
                    private LocalDateTime createTime;

                    // TODO: 只保留列表必要的字段
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "ListVO.java", content);
    }

    /**
     * 根据列信息生成列表VO
     */
    private static void generateListVOWithColumns(String entityName, String entityCnName,
                                                   List<Map<String, Object>> columns) throws IOException {
        String basePackage = getBasePackage();
        StringBuilder fields = new StringBuilder();

        // 只取前几个重要字段
        int count = 0;
        for (Map<String, Object> column : columns) {
            if (count >= 8) break; // 列表只保留8个字段

            String columnName = (String) column.get("columnName");
            String dataType = (String) column.get("dataType");
            String comment = (String) column.get("columnComment");
            String fieldName = columnNameToFieldName(columnName);
            String javaType = dbTypeToJavaType(dataType);

            // 跳过敏感和大字段
            if (isSensitiveField(fieldName) || isLargeField(dataType)) continue;

            fields.append(String.format("""

                    @Schema(description = "%s")
                    private %s %s;
                """, comment != null ? comment : fieldName, javaType, fieldName));

            count++;
        }

        String content = String.format("""
                package %s.vo;

                import io.swagger.v3.oas.annotations.media.Schema;
                import lombok.Data;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s列表视图对象
                 * 根据数据库表结构自动生成，只包含列表必要字段
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                @Schema(description = "%s列表项")
                public class %sListVO implements Serializable {

                    private static final long serialVersionUID = 1L;
                %s
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName, fields.toString()
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "ListVO.java", content);
    }

    /**
     * 生成导出VO
     */
    private static void generateExportVO(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.vo;

                import com.alibaba.excel.annotation.ExcelProperty;
                import com.alibaba.excel.annotation.format.DateTimeFormat;
                import com.alibaba.excel.annotation.write.style.ColumnWidth;
                import lombok.Data;

                import java.io.Serializable;
                import java.time.LocalDateTime;

                /**
                 * %s导出视图对象
                 * 用于Excel导出，配合EasyExcel使用
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                public class %sExportVO implements Serializable {

                    private static final long serialVersionUID = 1L;

                    @ExcelProperty("ID")
                    @ColumnWidth(15)
                    private Long id;

                    @ExcelProperty("名称")
                    @ColumnWidth(20)
                    private String name;

                    @ExcelProperty("编码")
                    @ColumnWidth(15)
                    private String code;

                    @ExcelProperty("描述")
                    @ColumnWidth(30)
                    private String description;

                    @ExcelProperty("状态")
                    @ColumnWidth(10)
                    private String statusName;

                    @ExcelProperty("排序")
                    @ColumnWidth(10)
                    private Integer sort;

                    @ExcelProperty("创建时间")
                    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
                    @ColumnWidth(20)
                    private LocalDateTime createTime;

                    @ExcelProperty("更新时间")
                    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
                    @ColumnWidth(20)
                    private LocalDateTime updateTime;

                    // TODO: 根据导出需求调整字段
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(), entityName
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "ExportVO.java", content);
    }

    /**
     * 根据列信息生成导出VO
     */
    private static void generateExportVOWithColumns(String entityName, String entityCnName,
                                                     List<Map<String, Object>> columns) throws IOException {
        String basePackage = getBasePackage();
        StringBuilder fields = new StringBuilder();

        for (Map<String, Object> column : columns) {
            String columnName = (String) column.get("columnName");
            String dataType = (String) column.get("dataType");
            String comment = (String) column.get("columnComment");
            String fieldName = columnNameToFieldName(columnName);
            String javaType = dbTypeToJavaType(dataType);

            // 跳过敏感字段和deleted字段
            if (isSensitiveField(fieldName) || "deleted".equals(fieldName)) continue;

            int width = calculateColumnWidth(javaType, fieldName);
            String dateFormat = "";
            if ("LocalDateTime".equals(javaType)) {
                dateFormat = "\n    @DateTimeFormat(\"yyyy-MM-dd HH:mm:ss\")";
            } else if ("LocalDate".equals(javaType)) {
                dateFormat = "\n    @DateTimeFormat(\"yyyy-MM-dd\")";
            }

            fields.append(String.format("""

                    @ExcelProperty("%s")%s
                    @ColumnWidth(%d)
                    private %s %s;
                """, comment != null ? comment : fieldName, dateFormat, width, javaType, fieldName));
        }

        String content = String.format("""
                package %s.vo;

                import com.alibaba.excel.annotation.ExcelProperty;
                import com.alibaba.excel.annotation.format.DateTimeFormat;
                import com.alibaba.excel.annotation.write.style.ColumnWidth;
                import lombok.Data;

                import java.io.Serializable;
                import java.math.BigDecimal;
                import java.time.LocalDate;
                import java.time.LocalDateTime;

                /**
                 * %s导出视图对象
                 * 根据数据库表结构自动生成
                 *
                 * @author %s
                 * @since %s
                 */
                @Data
                public class %sExportVO implements Serializable {

                    private static final long serialVersionUID = 1L;
                %s
                }
                """,
                basePackage, entityCnName, getAuthor(), getCurrentDate(),
                entityName, fields.toString()
        );

        writeToFile(getMainJavaPath(), "vo", entityName + "ExportVO.java", content);
    }

    // ==================== 转换器生成 ====================

    /**
     * 生成VO/BO转换器
     */
    private static void generateVoBoConverter(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();

        String content = String.format("""
                package %s.converter;

                import %s.bo.%sBO;
                import %s.entity.%s;
                import %s.vo.%sVO;
                import %s.vo.%sDetailVO;
                import %s.vo.%sListVO;
                import %s.vo.%sExportVO;
                import org.springframework.beans.BeanUtils;

                import java.util.ArrayList;
                import java.util.List;
                import java.util.stream.Collectors;

                /**
                 * %s VO/BO 转换器
                 * 实现 Entity、BO、VO 之间的相互转换
                 *
                 * 转换规则：
                 *   Entity ↔ BO：业务层使用
                 *   BO → VO：展示层使用
                 *   Entity → VO：简单场景直接转换
                 *
                 * @author %s
                 * @since %s
                 */
                public final class %sVoBoConverter {

                    private %sVoBoConverter() {
                    }

                    // ==================== Entity ↔ BO ====================

                    /**
                     * Entity 转 BO
                     */
                    public static %sBO toBO(%s entity) {
                        if (entity == null) {
                            return null;
                        }
                        %sBO bo = new %sBO();
                        BeanUtils.copyProperties(entity, bo);
                        // 设置业务扩展字段
                        bo.setStatusName(entity.getStatus() != null && entity.getStatus() == 1 ? "启用" : "禁用");
                        bo.setEditable(true);  // 默认可编辑，实际根据业务权限判断
                        bo.setDeletable(true); // 默认可删除，实际根据业务权限判断
                        return bo;
                    }

                    /**
                     * BO 转 Entity
                     */
                    public static %s toEntity(%sBO bo) {
                        if (bo == null) {
                            return null;
                        }
                        %s entity = new %s();
                        BeanUtils.copyProperties(bo, entity);
                        return entity;
                    }

                    /**
                     * Entity列表 转 BO列表
                     */
                    public static List<%sBO> toBOList(List<%s> entities) {
                        if (entities == null) {
                            return new ArrayList<>();
                        }
                        return entities.stream()
                                .map(%sVoBoConverter::toBO)
                                .collect(Collectors.toList());
                    }

                    // ==================== BO → VO ====================

                    /**
                     * BO 转 VO
                     */
                    public static %sVO toVO(%sBO bo) {
                        if (bo == null) {
                            return null;
                        }
                        %sVO vo = new %sVO();
                        BeanUtils.copyProperties(bo, vo);
                        return vo;
                    }

                    /**
                     * BO 转 DetailVO
                     */
                    public static %sDetailVO toDetailVO(%sBO bo) {
                        if (bo == null) {
                            return null;
                        }
                        %sDetailVO vo = new %sDetailVO();
                        BeanUtils.copyProperties(bo, vo);
                        return vo;
                    }

                    /**
                     * BO 转 ListVO
                     */
                    public static %sListVO toListVO(%sBO bo) {
                        if (bo == null) {
                            return null;
                        }
                        %sListVO vo = new %sListVO();
                        BeanUtils.copyProperties(bo, vo);
                        return vo;
                    }

                    /**
                     * BO 转 ExportVO
                     */
                    public static %sExportVO toExportVO(%sBO bo) {
                        if (bo == null) {
                            return null;
                        }
                        %sExportVO vo = new %sExportVO();
                        BeanUtils.copyProperties(bo, vo);
                        return vo;
                    }

                    /**
                     * BO列表 转 VO列表
                     */
                    public static List<%sVO> toVOList(List<%sBO> bos) {
                        if (bos == null) {
                            return new ArrayList<>();
                        }
                        return bos.stream()
                                .map(%sVoBoConverter::toVO)
                                .collect(Collectors.toList());
                    }

                    /**
                     * BO列表 转 ListVO列表
                     */
                    public static List<%sListVO> toListVOList(List<%sBO> bos) {
                        if (bos == null) {
                            return new ArrayList<>();
                        }
                        return bos.stream()
                                .map(%sVoBoConverter::toListVO)
                                .collect(Collectors.toList());
                    }

                    /**
                     * BO列表 转 ExportVO列表
                     */
                    public static List<%sExportVO> toExportVOList(List<%sBO> bos) {
                        if (bos == null) {
                            return new ArrayList<>();
                        }
                        return bos.stream()
                                .map(%sVoBoConverter::toExportVO)
                                .collect(Collectors.toList());
                    }

                    // ==================== Entity → VO (简化场景) ====================

                    /**
                     * Entity 直接转 VO（简单场景使用）
                     */
                    public static %sVO entityToVO(%s entity) {
                        return toVO(toBO(entity));
                    }

                    /**
                     * Entity列表 直接转 VO列表
                     */
                    public static List<%sVO> entityToVOList(List<%s> entities) {
                        return toVOList(toBOList(entities));
                    }

                    /**
                     * Entity列表 直接转 ListVO列表
                     */
                    public static List<%sListVO> entityToListVOList(List<%s> entities) {
                        return toListVOList(toBOList(entities));
                    }
                }
                """,
                basePackage,
                basePackage, entityName,
                basePackage, entityName,
                basePackage, entityName,
                basePackage, entityName,
                basePackage, entityName,
                basePackage, entityName,
                entityCnName, getAuthor(), getCurrentDate(),
                entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName, entityName,
                entityName, entityName,
                entityName, entityName,
                entityName, entityName
        );

        writeToFile(getMainJavaPath(), "converter", entityName + "VoBoConverter.java", content);
    }

    // ==================== 工具方法 ====================

    /**
     * 判断是否敏感字段
     */
    private static boolean isSensitiveField(String fieldName) {
        return "password".equalsIgnoreCase(fieldName) ||
               "salt".equalsIgnoreCase(fieldName) ||
               "secret".equalsIgnoreCase(fieldName) ||
               "token".equalsIgnoreCase(fieldName) ||
               fieldName.toLowerCase().contains("password") ||
               fieldName.toLowerCase().contains("secret");
    }

    /**
     * 判断是否大字段
     */
    private static boolean isLargeField(String dataType) {
        if (dataType == null) return false;
        String lower = dataType.toLowerCase();
        return lower.contains("text") || lower.contains("blob") || lower.contains("clob");
    }

    /**
     * 计算Excel列宽
     */
    private static int calculateColumnWidth(String javaType, String fieldName) {
        if ("LocalDateTime".equals(javaType)) return 20;
        if ("LocalDate".equals(javaType)) return 12;
        if ("id".equalsIgnoreCase(fieldName)) return 15;
        if (fieldName.toLowerCase().contains("name")) return 20;
        if (fieldName.toLowerCase().contains("description")) return 30;
        if (fieldName.toLowerCase().contains("remark")) return 30;
        return 15;
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
