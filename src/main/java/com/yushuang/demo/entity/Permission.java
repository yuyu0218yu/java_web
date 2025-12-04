package com.yushuang.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限实体类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    @TableField("permission_type")
    private Integer permissionType;

    /**
     * 父级权限ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

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
     * 逻辑删除：0-未删除，1-已删除
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
     * 权限类型枚举
     */
    public enum PermissionType {
        MENU(1, "菜单"),
        BUTTON(2, "按钮"),
        API(3, "接口");

        private final Integer code;
        private final String desc;

        PermissionType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static PermissionType getByCode(Integer code) {
            for (PermissionType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return MENU;
        }
    }

    /**
     * 状态枚举
     */
    public enum Status {
        DISABLED(0, "禁用"),
        ENABLED(1, "启用");

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static Status getByCode(Integer code) {
            for (Status status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return DISABLED;
        }
    }
}