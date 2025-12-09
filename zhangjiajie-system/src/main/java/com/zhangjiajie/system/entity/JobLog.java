package com.zhangjiajie.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务执行日志实体类
 *
 * @author yushuang
 * @since 2025-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    @TableField("job_id")
    private Long jobId;

    /**
     * 任务名称
     */
    @TableField("job_name")
    private String jobName;

    /**
     * 任务组名
     */
    @TableField("job_group")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @TableField("invoke_target")
    private String invokeTarget;

    /**
     * 日志信息
     */
    @TableField("job_message")
    private String jobMessage;

    /**
     * 执行状态（0失败 1成功）
     */
    @TableField("status")
    private Integer status;

    /**
     * 异常信息
     */
    @TableField("exception_info")
    private String exceptionInfo;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 耗时（毫秒）
     */
    @TableField("elapsed_time")
    private Long elapsedTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
