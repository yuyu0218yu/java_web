package com.zhangjiajie.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.portal.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 轮播图Mapper
 */
@Mapper
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 查询启用的轮播图
     */
    @Select("SELECT * FROM portal_banner " +
            "WHERE deleted = 0 AND status = 1 " +
            "ORDER BY sort_order ASC " +
            "LIMIT #{limit}")
    List<Banner> selectActiveBanners(@Param("limit") int limit);
}
