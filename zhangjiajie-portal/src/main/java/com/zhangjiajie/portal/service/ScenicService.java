package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;

import java.util.List;

/**
 * 景点Service接口
 */
public interface ScenicService extends IService<Scenic> {

    /**
     * 分页查询景点列表
     */
    IPage<Scenic> getScenicPage(Page<Scenic> page, Long categoryId, String keyword);

    /**
     * 获取景点详情
     */
    Scenic getScenicDetail(Long id);

    /**
     * 获取热门景点
     */
    List<Scenic> getHotScenics(int limit);

    /**
     * 获取推荐景点
     */
    List<Scenic> getRecommendScenics(int limit);

    /**
     * 获取所有景点分类
     */
    List<ScenicCategory> getAllCategories();

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);

    /**
     * 搜索景点
     */
    IPage<Scenic> searchScenics(Page<Scenic> page, String keyword);
}
