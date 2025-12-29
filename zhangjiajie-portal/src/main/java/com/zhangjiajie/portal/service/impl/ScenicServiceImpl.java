package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;
import com.zhangjiajie.portal.mapper.ScenicCategoryMapper;
import com.zhangjiajie.portal.mapper.ScenicMapper;
import com.zhangjiajie.portal.service.ScenicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 景点Service实现
 */
@Service
@RequiredArgsConstructor
public class ScenicServiceImpl extends ServiceImpl<ScenicMapper, Scenic> implements ScenicService {

    private final ScenicMapper scenicMapper;
    private final ScenicCategoryMapper categoryMapper;

    @Override
    public IPage<Scenic> getScenicPage(Page<Scenic> page, Long categoryId, String keyword) {
        LambdaQueryWrapper<Scenic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Scenic::getStatus, 1);

        if (categoryId != null) {
            wrapper.eq(Scenic::getCategoryId, categoryId);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Scenic::getScenicName, keyword)
                    .or().like(Scenic::getAddress, keyword)
                    .or().like(Scenic::getDescription, keyword));
        }

        wrapper.orderByAsc(Scenic::getSortOrder)
               .orderByDesc(Scenic::getCreateTime);

        IPage<Scenic> result = page(page, wrapper);

        // 填充分类名称
        result.getRecords().forEach(scenic -> {
            if (scenic.getCategoryId() != null) {
                ScenicCategory category = categoryMapper.selectById(scenic.getCategoryId());
                if (category != null) {
                    scenic.setCategoryName(category.getCategoryName());
                }
            }
        });

        return result;
    }

    @Override
    public Scenic getScenicDetail(Long id) {
        Scenic scenic = getById(id);
        if (scenic != null && scenic.getCategoryId() != null) {
            ScenicCategory category = categoryMapper.selectById(scenic.getCategoryId());
            if (category != null) {
                scenic.setCategoryName(category.getCategoryName());
            }
        }
        return scenic;
    }

    @Override
    public List<Scenic> getHotScenics(int limit) {
        return scenicMapper.selectHotScenics(limit);
    }

    @Override
    public List<Scenic> getRecommendScenics(int limit) {
        return scenicMapper.selectRecommendScenics(limit);
    }

    @Override
    public List<ScenicCategory> getAllCategories() {
        LambdaQueryWrapper<ScenicCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScenicCategory::getStatus, 1)
               .orderByAsc(ScenicCategory::getSortOrder);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public void incrementViewCount(Long id) {
        scenicMapper.incrementViewCount(id);
    }

    @Override
    public IPage<Scenic> searchScenics(Page<Scenic> page, String keyword) {
        return getScenicPage(page, null, keyword);
    }
}
