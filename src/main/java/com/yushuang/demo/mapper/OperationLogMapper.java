package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 分页查询操作日志
     */
    @Select("SELECT * FROM sys_operation_log WHERE deleted = 0 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='operation != null and operation != \"\"'> AND operation LIKE CONCAT('%', #{operation}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "ORDER BY create_time DESC")
    IPage<OperationLog> selectOperationLogPage(Page<OperationLog> page,
                                               @Param("username") String username,
                                               @Param("operation") String operation,
                                               @Param("status") Integer status,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID查询操作日志
     */
    @Select("SELECT * FROM sys_operation_log WHERE deleted = 0 AND user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<OperationLog> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 根据用户名查询操作日志
     */
    @Select("SELECT * FROM sys_operation_log WHERE deleted = 0 AND username = #{username} ORDER BY create_time DESC LIMIT #{limit}")
    List<OperationLog> selectByUsername(@Param("username") String username, @Param("limit") Integer limit);

    /**
     * 删除指定日期之前的日志（日志清理）
     */
    @Select("UPDATE sys_operation_log SET deleted = 1 WHERE deleted = 0 AND create_time &lt; #{beforeDate}")
    int deleteLogsBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 统计操作日志数量
     */
    @Select("SELECT COUNT(*) FROM sys_operation_log WHERE deleted = 0 " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>")
    Long countLogs(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计各操作类型的数量
     */
    @Select("SELECT operation, COUNT(*) as count FROM sys_operation_log WHERE deleted = 0 " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "GROUP BY operation ORDER BY count DESC")
    List<OperationCount> countByOperation(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 操作统计结果
     */
    class OperationCount {
        private String operation;
        private Long count;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
}