package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.Comment;
import com.zhangjiajie.portal.mapper.CommentMapper;
import com.zhangjiajie.portal.service.CommentService;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论Service实现
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserMapper userMapper;

    @Override
    public IPage<Comment> getComments(Page<Comment> page, Integer targetType, Long targetId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, targetType)
                .eq(Comment::getTargetId, targetId)
                .eq(Comment::getParentId, 0) // 只查询顶级评论
                .eq(Comment::getStatus, 1) // 已发布
                .eq(Comment::getDeleted, 0)
                .orderByDesc(Comment::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        // 获取用户信息
        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            comment.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            comment.setUserAvatar(user.getAvatar());
        }
        comment.setParentId(0L);
        comment.setLikeCount(0);
        comment.setStatus(1); // 直接发布，无需审核
        comment.setDeleted(0);
        save(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment replyComment(Comment comment) {
        // 检查父评论是否存在
        Comment parent = getById(comment.getParentId());
        if (parent == null) {
            throw new RuntimeException("评论不存在");
        }

        // 获取用户信息
        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            comment.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            comment.setUserAvatar(user.getAvatar());
        }

        // 继承父评论的目标信息
        comment.setTargetType(parent.getTargetType());
        comment.setTargetId(parent.getTargetId());
        comment.setLikeCount(0);
        comment.setStatus(1);
        comment.setDeleted(0);
        save(comment);
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getId, id)
                .eq(Comment::getUserId, userId);
        Comment comment = getOne(wrapper);
        if (comment == null) {
            throw new RuntimeException("评论不存在或无权删除");
        }

        // 逻辑删除
        comment.setDeleted(1);
        updateById(comment);

        // 同时删除该评论的所有回复
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getParentId, id)
                .set(Comment::getDeleted, 1);
        update(updateWrapper);
    }

    @Override
    @Transactional
    public void likeComment(Long id) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, id)
                .setSql("like_count = like_count + 1");
        update(wrapper);
    }

    @Override
    public IPage<Comment> getUserComments(Page<Comment> page, Long userId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, userId)
                .eq(Comment::getDeleted, 0)
                .orderByDesc(Comment::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<Comment> getCommentReplies(Long parentId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, parentId)
                .eq(Comment::getStatus, 1)
                .eq(Comment::getDeleted, 0)
                .orderByAsc(Comment::getCreateTime);
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getScenicRatingStats(Long scenicId) {
        Map<String, Object> stats = new HashMap<>();

        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, 1) // 景点
                .eq(Comment::getTargetId, scenicId)
                .eq(Comment::getParentId, 0) // 只统计顶级评论
                .eq(Comment::getStatus, 1)
                .eq(Comment::getDeleted, 0)
                .isNotNull(Comment::getRating);

        List<Comment> comments = list(wrapper);

        int totalCount = comments.size();
        stats.put("totalCount", totalCount);

        if (totalCount == 0) {
            stats.put("averageRating", 5.0);
            stats.put("ratingDistribution", new int[]{0, 0, 0, 0, 0});
            return stats;
        }

        // 计算平均分
        double sum = comments.stream()
                .mapToInt(Comment::getRating)
                .sum();
        BigDecimal average = BigDecimal.valueOf(sum / totalCount)
                .setScale(1, RoundingMode.HALF_UP);
        stats.put("averageRating", average.doubleValue());

        // 计算各星级分布
        int[] distribution = new int[5];
        for (Comment comment : comments) {
            int rating = comment.getRating();
            if (rating >= 1 && rating <= 5) {
                distribution[rating - 1]++;
            }
        }
        stats.put("ratingDistribution", distribution);

        return stats;
    }

    @Override
    public int getCommentCount(Integer targetType, Long targetId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetType, targetType)
                .eq(Comment::getTargetId, targetId)
                .eq(Comment::getStatus, 1)
                .eq(Comment::getDeleted, 0);
        return (int) count(wrapper);
    }
}
