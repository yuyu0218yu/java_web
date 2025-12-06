package com.zhangjiajie.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 代码生成业务表字段
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
@TableName("gen_table_column")
public class GenTableColumn {

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long columnId;

    /** 归属表编号 */
    private Long tableId;

    /** 列名称 */
    private String columnName;

    /** 列描述 */
    private String columnComment;

    /** 列类型 */
    private String columnType;

    /** JAVA类型 */
    private String javaType;

    /** JAVA字段名 */
    private String javaField;

    /** 是否主键（1是） */
    private String isPk;

    /** 是否自增（1是） */
    private String isIncrement;

    /** 是否必填（1是） */
    private String isRequired;

    /** 是否为插入字段（1是） */
    private String isInsert;

    /** 是否编辑字段（1是） */
    private String isEdit;

    /** 是否列表字段（1是） */
    private String isList;

    /** 是否查询字段（1是） */
    private String isQuery;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    private String queryType;

    /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传、upload文件上传、editor富文本控件） */
    private String htmlType;

    /** 字典类型 */
    private String dictType;

    /** 排序 */
    private Integer sort;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ==================== 常量定义 ====================

    /** 是 */
    public static final String YES = "1";

    /** 否 */
    public static final String NO = "0";

    /** 查询方式：等于 */
    public static final String QUERY_EQ = "EQ";

    /** 查询方式：不等于 */
    public static final String QUERY_NE = "NE";

    /** 查询方式：大于 */
    public static final String QUERY_GT = "GT";

    /** 查询方式：大于等于 */
    public static final String QUERY_GTE = "GTE";

    /** 查询方式：小于 */
    public static final String QUERY_LT = "LT";

    /** 查询方式：小于等于 */
    public static final String QUERY_LTE = "LTE";

    /** 查询方式：模糊 */
    public static final String QUERY_LIKE = "LIKE";

    /** 查询方式：范围 */
    public static final String QUERY_BETWEEN = "BETWEEN";

    /** 显示类型：文本框 */
    public static final String HTML_INPUT = "input";

    /** 显示类型：文本域 */
    public static final String HTML_TEXTAREA = "textarea";

    /** 显示类型：下拉框 */
    public static final String HTML_SELECT = "select";

    /** 显示类型：单选框 */
    public static final String HTML_RADIO = "radio";

    /** 显示类型：复选框 */
    public static final String HTML_CHECKBOX = "checkbox";

    /** 显示类型：日期控件 */
    public static final String HTML_DATETIME = "datetime";

    /** 显示类型：图片上传 */
    public static final String HTML_IMAGE = "imageUpload";

    /** 显示类型：文件上传 */
    public static final String HTML_UPLOAD = "fileUpload";

    /** 显示类型：富文本控件 */
    public static final String HTML_EDITOR = "editor";

    // ==================== 便捷方法 ====================

    /**
     * 是否是主键
     */
    public boolean isPk() {
        return YES.equals(this.isPk);
    }

    /**
     * 是否是自增
     */
    public boolean isIncrement() {
        return YES.equals(this.isIncrement);
    }

    /**
     * 是否是必填
     */
    public boolean isRequired() {
        return YES.equals(this.isRequired);
    }

    /**
     * 是否是插入字段
     */
    public boolean isInsert() {
        return YES.equals(this.isInsert);
    }

    /**
     * 是否是编辑字段
     */
    public boolean isEdit() {
        return YES.equals(this.isEdit);
    }

    /**
     * 是否是列表字段
     */
    public boolean isList() {
        return YES.equals(this.isList);
    }

    /**
     * 是否是查询字段
     */
    public boolean isQuery() {
        return YES.equals(this.isQuery);
    }

    /**
     * 是否是超级字段（创建时间、更新时间、创建人、更新人等）
     */
    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public static boolean isSuperColumn(String javaField) {
        return "id".equals(javaField) ||
               "createBy".equals(javaField) ||
               "createTime".equals(javaField) ||
               "updateBy".equals(javaField) ||
               "updateTime".equals(javaField) ||
               "deleted".equals(javaField) ||
               "remark".equals(javaField);
    }

    /**
     * 是否是基础实体字段
     */
    public boolean isBaseEntity() {
        return isBaseEntity(this.javaField);
    }

    public static boolean isBaseEntity(String javaField) {
        return "createBy".equals(javaField) ||
               "createTime".equals(javaField) ||
               "updateBy".equals(javaField) ||
               "updateTime".equals(javaField);
    }

    /**
     * 获取首字母大写的Java字段名
     */
    public String getCapJavaField() {
        if (javaField == null || javaField.isEmpty()) {
            return javaField;
        }
        return javaField.substring(0, 1).toUpperCase() + javaField.substring(1);
    }
}
