package com.zhangjiajie.portal.dto;

import lombok.Data;

/**
 * 用户统计DTO
 */
@Data
public class UserStatsDTO {

    /**
     * 订单总数
     */
    private Integer orderCount;

    /**
     * 待支付订单数
     */
    private Integer pendingPayCount;

    /**
     * 待使用订单数
     */
    private Integer pendingUseCount;

    /**
     * 已完成订单数
     */
    private Integer completedCount;

    /**
     * 收藏景点数
     */
    private Integer favoriteScenicCount;

    /**
     * 收藏攻略数
     */
    private Integer favoriteGuideCount;

    /**
     * 发布攻略数
     */
    private Integer publishedGuideCount;

    /**
     * 评论数
     */
    private Integer commentCount;
}
