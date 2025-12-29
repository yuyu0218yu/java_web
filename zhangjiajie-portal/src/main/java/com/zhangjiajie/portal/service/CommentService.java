package com.zhangjiajie.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.portal.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * 评论Service接口
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页获取评论列表
     */
    IPage<Comment> getComments(Page<Comment> page, Integer targetType, Long targetId);

    /**
     * 添加评论
     */
    Comment addComment(Comment comment);

    /**
     * 回复评论
     */
    Comment replyComment(Comment comment);

    /**
     * 删除评论
     */
    void deleteComment(Long id, Long userId);

    /**
     * 点赞评论
     */
    void likeComment(Long id);

    /**
     * 获取用户评论列表
     */
    IPage<Comment> getUserComments(Page<Comment> page, Long userId);

    /**
     * 获取评论回复
     */
    List<Comment> getCommentReplies(Long parentId);

    /**
     * 获取景点评分统计
     */
    Map<String, Object> getScenicRatingStats(Long scenicId);

    /**
     * 获取评论数量
     */
    int getCommentCount(Integer targetType, Long targetId);
}
