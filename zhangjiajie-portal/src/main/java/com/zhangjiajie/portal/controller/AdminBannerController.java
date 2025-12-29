package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.entity.Banner;
import com.zhangjiajie.portal.mapper.BannerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图管理Controller（管理端）
 */
@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
@Tag(name = "管理端-轮播图", description = "轮播图管理接口")
public class AdminBannerController {

    private final BannerMapper bannerMapper;

    /**
     * 获取轮播图列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取轮播图列表")
    public Result<PageResult<Banner>> getBannerList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {

        Page<Banner> page = new Page<>(current, size);
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null, Banner::getStatus, status)
               .eq(Banner::getDeleted, 0)
               .orderByAsc(Banner::getSortOrder);

        IPage<Banner> result = bannerMapper.selectPage(page, wrapper);

        PageResult<Banner> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取轮播图详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取轮播图详情")
    public Result<Banner> getBanner(@PathVariable Long id) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) {
            return Result.notFound("轮播图不存在");
        }
        return Result.success(banner);
    }

    /**
     * 新增轮播图
     */
    @PostMapping
    @Operation(summary = "新增轮播图")
    public Result<Void> addBanner(@RequestBody Banner banner) {
        banner.setDeleted(0);
        bannerMapper.insert(banner);
        return Result.success("新增成功");
    }

    /**
     * 修改轮播图
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改轮播图")
    public Result<Void> updateBanner(@PathVariable Long id, @RequestBody Banner banner) {
        banner.setId(id);
        bannerMapper.updateById(banner);
        return Result.success("修改成功");
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除轮播图")
    public Result<Void> deleteBanner(@PathVariable Long id) {
        bannerMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除轮播图
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除轮播图")
    public Result<Void> batchDeleteBanners(@RequestBody List<Long> ids) {
        bannerMapper.deleteBatchIds(ids);
        return Result.success("批量删除成功");
    }
}
