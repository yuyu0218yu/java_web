package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.Favorite;
import com.zhangjiajie.portal.mapper.FavoriteMapper;
import com.zhangjiajie.portal.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收藏Service实现
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Integer targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);

        Favorite existing = getOne(wrapper);
        if (existing != null) {
            // 已收藏，取消收藏
            removeById(existing.getId());
            return false;
        } else {
            // 未收藏，添加收藏
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setTargetType(targetType);
            favorite.setTargetId(targetId);
            save(favorite);
            return true;
        }
    }

    @Override
    public boolean isFavorited(Long userId, Integer targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        return count(wrapper) > 0;
    }

    @Override
    public IPage<Favorite> getUserFavorites(Page<Favorite> page, Long userId, Integer targetType) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        if (targetType != null) {
            wrapper.eq(Favorite::getTargetType, targetType);
        }
        wrapper.orderByDesc(Favorite::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public void removeFavorite(Long id, Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getId, id)
                .eq(Favorite::getUserId, userId);
        remove(wrapper);
    }

    @Override
    public int getFavoriteCount(Integer targetType, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId);
        return (int) count(wrapper);
    }
}
