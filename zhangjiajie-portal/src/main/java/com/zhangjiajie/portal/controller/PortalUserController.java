package com.zhangjiajie.portal.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.common.security.SecurityUtils;
import com.zhangjiajie.portal.dto.UserProfileDTO;
import com.zhangjiajie.portal.dto.UserStatsDTO;
import com.zhangjiajie.portal.service.UserPortalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中心Controller
 */
@RestController
@RequestMapping("/api/portal/user")
@RequiredArgsConstructor
@Tag(name = "用户端-用户中心", description = "用户中心相关接口")
public class PortalUserController {

    private final UserPortalService userPortalService;

    /**
     * 获取个人信息
     */
    @GetMapping("/profile")
    @Operation(summary = "获取个人信息")
    public Result<UserProfileDTO> getProfile() {
        Long userId = SecurityUtils.requireUserId();
        UserProfileDTO profile = userPortalService.getUserProfile(userId);
        return Result.success(profile);
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    @Operation(summary = "更新个人信息")
    public Result<Void> updateProfile(@Valid @RequestBody UserProfileDTO profile) {
        try {
            Long userId = SecurityUtils.requireUserId();
            userPortalService.updateUserProfile(userId, profile);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取用户统计信息")
    public Result<UserStatsDTO> getUserStats() {
        Long userId = SecurityUtils.requireUserId();
        UserStatsDTO stats = userPortalService.getUserStats(userId);
        return Result.success(stats);
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            Long userId = SecurityUtils.requireUserId();
            userPortalService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新头像
     */
    @PostMapping("/avatar")
    @Operation(summary = "更新头像")
    public Result<String> updateAvatar(@RequestParam String avatarUrl) {
        try {
            Long userId = SecurityUtils.requireUserId();
            userPortalService.updateAvatar(userId, avatarUrl);
            return Result.success("头像更新成功", avatarUrl);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
