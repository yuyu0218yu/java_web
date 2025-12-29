package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论实体
 */
@Data
@TableName("portal_comment")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 目标类型：1景点 2攻略
     */
    private Integer targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片（JSON数组）
     */
    private String images;

    /**
     * 评分1-5
     */
    private Integer rating;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 状态：0待审核 1已发布
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
     * 子评论（非数据库字段）
     */
    @TableField(exist = false)
    private List<Comment> replies;

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
