package com.zhangjiajie.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.portal.entity.Guide;
import com.zhangjiajie.portal.mapper.GuideMapper;
import com.zhangjiajie.portal.service.GuideService;
import com.zhangjiajie.system.entity.User;
import com.zhangjiajie.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 攻略Service实现
 */
@Service
@RequiredArgsConstructor
public class GuideServiceImpl extends ServiceImpl<GuideMapper, Guide> implements GuideService {

    private final GuideMapper guideMapper;
    private final UserMapper userMapper;

    @Override
    public IPage<Guide> getGuidePage(Page<Guide> page, Integer guideType, String keyword) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getStatus, 1)
               .eq(Guide::getDeleted, 0);

        if (guideType != null) {
            wrapper.eq(Guide::getGuideType, guideType);
        }

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Guide::getTitle, keyword)
                    .or().like(Guide::getTags, keyword));
        }

        wrapper.orderByDesc(Guide::getIsRecommend)
               .orderByDesc(Guide::getCreateTime);

        return page(page, wrapper);
    }

    @Override
    public Guide getGuideDetail(Long id) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getId, id)
               .eq(Guide::getDeleted, 0);
        return getOne(wrapper);
    }

    @Override
    public List<Guide> getRecommendGuides(int limit) {
        return guideMapper.selectRecommendGuides(limit);
    }

    @Override
    public List<Guide> getHotGuides(int limit) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getStatus, 1)
               .eq(Guide::getDeleted, 0)
               .orderByDesc(Guide::getViewCount)
               .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public IPage<Guide> getUserGuides(Page<Guide> page, Long userId, Integer status) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getAuthorId, userId)
               .eq(Guide::getDeleted, 0);
        if (status != null) {
            wrapper.eq(Guide::getStatus, status);
        }
        wrapper.orderByDesc(Guide::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional
    public Guide publishGuide(Guide guide) {
        // 获取作者信息
        User user = userMapper.selectById(guide.getAuthorId());
        if (user != null) {
            guide.setAuthorName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            guide.setAuthorAvatar(user.getAvatar());
        }

        guide.setViewCount(0);
        guide.setLikeCount(0);
        guide.setCommentCount(0);
        guide.setDeleted(0);
        save(guide);
        return guide;
    }

    @Override
    @Transactional
    public void updateGuide(Guide guide, Long userId) {
        // 验证攻略归属
        Guide existing = getById(guide.getId());
        if (existing == null || !existing.getAuthorId().equals(userId)) {
            throw new RuntimeException("攻略不存在或无权修改");
        }

        // 只更新允许修改的字段
        existing.setTitle(guide.getTitle());
        existing.setCoverImage(guide.getCoverImage());
        existing.setContent(guide.getContent());
        existing.setGuideType(guide.getGuideType());
        existing.setScenicIds(guide.getScenicIds());
        existing.setTags(guide.getTags());
        existing.setStatus(0); // 修改后重新审核

        updateById(existing);
    }

    @Override
    @Transactional
    public void deleteGuide(Long id, Long userId) {
        Guide guide = getById(id);
        if (guide == null || !guide.getAuthorId().equals(userId)) {
            throw new RuntimeException("攻略不存在或无权删除");
        }

        // 逻辑删除
        guide.setDeleted(1);
        updateById(guide);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        LambdaUpdateWrapper<Guide> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Guide::getId, id)
               .setSql("view_count = view_count + 1");
        update(wrapper);
    }

    @Override
    @Transactional
    public void incrementLikeCount(Long id) {
        LambdaUpdateWrapper<Guide> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Guide::getId, id)
               .setSql("like_count = like_count + 1");
        update(wrapper);
    }
}
