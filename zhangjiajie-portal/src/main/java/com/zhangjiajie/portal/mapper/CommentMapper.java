package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.portal.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 评论Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询目标的评论列表（一级评论）
     */
    @Select("SELECT * FROM portal_comment " +
            "WHERE deleted = 0 AND status = 1 AND target_type = #{targetType} AND target_id = #{targetId} AND parent_id = 0 " +
            "ORDER BY create_time DESC")
    IPage<Comment> selectCommentsByTarget(Page<Comment> page,
                                          @Param("targetType") Integer targetType,
                                          @Param("targetId") Long targetId);

    /**
     * 查询评论的回复列表
     */
    @Select("SELECT * FROM portal_comment " +
            "WHERE deleted = 0 AND status = 1 AND parent_id = #{parentId} " +
            "ORDER BY create_time ASC")
    List<Comment> selectRepliesByParentId(@Param("parentId") Long parentId);

    /**
     * 统计目标的评论数量
     */
    @Select("SELECT COUNT(*) FROM portal_comment " +
            "WHERE deleted = 0 AND status = 1 AND target_type = #{targetType} AND target_id = #{targetId}")
    int countByTarget(@Param("targetType") Integer targetType, @Param("targetId") Long targetId);

    /**
     * 计算目标的平均评分
     */
    @Select("SELECT IFNULL(AVG(rating), 5.0) FROM portal_comment " +
            "WHERE deleted = 0 AND status = 1 AND target_type = #{targetType} AND target_id = #{targetId} AND rating IS NOT NULL")
    Double avgRatingByTarget(@Param("targetType") Integer targetType, @Param("targetId") Long targetId);

    /**
     * 增加点赞数
     */
    @Update("UPDATE portal_comment SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);
}
