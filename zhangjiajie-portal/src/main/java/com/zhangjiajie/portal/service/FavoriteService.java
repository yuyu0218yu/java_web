package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.Favorite;

/**
 * 收藏Service接口
 */
public interface FavoriteService extends IService<Favorite> {

    /**
     * 切换收藏状态
     * @return true-已收藏 false-已取消收藏
     */
    boolean toggleFavorite(Long userId, Integer targetType, Long targetId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Integer targetType, Long targetId);

    /**
     * 获取用户收藏列表
     */
    IPage<Favorite> getUserFavorites(Page<Favorite> page, Long userId, Integer targetType);

    /**
     * 取消收藏
     */
    void removeFavorite(Long id, Long userId);

    /**
     * 获取收藏数量
     */
    int getFavoriteCount(Integer targetType, Long targetId);
}
