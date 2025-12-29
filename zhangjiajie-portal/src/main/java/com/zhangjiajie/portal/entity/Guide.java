package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻略实体
 */
@Data
@TableName("portal_guide")
public class Guide {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 内容（富文本）
     */
    private String content;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 类型：1攻略 2游记
     */
    private Integer guideType;

    /**
     * 关联景点ID（逗号分隔）
     */
    private String scenicIds;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 是否官方
     */
    private Integer isOfficial;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

    /**
     * 状态：0待审核 1已发布 2已下架
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
     * 类型名称
     */
    public String getGuideTypeName() {
        if (guideType == null) return "";
        return switch (guideType) {
            case 1 -> "攻略";
            case 2 -> "游记";
            default -> "";
        };
    }

    /**
     * 状态名称
     */
    public String getStatusName() {
        if (status == null) return "";
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "已发布";
            case 2 -> "已下架";
            default -> "";
        };
    }
}
