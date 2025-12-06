package com.zhangjiajie.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 代码生成业务表
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
@TableName("gen_table")
public class GenTable {

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Long tableId;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 关联子表的表名 */
    private String subTableName;

    /** 子表关联的外键名 */
    private String subTableFkName;

    /** 实体类名称 */
    private String className;

    /** 使用的模板（crud单表 tree树表 sub主子表） */
    private String tplCategory;

    /** 前端模板类型（element-ui element-plus） */
    private String tplWebType;

    /** 生成包路径 */
    private String packageName;

    /** 生成模块名 */
    private String moduleName;

    /** 生成业务名 */
    private String businessName;

    /** 生成功能名 */
    private String functionName;

    /** 生成功能作者 */
    private String functionAuthor;

    /** 生成代码方式（0zip压缩包 1自定义路径） */
    private String genType;

    /** 生成路径（不填默认项目路径） */
    private String genPath;

    /** 其它生成选项（JSON格式） */
    private String options;

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

    /** 备注 */
    private String remark;

    /** 表字段列表（非数据库字段） */
    @TableField(exist = false)
    private List<GenTableColumn> columns;

    /** 主键信息（非数据库字段） */
    @TableField(exist = false)
    private GenTableColumn pkColumn;

    /** 子表信息（非数据库字段） */
    @TableField(exist = false)
    private GenTable subTable;

    // ==================== 常量定义 ====================

    /** 单表（增删改查） */
    public static final String TPL_CRUD = "crud";

    /** 树表（增删改查） */
    public static final String TPL_TREE = "tree";

    /** 主子表（增删改查） */
    public static final String TPL_SUB = "sub";

    /** zip压缩包 */
    public static final String GEN_TYPE_ZIP = "0";

    /** 自定义路径 */
    public static final String GEN_TYPE_CUSTOM = "1";

    // ==================== 便捷方法 ====================

    /**
     * 是否是单表
     */
    public boolean isCrud() {
        return TPL_CRUD.equals(this.tplCategory);
    }

    /**
     * 是否是树表
     */
    public boolean isTree() {
        return TPL_TREE.equals(this.tplCategory);
    }

    /**
     * 是否是主子表
     */
    public boolean isSub() {
        return TPL_SUB.equals(this.tplCategory);
    }
}
