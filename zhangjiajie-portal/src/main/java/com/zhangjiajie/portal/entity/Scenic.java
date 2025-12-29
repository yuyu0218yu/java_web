package com.zhangjiajie.portal.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景点实体
 */
@Data
@TableName("portal_scenic")
public class Scenic {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 景点名称
     */
    private String scenicName;

    /**
     * 景点编码
     */
    private String scenicCode;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 图片集（JSON数组）
     */
    private String images;

    /**
     * 景点描述
     */
    private String description;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 开放时间
     */
    private String openTime;

    /**
     * 游玩贴士
     */
    private String tips;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 是否热门
     */
    private Integer isHot;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

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
     * 分类名称（非数据库字段）
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 最低票价（非数据库字段，通过关联查询获取）
     */
    @TableField(exist = false)
    private java.math.BigDecimal minPrice;

    /**
     * 获取景点名称（兼容前端字段名）
     */
    public String getName() {
        return this.scenicName;
    }
}
