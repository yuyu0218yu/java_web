package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体
 */
@Data
@TableName("portal_favorite")
public class Favorite {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 目标类型：1景点 2攻略
     */
    private Integer targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 目标名称（非数据库字段）
     */
    @TableField(exist = false)
    private String targetName;

    /**
     * 目标封面（非数据库字段）
     */
    @TableField(exist = false)
    private String targetCover;

    /**
     * 目标类型名称
     */
    public String getTargetTypeName() {
        if (targetType == null) return "";
        return switch (targetType) {
            case 1 -> "景点";
            case 2 -> "攻略";
            default -> "";
        };
    }
}
