package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.ScenicCategory;

import java.util.List;

/**
 * 景点分类Service接口
 */
public interface ScenicCategoryService extends IService<ScenicCategory> {

    /**
     * 获取所有启用的分类
     */
    List<ScenicCategory> getActiveCategories();

    /**
     * 获取顶级分类
     */
    List<ScenicCategory> getTopCategories();
}
