package com.yushuang.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yushuang.demo.common.enums.CodeEnum;
import com.yushuang.demo.common.enums.EnableStatus;
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
     * 资源类型：1-菜单，2-按钮，3-接口
     */
    @TableField("resource_type")
    private Integer resourceType;

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
    public enum PermissionType implements CodeEnum<Integer> {
        MENU(1, "菜单"),
        BUTTON(2, "按钮"),
        API(3, "接口");

        private final Integer code;
        private final String desc;

        PermissionType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static PermissionType getByCode(Integer code) {
            return CodeEnum.getByCode(PermissionType.class, code, MENU);
        }
    }

    /**
     * 状态枚举
     * 使用通用启用状态枚举，保留此内部类以维持向后兼容
     */
    public enum Status implements CodeEnum<Integer> {
        DISABLED(EnableStatus.DISABLED.getCode(), EnableStatus.DISABLED.getDesc()),
        ENABLED(EnableStatus.ENABLED.getCode(), EnableStatus.ENABLED.getDesc());

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        public static Status getByCode(Integer code) {
            return CodeEnum.getByCode(Status.class, code, DISABLED);
        }
    }
}