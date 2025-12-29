package com.zhangjiajie.portal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.PageResult;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.portal.entity.Favorite;
import com.zhangjiajie.portal.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 收藏Controller
 */
@RestController
@RequestMapping("/api/portal/favorite")
@RequiredArgsConstructor
@Tag(name = "用户端-收藏", description = "收藏相关接口")
public class PortalFavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 切换收藏状态
     */
    @PostMapping("/toggle")
    @Operation(summary = "收藏/取消收藏")
    public Result<Map<String, Object>> toggleFavorite(
            @Parameter(description = "目标类型：1景点 2攻略") @RequestParam Integer targetType,
            @Parameter(description = "目标ID") @RequestParam Long targetId) {

        Long userId = SecurityUtils.requireUserId();
        boolean isFavorited = favoriteService.toggleFavorite(userId, targetType, targetId);

        Map<String, Object> result = new HashMap<>();
        result.put("isFavorited", isFavorited);
        result.put("message", isFavorited ? "收藏成功" : "已取消收藏");

        return Result.success(result);
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check")
    @Operation(summary = "检查是否已收藏")
    public Result<Boolean> checkFavorite(
            @Parameter(description = "目标类型：1景点 2攻略") @RequestParam Integer targetType,
            @Parameter(description = "目标ID") @RequestParam Long targetId) {

        Long userId = SecurityUtils.requireUserId();
        boolean isFavorited = favoriteService.isFavorited(userId, targetType, targetId);
        return Result.success(isFavorited);
    }

    /**
     * 获取我的景点收藏
     */
    @GetMapping("/scenic")
    @Operation(summary = "获取我收藏的景点")
    public Result<PageResult<Favorite>> getFavoriteScenics(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {

        Long userId = SecurityUtils.requireUserId();
        Page<Favorite> page = new Page<>(current, size);
        IPage<Favorite> result = favoriteService.getUserFavorites(page, userId, 1);

        PageResult<Favorite> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取我的攻略收藏
     */
    @GetMapping("/guide")
    @Operation(summary = "获取我收藏的攻略")
    public Result<PageResult<Favorite>> getFavoriteGuides(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {

        Long userId = SecurityUtils.requireUserId();
        Page<Favorite> page = new Page<>(current, size);
        IPage<Favorite> result = favoriteService.getUserFavorites(page, userId, 2);

        PageResult<Favorite> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 获取我的所有收藏
     */
    @GetMapping("/list")
    @Operation(summary = "获取我的所有收藏")
    public Result<PageResult<Favorite>> getAllFavorites(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "目标类型：1景点 2攻略") @RequestParam(required = false) Integer targetType) {

        Long userId = SecurityUtils.requireUserId();
        Page<Favorite> page = new Page<>(current, size);
        IPage<Favorite> result = favoriteService.getUserFavorites(page, userId, targetType);

        PageResult<Favorite> pageResult = PageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
        return Result.success(pageResult);
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "取消收藏")
    public Result<Void> removeFavorite(@PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        favoriteService.removeFavorite(id, userId);
        return Result.success("取消收藏成功");
    }
}
