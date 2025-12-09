package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告Mapper接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
