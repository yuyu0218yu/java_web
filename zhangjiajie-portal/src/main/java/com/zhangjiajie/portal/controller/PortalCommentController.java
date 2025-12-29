package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.portal.entity.Comment;
import com.zhangjiajie.portal.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论Controller
 */
@RestController
@RequestMapping("/api/portal/comment")
@RequiredArgsConstructor
@Tag(name = "用户端-评论", description = "评论相关接口")
public class PortalCommentController {

    private final CommentService commentService;

    /**
     * 获取评论列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取评论列表")
    public Result<PageResult<Comment>> getComments(
            @Parameter(description = "目标类型：1景点 2攻略") @RequestParam Integer targetType,
            @Parameter(description = "目标ID") @RequestParam Long targetId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {

        Page<Comment> page = new Page<>(current, size);
        IPage<Comment> result = commentService.getComments(page, targetType, targetId);

        PageResult<Comment> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 发表评论
     */
    @PostMapping("/add")
    @Operation(summary = "发表评论")
    public Result<Comment> addComment(@Valid @RequestBody Comment comment) {
        try {
            Long userId = SecurityUtils.requireUserId();
            comment.setUserId(userId);
            Comment saved = commentService.addComment(comment);
            return Result.success("评论成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 回复评论
     */
    @PostMapping("/{id}/reply")
    @Operation(summary = "回复评论")
    public Result<Comment> replyComment(
            @PathVariable Long id,
            @Valid @RequestBody Comment comment) {
        try {
            Long userId = SecurityUtils.requireUserId();
            comment.setUserId(userId);
            comment.setParentId(id);
            Comment saved = commentService.replyComment(comment);
            return Result.success("回复成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public Result<Void> deleteComment(@PathVariable Long id) {
        try {
            Long userId = SecurityUtils.requireUserId();
            commentService.deleteComment(id, userId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞评论
     */
    @PostMapping("/{id}/like")
    @Operation(summary = "点赞评论")
    public Result<Void> likeComment(@PathVariable Long id) {
        commentService.likeComment(id);
        return Result.success("点赞成功");
    }

    /**
     * 获取我的评论
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的评论")
    public Result<PageResult<Comment>> getMyComments(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {

        Long userId = SecurityUtils.requireUserId();
        Page<Comment> page = new Page<>(current, size);
        IPage<Comment> result = commentService.getUserComments(page, userId);

        PageResult<Comment> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取评论回复列表
     */
    @GetMapping("/{id}/replies")
    @Operation(summary = "获取评论回复")
    public Result<List<Comment>> getCommentReplies(@PathVariable Long id) {
        List<Comment> replies = commentService.getCommentReplies(id);
        return Result.success(replies);
    }

    /**
     * 获取景点评分统计
     */
    @GetMapping("/scenic/{scenicId}/stats")
    @Operation(summary = "获取景点评分统计")
    public Result<Object> getScenicRatingStats(@PathVariable Long scenicId) {
        return Result.success(commentService.getScenicRatingStats(scenicId));
    }
}
