package com.zhangjiajie.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 结构实体类
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
@TableName("sys_dept")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 结构ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父结构ID (0=根节点)
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 祖先节点列表 (如: 0,1,2)
     */
    @TableField("ancestors")
    private String ancestors;

    /**
     * 结构名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 结构编码
     */
    @TableField("dept_code")
    private String deptCode;

    /**
     * 负责人
     */
    @TableField("leader")
    private String leader;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 子结构列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Dept> children;
}
