package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.portal.entity.Guide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 攻略Mapper
 */
@Mapper
public interface GuideMapper extends BaseMapper<Guide> {

    /**
     * 分页查询攻略列表（已发布）
     */
    @Select("SELECT * FROM portal_guide " +
            "WHERE deleted = 0 AND status = 1 " +
            "ORDER BY is_recommend DESC, create_time DESC")
    IPage<Guide> selectGuidePage(Page<Guide> page);

    /**
     * 查询推荐攻略
     */
    @Select("SELECT * FROM portal_guide " +
            "WHERE deleted = 0 AND status = 1 AND is_recommend = 1 " +
            "ORDER BY create_time DESC " +
            "LIMIT #{limit}")
    List<Guide> selectRecommendGuides(@Param("limit") int limit);

    /**
     * 查询用户的攻略
     */
    @Select("SELECT * FROM portal_guide " +
            "WHERE deleted = 0 AND author_id = #{authorId} " +
            "ORDER BY create_time DESC")
    IPage<Guide> selectGuidesByAuthorId(Page<Guide> page, @Param("authorId") Long authorId);

    /**
     * 增加浏览量
     */
    @Update("UPDATE portal_guide SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);

    /**
     * 增加点赞数
     */
    @Update("UPDATE portal_guide SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);

    /**
     * 增加评论数
     */
    @Update("UPDATE portal_guide SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(@Param("id") Long id);
}
