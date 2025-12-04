package com.yushuang.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yushuang.demo.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志Mapper接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 分页查询登录日志
     */
    @Select("SELECT * FROM sys_login_log WHERE deleted = 0 " +
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='ip != null and ip != \"\"'> AND ip LIKE CONCAT('%', #{ip}, '%') </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "ORDER BY create_time DESC")
    IPage<LoginLog> selectLoginLogPage(Page<LoginLog> page,
                                      @Param("username") String username,
                                      @Param("ip") String ip,
                                      @Param("status") Integer status,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户名查询登录日志
     */
    @Select("SELECT * FROM sys_login_log WHERE deleted = 0 AND username = #{username} ORDER BY create_time DESC LIMIT #{limit}")
    List<LoginLog> selectByUsername(@Param("username") String username, @Param("limit") Integer limit);

    /**
     * 根据IP查询登录日志
     */
    @Select("SELECT * FROM sys_login_log WHERE deleted = 0 AND ip = #{ip} ORDER BY create_time DESC LIMIT #{limit}")
    List<LoginLog> selectByIp(@Param("ip") String ip, @Param("limit") Integer limit);

    /**
     * 删除指定日期之前的日志（日志清理）
     */
    @Select("UPDATE sys_login_log SET deleted = 1 WHERE deleted = 0 AND create_time &lt; #{beforeDate}")
    int deleteLogsBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 统计登录日志数量
     */
    @Select("SELECT COUNT(*) FROM sys_login_log WHERE deleted = 0 " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>")
    Long countLogs(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计登录成功和失败次数
     */
    @Select("SELECT status, COUNT(*) as count FROM sys_login_log WHERE deleted = 0 " +
            "<if test='startTime != null'> AND create_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND create_time &lt;= #{endTime} </if>" +
            "GROUP BY status")
    List<LoginCount> countByStatus(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最近登录失败的用户
     */
    @Select("SELECT username, COUNT(*) as fail_count FROM sys_login_log WHERE deleted = 0 AND status = 0 " +
            "AND create_time &gt;= #{since} GROUP BY username HAVING fail_count &gt; #{threshold} ORDER BY fail_count DESC")
    List<LoginFailCount> selectRecentFailedLogins(@Param("since") LocalDateTime since, @Param("threshold") Integer threshold);

    /**
     * 登录统计结果
     */
    class LoginCount {
        private Integer status;
        private Long count;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }

    /**
     * 登录失败统计结果
     */
    class LoginFailCount {
        private String username;
        private Long failCount;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getFailCount() {
            return failCount;
        }

        public void setFailCount(Long failCount) {
            this.failCount = failCount;
        }
    }
}