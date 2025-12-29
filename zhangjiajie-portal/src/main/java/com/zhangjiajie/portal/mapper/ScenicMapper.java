package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.portal.entity.Scenic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 景点Mapper
 */
@Mapper
public interface ScenicMapper extends BaseMapper<Scenic> {

    /**
     * 分页查询景点列表（包含分类名称）
     */
    @Select("SELECT s.*, c.category_name " +
            "FROM portal_scenic s " +
            "LEFT JOIN portal_scenic_category c ON s.category_id = c.id AND c.deleted = 0 " +
            "WHERE s.deleted = 0 AND s.status = 1 " +
            "ORDER BY s.sort_order ASC, s.create_time DESC")
    IPage<Scenic> selectScenicPage(Page<Scenic> page);

    /**
     * 根据分类查询景点
     */
    @Select("SELECT s.*, c.category_name " +
            "FROM portal_scenic s " +
            "LEFT JOIN portal_scenic_category c ON s.category_id = c.id AND c.deleted = 0 " +
            "WHERE s.deleted = 0 AND s.status = 1 AND s.category_id = #{categoryId} " +
            "ORDER BY s.sort_order ASC, s.create_time DESC")
    IPage<Scenic> selectScenicByCategoryId(Page<Scenic> page, @Param("categoryId") Long categoryId);

    /**
     * 查询热门景点
     */
    @Select("SELECT s.*, c.category_name " +
            "FROM portal_scenic s " +
            "LEFT JOIN portal_scenic_category c ON s.category_id = c.id AND c.deleted = 0 " +
            "WHERE s.deleted = 0 AND s.status = 1 AND s.is_hot = 1 " +
            "ORDER BY s.sort_order ASC, s.view_count DESC " +
            "LIMIT #{limit}")
    java.util.List<Scenic> selectHotScenics(@Param("limit") int limit);

    /**
     * 查询推荐景点
     */
    @Select("SELECT s.*, c.category_name " +
            "FROM portal_scenic s " +
            "LEFT JOIN portal_scenic_category c ON s.category_id = c.id AND c.deleted = 0 " +
            "WHERE s.deleted = 0 AND s.status = 1 AND s.is_recommend = 1 " +
            "ORDER BY s.sort_order ASC " +
            "LIMIT #{limit}")
    java.util.List<Scenic> selectRecommendScenics(@Param("limit") int limit);

    /**
     * 增加浏览量
     */
    @Update("UPDATE portal_scenic SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);
}
