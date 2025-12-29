package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 攻略管理Controller（管理端）
 */
@RestController
@RequestMapping("/api/admin/guide")
@RequiredArgsConstructor
@Tag(name = "管理端-攻略", description = "攻略管理接口")
public class AdminGuideController {

    private final GuideService guideService;

    /**
     * 获取攻略列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取攻略列表")
    public Result<PageResult<Guide>> getGuideList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer guideType,
            @RequestParam(required = false) Integer status) {

        Page<Guide> page = new Page<>(current, size);
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title), Guide::getTitle, title)
               .eq(guideType != null, Guide::getGuideType, guideType)
               .eq(status != null, Guide::getStatus, status)
               .eq(Guide::getDeleted, 0)
               .orderByDesc(Guide::getCreateTime);

        IPage<Guide> result = guideService.page(page, wrapper);

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
    public Result<Guide> getGuide(@PathVariable Long id) {
        Guide guide = guideService.getById(id);
        if (guide == null) {
            return Result.notFound("攻略不存在");
        }
        return Result.success(guide);
    }

    /**
     * 新增攻略
     */
    @PostMapping
    @Operation(summary = "新增攻略")
    public Result<Void> addGuide(@RequestBody Guide guide) {
        guide.setDeleted(0);
        guide.setViewCount(0);
        guide.setLikeCount(0);
        guide.setCommentCount(0);
        guideService.save(guide);
        return Result.success("新增成功");
    }

    /**
     * 修改攻略
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改攻略")
    public Result<Void> updateGuide(@PathVariable Long id, @RequestBody Guide guide) {
        guide.setId(id);
        guideService.updateById(guide);
        return Result.success("修改成功");
    }

    /**
     * 删除攻略
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除攻略")
    public Result<Void> deleteGuide(@PathVariable Long id) {
        guideService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除攻略
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除攻略")
    public Result<Void> batchDeleteGuides(@RequestBody List<Long> ids) {
        guideService.removeByIds(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 审核攻略
     */
    @PutMapping("/{id}/audit")
    @Operation(summary = "审核攻略")
    public Result<Void> auditGuide(@PathVariable Long id, @RequestParam Integer status) {
        Guide guide = new Guide();
        guide.setId(id);
        guide.setStatus(status);
        guideService.updateById(guide);
        return Result.success("审核成功");
    }
}
