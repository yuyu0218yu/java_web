package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.portal.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 收藏Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    /**
     * 查询用户收藏的景点
     */
    @Select("SELECT f.*, s.scenic_name as target_name, s.cover_image as target_cover " +
            "FROM portal_favorite f " +
            "LEFT JOIN portal_scenic s ON f.target_id = s.id AND s.deleted = 0 " +
            "WHERE f.user_id = #{userId} AND f.target_type = 1 " +
            "ORDER BY f.create_time DESC")
    IPage<Favorite> selectScenicFavorites(Page<Favorite> page, @Param("userId") Long userId);

    /**
     * 查询用户收藏的攻略
     */
    @Select("SELECT f.*, g.title as target_name, g.cover_image as target_cover " +
            "FROM portal_favorite f " +
            "LEFT JOIN portal_guide g ON f.target_id = g.id AND g.deleted = 0 " +
            "WHERE f.user_id = #{userId} AND f.target_type = 2 " +
            "ORDER BY f.create_time DESC")
    IPage<Favorite> selectGuideFavorites(Page<Favorite> page, @Param("userId") Long userId);

    /**
     * 检查是否已收藏
     */
    @Select("SELECT COUNT(*) FROM portal_favorite " +
            "WHERE user_id = #{userId} AND target_type = #{targetType} AND target_id = #{targetId}")
    int checkFavorite(@Param("userId") Long userId,
                      @Param("targetType") Integer targetType,
                      @Param("targetId") Long targetId);

    /**
     * 统计用户收藏数量
     */
    @Select("SELECT COUNT(*) FROM portal_favorite WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);
}
