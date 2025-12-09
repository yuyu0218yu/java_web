package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.entity.JobLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 定时任务日志Mapper接口
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

    /**
     * 删除指定时间之前的日志
     */
    @Delete("UPDATE sys_job_log SET deleted = 1 WHERE create_time < #{beforeDate} AND deleted = 0")
    int deleteLogsBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
}
