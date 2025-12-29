package com.zhangjiajie.portal.dto;

import com.zhangjiajie.portal.entity.Banner;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;
import lombok.Data;

import java.util.List;

/**
 * 首页数据DTO
 */
@Data
public class HomeDataDTO {

    /**
     * 轮播图
     */
    private List<Banner> banners;

    /**
     * 热门景点
     */
    private List<Scenic> hotScenics;

    /**
     * 推荐景点
     */
    private List<Scenic> recommendScenics;

    /**
     * 推荐攻略
     */
    private List<Guide> recommendGuides;

    /**
     * 景点分类
     */
    private List<ScenicCategory> categories;
}
