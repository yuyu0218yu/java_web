package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.Guide;

import java.util.List;

/**
 * 攻略Service接口
 */
public interface GuideService extends IService<Guide> {

    /**
     * 分页查询攻略列表
     */
    IPage<Guide> getGuidePage(Page<Guide> page, Integer guideType, String keyword);

    /**
     * 获取攻略详情
     */
    Guide getGuideDetail(Long id);

    /**
     * 获取推荐攻略
     */
    List<Guide> getRecommendGuides(int limit);

    /**
     * 获取热门攻略
     */
    List<Guide> getHotGuides(int limit);

    /**
     * 获取用户的攻略
     */
    IPage<Guide> getUserGuides(Page<Guide> page, Long userId, Integer status);

    /**
     * 发布攻略
     */
    Guide publishGuide(Guide guide);

    /**
     * 更新攻略
     */
    void updateGuide(Guide guide, Long userId);

    /**
     * 删除攻略
     */
    void deleteGuide(Long id, Long userId);

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);

    /**
     * 增加点赞数
     */
    void incrementLikeCount(Long id);
}
