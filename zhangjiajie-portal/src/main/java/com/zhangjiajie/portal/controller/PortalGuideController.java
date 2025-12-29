package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 攻略Controller
 */
@RestController
@RequestMapping("/api/portal/guide")
@RequiredArgsConstructor
@Tag(name = "用户端-攻略", description = "攻略相关接口")
public class PortalGuideController {

    private final GuideService guideService;

    /**
     * 获取攻略列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取攻略列表")
    public Result<PageResult<Guide>> getGuideList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "攻略类型：1攻略 2游记") @RequestParam(required = false) Integer guideType,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {

        Page<Guide> page = new Page<>(current, size);
        IPage<Guide> result = guideService.getGuidePage(page, guideType, keyword);

        PageResult<Guide> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取攻略详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取攻略详情")
    public Result<Guide> getGuideDetail(@PathVariable Long id) {
        Guide guide = guideService.getGuideDetail(id);
        if (guide == null) {
            return Result.notFound("攻略不存在");
        }

        // 增加浏览量
        guideService.incrementViewCount(id);

        return Result.success(guide);
    }

    /**
     * 发布攻略
     */
    @PostMapping("/publish")
    @Operation(summary = "发布攻略")
    public Result<Guide> publishGuide(@Valid @RequestBody Guide guide) {
        try {
            Long userId = SecurityUtils.requireUserId();
            guide.setAuthorId(userId);
            guide.setStatus(0); // 待审核
            Guide saved = guideService.publishGuide(guide);
            return Result.success("发布成功，等待审核", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取我的攻略
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的攻略")
    public Result<PageResult<Guide>> getMyGuides(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        Long userId = SecurityUtils.requireUserId();
        Page<Guide> page = new Page<>(current, size);
        IPage<Guide> result = guideService.getUserGuides(page, userId, status);

        PageResult<Guide> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 更新我的攻略
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新攻略")
    public Result<Void> updateGuide(@PathVariable Long id, @Valid @RequestBody Guide guide) {
        try {
            Long userId = SecurityUtils.requireUserId();
            guide.setId(id);
            guideService.updateGuide(guide, userId);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除我的攻略
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除攻略")
    public Result<Void> deleteGuide(@PathVariable Long id) {
        try {
            Long userId = SecurityUtils.requireUserId();
            guideService.deleteGuide(id, userId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞攻略
     */
    @PostMapping("/{id}/like")
    @Operation(summary = "点赞攻略")
    public Result<Void> likeGuide(@PathVariable Long id) {
        guideService.incrementLikeCount(id);
        return Result.success("点赞成功");
    }

    /**
     * 获取热门攻略
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门攻略")
    public Result<List<Guide>> getHotGuides(
            @Parameter(description = "数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<Guide> guides = guideService.getHotGuides(limit);
        return Result.success(guides);
    }
}
