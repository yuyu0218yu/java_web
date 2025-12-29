package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhangjiajie.portal.dto.UserProfileDTO;
import com.zhangjiajie.portal.dto.UserStatsDTO;
import com.zhangjiajie.portal.entity.Comment;
import com.zhangjiajie.portal.entity.Favorite;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.entity.Order;
import com.zhangjiajie.portal.mapper.CommentMapper;
import com.zhangjiajie.portal.mapper.FavoriteMapper;
import com.zhangjiajie.portal.mapper.GuideMapper;
import com.zhangjiajie.portal.mapper.OrderMapper;
import com.zhangjiajie.portal.service.UserPortalService;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户门户Service实现
 */
@Service
@RequiredArgsConstructor
public class UserPortalServiceImpl implements UserPortalService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final FavoriteMapper favoriteMapper;
    private final GuideMapper guideMapper;
    private final CommentMapper commentMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfileDTO profile = new UserProfileDTO();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setNickname(user.getNickname());
        profile.setAvatar(user.getAvatar());
        profile.setEmail(user.getEmail());
        profile.setPhone(user.getPhone());
        profile.setGender(user.getGender());
        // bio 字段可能需要在 User 实体中添加
        return profile;
    }

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UserProfileDTO profile) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新允许修改的字段
        if (profile.getNickname() != null) {
            user.setNickname(profile.getNickname());
        }
        if (profile.getEmail() != null) {
            user.setEmail(profile.getEmail());
        }
        if (profile.getPhone() != null) {
            user.setPhone(profile.getPhone());
        }
        if (profile.getGender() != null) {
            user.setGender(profile.getGender());
        }

        userMapper.updateById(user);
    }

    @Override
    public UserStatsDTO getUserStats(Long userId) {
        UserStatsDTO stats = new UserStatsDTO();

        // 订单统计
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId).eq(Order::getDeleted, 0);
        stats.setOrderCount(Math.toIntExact(orderMapper.selectCount(orderWrapper)));

        // 待支付订单
        orderWrapper.clear();
        orderWrapper.eq(Order::getUserId, userId).eq(Order::getOrderStatus, 0).eq(Order::getDeleted, 0);
        stats.setPendingPayCount(Math.toIntExact(orderMapper.selectCount(orderWrapper)));

        // 已支付待使用订单
        orderWrapper.clear();
        orderWrapper.eq(Order::getUserId, userId).eq(Order::getOrderStatus, 1).eq(Order::getDeleted, 0);
        stats.setPendingUseCount(Math.toIntExact(orderMapper.selectCount(orderWrapper)));

        // 已完成订单
        orderWrapper.clear();
        orderWrapper.eq(Order::getUserId, userId).eq(Order::getOrderStatus, 2).eq(Order::getDeleted, 0);
        stats.setCompletedCount(Math.toIntExact(orderMapper.selectCount(orderWrapper)));

        // 收藏景点数
        LambdaQueryWrapper<Favorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(Favorite::getUserId, userId).eq(Favorite::getTargetType, 1);
        stats.setFavoriteScenicCount(Math.toIntExact(favoriteMapper.selectCount(favoriteWrapper)));

        // 收藏攻略数
        favoriteWrapper.clear();
        favoriteWrapper.eq(Favorite::getUserId, userId).eq(Favorite::getTargetType, 2);
        stats.setFavoriteGuideCount(Math.toIntExact(favoriteMapper.selectCount(favoriteWrapper)));

        // 发布攻略数
        LambdaQueryWrapper<Guide> guideWrapper = new LambdaQueryWrapper<>();
        guideWrapper.eq(Guide::getAuthorId, userId).eq(Guide::getDeleted, 0);
        stats.setPublishedGuideCount(Math.toIntExact(guideMapper.selectCount(guideWrapper)));

        // 评论数
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, userId).eq(Comment::getDeleted, 0);
        stats.setCommentCount(Math.toIntExact(commentMapper.selectCount(commentWrapper)));

        return stats;
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
    }
}
