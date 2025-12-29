package com.zhangjiajie.portal.service.impl;

import com.zhangjiajie.portal.dto.HomeDataDTO;
import com.zhangjiajie.portal.entity.Banner;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;
import com.zhangjiajie.portal.mapper.BannerMapper;
import com.zhangjiajie.portal.service.GuideService;
import com.zhangjiajie.portal.service.HomeService;
import com.zhangjiajie.portal.service.ScenicCategoryService;
import com.zhangjiajie.portal.service.ScenicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页Service实现
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BannerMapper bannerMapper;
    private final ScenicService scenicService;
    private final GuideService guideService;
    private final ScenicCategoryService scenicCategoryService;

    @Override
    public HomeDataDTO getHomeData() {
        HomeDataDTO data = new HomeDataDTO();

        // 获取轮播图
        List<Banner> banners = bannerMapper.selectActiveBanners(5);
        data.setBanners(banners);

        // 获取热门景点
        List<Scenic> hotScenics = scenicService.getHotScenics(8);
        data.setHotScenics(hotScenics);

        // 获取推荐景点
        List<Scenic> recommendScenics = scenicService.getRecommendScenics(4);
        data.setRecommendScenics(recommendScenics);

        // 获取推荐攻略
        List<Guide> recommendGuides = guideService.getRecommendGuides(6);
        data.setRecommendGuides(recommendGuides);

        // 获取景点分类
        List<ScenicCategory> categories = scenicCategoryService.getTopCategories();
        data.setCategories(categories);

        return data;
    }
}
