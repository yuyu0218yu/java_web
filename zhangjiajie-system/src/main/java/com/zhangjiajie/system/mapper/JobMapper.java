package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.entity.Job;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务Mapper接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {
}
