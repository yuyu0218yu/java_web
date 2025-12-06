package com.zhangjiajie.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 仪表板统计数据DTO
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Data
public class DashboardStatistics {

    /**
     * 统计卡片数据
     */
    private StatsData stats;

    /**
     * 用户增长趋势数据
     */
    private List<ChartData> userGrowthData;

    /**
     * 权限分布数据
     */
    private List<PermissionDistribution> permissionDistribution;

    /**
     * 最近活动数据
     */
    private List<RecentActivity> recentActivities;

    /**
     * 统计卡片数据
     */
    @Data
    public static class StatsData {
        /**
         * 总用户数
         */
        private Long totalUsers;

        /**
         * 活跃用户数（状态为启用的用户）
         */
        private Long activeUsers;

        /**
         * 角色数量
         */
        private Long roleCount;

        /**
         * 权限数量
         */
        private Long permissionCount;

        /**
         * 用户增长趋势（百分比，与上月比较）
         */
        private Double userTrend;

        /**
         * 活跃用户趋势（百分比）
         */
        private Double activeTrend;

        /**
         * 角色趋势（百分比）
         */
        private Double roleTrend;

        /**
         * 权限趋势（百分比）
         */
        private Double permissionTrend;
    }

    /**
     * 图表数据
     */
    @Data
    public static class ChartData {
        /**
         * 标签（如月份）
         */
        private String label;

        /**
         * 百分比值（用于柱状图高度）
         */
        private Integer value;

        /**
         * 实际数量
         */
        private Long count;
    }

    /**
     * 权限分布数据
     */
    @Data
    public static class PermissionDistribution {
        /**
         * 权限类型名称
         */
        private String name;

        /**
         * 数量
         */
        private Long value;

        /**
         * 颜色
         */
        private String color;

        /**
         * 百分比
         */
        private Integer percent;
    }

    /**
     * 最近活动数据
     */
    @Data
    public static class RecentActivity {
        /**
         * 时间
         */
        private String time;

        /**
         * 用户名
         */
        private String user;

        /**
         * 操作
         */
        private String action;

        /**
         * 状态：成功/失败
         */
        private String status;
    }
}
