package com.zhangjiajie.portal.service;

import com.zhangjiajie.portal.dto.UserProfileDTO;
import com.zhangjiajie.portal.dto.UserStatsDTO;

/**
 * 用户门户Service
 */
public interface UserPortalService {

    /**
     * 获取用户信息
     */
    UserProfileDTO getUserProfile(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserProfile(Long userId, UserProfileDTO profile);

    /**
     * 获取用户统计信息
     */
    UserStatsDTO getUserStats(Long userId);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新头像
     */
    void updateAvatar(Long userId, String avatarUrl);
}
