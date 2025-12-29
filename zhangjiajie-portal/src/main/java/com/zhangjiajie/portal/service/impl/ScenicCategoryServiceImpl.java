package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.ScenicCategory;
import com.zhangjiajie.portal.mapper.ScenicCategoryMapper;
import com.zhangjiajie.portal.service.ScenicCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 景点分类Service实现
 */
@Service
public class ScenicCategoryServiceImpl extends ServiceImpl<ScenicCategoryMapper, ScenicCategory> implements ScenicCategoryService {

    @Override
    public List<ScenicCategory> getActiveCategories() {
        LambdaQueryWrapper<ScenicCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicCategory::getStatus, 1)
               .eq(ScenicCategory::getDeleted, 0)
               .orderByAsc(ScenicCategory::getSortOrder);
        return list(wrapper);
    }

    @Override
    public List<ScenicCategory> getTopCategories() {
        LambdaQueryWrapper<ScenicCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicCategory::getParentId, 0)
               .eq(ScenicCategory::getStatus, 1)
               .eq(ScenicCategory::getDeleted, 0)
               .orderByAsc(ScenicCategory::getSortOrder);
        return list(wrapper);
    }
}
