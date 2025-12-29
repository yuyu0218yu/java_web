package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 轮播图实体
 */
@Data
@TableName("portal_banner")
public class Banner {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 跳转链接
     */
    private String linkUrl;

    /**
     * 链接类型：1景点 2攻略 3外链
     */
    private Integer linkType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0禁用 1启用
     */
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
     * 删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 链接类型名称
     */
    public String getLinkTypeName() {
        if (linkType == null) return "";
        return switch (linkType) {
            case 1 -> "景点";
            case 2 -> "攻略";
            case 3 -> "外链";
            default -> "";
        };
    }
}
