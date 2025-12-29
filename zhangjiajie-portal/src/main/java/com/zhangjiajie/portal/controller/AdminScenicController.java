package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Scenic;
import com.zhangjiajie.portal.entity.ScenicCategory;
import com.zhangjiajie.portal.service.ScenicService;
import com.zhangjiajie.portal.service.ScenicCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点管理Controller（管理端）
 */
@RestController
@RequestMapping("/api/admin/scenic")
@RequiredArgsConstructor
@Tag(name = "管理端-景点", description = "景点管理接口")
public class AdminScenicController {

    private final ScenicService scenicService;
    private final ScenicCategoryService categoryService;

    /**
     * 获取景点列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取景点列表")
    public Result<PageResult<Scenic>> getScenicList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String scenicName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {

        Page<Scenic> page = new Page<>(current, size);
        LambdaQueryWrapper<Scenic> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(scenicName), Scenic::getScenicName, scenicName)
               .eq(categoryId != null, Scenic::getCategoryId, categoryId)
               .eq(status != null, Scenic::getStatus, status)
               .eq(Scenic::getDeleted, 0)
               .orderByDesc(Scenic::getCreateTime);

        IPage<Scenic> result = scenicService.page(page, wrapper);

        PageResult<Scenic> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取景点详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取景点详情")
    public Result<Scenic> getScenic(@PathVariable Long id) {
        Scenic scenic = scenicService.getById(id);
        if (scenic == null) {
            return Result.notFound("景点不存在");
        }
        return Result.success(scenic);
    }

    /**
     * 新增景点
     */
    @PostMapping
    @Operation(summary = "新增景点")
    public Result<Void> addScenic(@RequestBody Scenic scenic) {
        scenic.setDeleted(0);
        scenic.setViewCount(0);
        scenicService.save(scenic);
        return Result.success("新增成功");
    }

    /**
     * 修改景点
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改景点")
    public Result<Void> updateScenic(@PathVariable Long id, @RequestBody Scenic scenic) {
        scenic.setId(id);
        scenicService.updateById(scenic);
        return Result.success("修改成功");
    }

    /**
     * 删除景点
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除景点")
    public Result<Void> deleteScenic(@PathVariable Long id) {
        scenicService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除景点
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除景点")
    public Result<Void> batchDeleteScenics(@RequestBody List<Long> ids) {
        scenicService.removeByIds(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 获取所有分类
     */
    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public Result<List<ScenicCategory>> getCategories() {
        List<ScenicCategory> categories = categoryService.list(
            new LambdaQueryWrapper<ScenicCategory>()
                .eq(ScenicCategory::getStatus, 1)
                .eq(ScenicCategory::getDeleted, 0)
                .orderByAsc(ScenicCategory::getSortOrder)
        );
        return Result.success(categories);
    }
}
