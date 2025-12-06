package com.yushuang.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yushuang.demo.dto.DashboardStatistics;
import com.yushuang.demo.entity.OperationLog;
import com.yushuang.demo.entity.Permission;
import com.yushuang.demo.entity.Role;
import com.yushuang.demo.entity.User;
import com.yushuang.demo.mapper.OperationLogMapper;
import com.yushuang.demo.mapper.PermissionMapper;
import com.yushuang.demo.mapper.RoleMapper;
import com.yushuang.demo.mapper.UserMapper;
import com.yushuang.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 仪表板服务实现类
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    /**
     * 百分比计算精度乘数
     */
    private static final double PERCENTAGE_PRECISION_MULTIPLIER = 1000.0;

    /**
     * 图表柱状条最小可见百分比
     */
    private static final int MIN_CHART_VISIBILITY_PERCENT = 5;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    public DashboardStatistics getStatistics(String period) {
        DashboardStatistics statistics = new DashboardStatistics();
        
        // 获取统计卡片数据
        statistics.setStats(getStatsData());
        
        // 获取用户增长趋势数据（根据周期）
        statistics.setUserGrowthData(getUserGrowthData(period));
        
        // 获取权限分布数据
        statistics.setPermissionDistribution(getPermissionDistribution());
        
        // 获取最近活动数据
        statistics.setRecentActivities(getRecentActivities());
        
        return statistics;
    }

    /**
     * 获取统计卡片数据
     */
    private DashboardStatistics.StatsData getStatsData() {
        DashboardStatistics.StatsData stats = new DashboardStatistics.StatsData();
        
        // 总用户数
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getDeleted, 0);
        long totalUsers = userMapper.selectCount(userWrapper);
        stats.setTotalUsers(totalUsers);
        
        // 活跃用户数（状态为启用）
        LambdaQueryWrapper<User> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(User::getDeleted, 0);
        activeWrapper.eq(User::getStatus, User.Status.ENABLED.getCode());
        long activeUsers = userMapper.selectCount(activeWrapper);
        stats.setActiveUsers(activeUsers);
        
        // 角色数量
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getDeleted, 0);
        long roleCount = roleMapper.selectCount(roleWrapper);
        stats.setRoleCount(roleCount);
        
        // 权限数量
        LambdaQueryWrapper<Permission> permWrapper = new LambdaQueryWrapper<>();
        permWrapper.eq(Permission::getDeleted, 0);
        long permissionCount = permissionMapper.selectCount(permWrapper);
        stats.setPermissionCount(permissionCount);
        
        // 计算所有趋势
        stats.setUserTrend(calculateUserTrend());
        stats.setActiveTrend(calculateActiveUserTrend());
        stats.setRoleTrend(calculateRoleTrend());
        stats.setPermissionTrend(calculatePermissionTrend());
        
        return stats;
    }

    /**
     * 计算用户增长趋势
     */
    private Double calculateUserTrend() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        // 本月新增用户
        LambdaQueryWrapper<User> currentWrapper = new LambdaQueryWrapper<>();
        currentWrapper.eq(User::getDeleted, 0);
        currentWrapper.ge(User::getCreateTime, lastMonth);
        long currentMonthUsers = userMapper.selectCount(currentWrapper);
        
        // 上月新增用户
        LambdaQueryWrapper<User> lastWrapper = new LambdaQueryWrapper<>();
        lastWrapper.eq(User::getDeleted, 0);
        lastWrapper.ge(User::getCreateTime, lastMonth.minusMonths(1));
        lastWrapper.lt(User::getCreateTime, lastMonth);
        long lastMonthUsers = userMapper.selectCount(lastWrapper);
        
        return calculateTrendPercentage(currentMonthUsers, lastMonthUsers);
    }

    /**
     * 计算活跃用户趋势
     */
    private Double calculateActiveUserTrend() {
        // 由于活跃用户是基于当前状态的，不跟踪历史变化，返回0
        // 实际项目中可以通过登录日志来计算活跃用户趋势
        return 0.0;
    }

    /**
     * 计算角色趋势
     */
    private Double calculateRoleTrend() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        // 本月新增角色
        LambdaQueryWrapper<Role> currentWrapper = new LambdaQueryWrapper<>();
        currentWrapper.eq(Role::getDeleted, 0);
        currentWrapper.ge(Role::getCreateTime, lastMonth);
        long currentMonthRoles = roleMapper.selectCount(currentWrapper);
        
        // 上月新增角色
        LambdaQueryWrapper<Role> lastWrapper = new LambdaQueryWrapper<>();
        lastWrapper.eq(Role::getDeleted, 0);
        lastWrapper.ge(Role::getCreateTime, lastMonth.minusMonths(1));
        lastWrapper.lt(Role::getCreateTime, lastMonth);
        long lastMonthRoles = roleMapper.selectCount(lastWrapper);
        
        return calculateTrendPercentage(currentMonthRoles, lastMonthRoles);
    }

    /**
     * 计算权限趋势
     */
    private Double calculatePermissionTrend() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonth = now.minusMonths(1);
        
        // 本月新增权限
        LambdaQueryWrapper<Permission> currentWrapper = new LambdaQueryWrapper<>();
        currentWrapper.eq(Permission::getDeleted, 0);
        currentWrapper.ge(Permission::getCreateTime, lastMonth);
        long currentMonthPermissions = permissionMapper.selectCount(currentWrapper);
        
        // 上月新增权限
        LambdaQueryWrapper<Permission> lastWrapper = new LambdaQueryWrapper<>();
        lastWrapper.eq(Permission::getDeleted, 0);
        lastWrapper.ge(Permission::getCreateTime, lastMonth.minusMonths(1));
        lastWrapper.lt(Permission::getCreateTime, lastMonth);
        long lastMonthPermissions = permissionMapper.selectCount(lastWrapper);
        
        return calculateTrendPercentage(currentMonthPermissions, lastMonthPermissions);
    }

    /**
     * 计算趋势百分比
     * @param currentValue 当前值
     * @param previousValue 上月值
     * @return 趋势百分比
     */
    private Double calculateTrendPercentage(long currentValue, long previousValue) {
        if (previousValue == 0) {
            return currentValue > 0 ? 100.0 : 0.0;
        }
        return Math.round((double)(currentValue - previousValue) / previousValue * PERCENTAGE_PRECISION_MULTIPLIER) / 10.0;
    }

    /**
     * 获取用户增长趋势数据（根据周期）
     * @param period 周期：week（最近7天）、month（最近12个月）、year（最近5年）
     */
    private List<DashboardStatistics.ChartData> getUserGrowthData(String period) {
        if ("week".equalsIgnoreCase(period)) {
            return getUserGrowthDataByWeek();
        } else if ("year".equalsIgnoreCase(period)) {
            return getUserGrowthDataByYear();
        } else {
            return getUserGrowthDataByMonth();
        }
    }

    /**
     * 获取用户增长趋势数据（最近7天）
     */
    private List<DashboardStatistics.ChartData> getUserGrowthDataByWeek() {
        List<DashboardStatistics.ChartData> chartDataList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        long maxCount = 0;
        long[] dailyCounts = new long[7];
        String[] labels = new String[7];
        
        // 获取过去7天的数据
        for (int i = 6; i >= 0; i--) {
            LocalDate dayStart = now.minusDays(i);
            LocalDate dayEnd = dayStart.plusDays(1);
            
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getDeleted, 0);
            wrapper.lt(User::getCreateTime, dayEnd.atStartOfDay());
            
            long count = userMapper.selectCount(wrapper);
            dailyCounts[6 - i] = count;
            labels[6 - i] = dayStart.format(DateTimeFormatter.ofPattern("M/d"));
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        return buildChartDataList(dailyCounts, labels, maxCount);
    }

    /**
     * 获取用户增长趋势数据（最近12个月）
     */
    private List<DashboardStatistics.ChartData> getUserGrowthDataByMonth() {
        List<DashboardStatistics.ChartData> chartDataList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        long maxCount = 0;
        long[] monthlyCounts = new long[12];
        String[] labels = new String[12];
        
        // 获取过去12个月的数据
        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1);
            
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getDeleted, 0);
            wrapper.lt(User::getCreateTime, monthEnd.atStartOfDay());
            
            long count = userMapper.selectCount(wrapper);
            monthlyCounts[11 - i] = count;
            labels[11 - i] = monthStart.format(DateTimeFormatter.ofPattern("M月"));
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        return buildChartDataList(monthlyCounts, labels, maxCount);
    }

    /**
     * 获取用户增长趋势数据（最近5年）
     */
    private List<DashboardStatistics.ChartData> getUserGrowthDataByYear() {
        List<DashboardStatistics.ChartData> chartDataList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        long maxCount = 0;
        long[] yearlyCounts = new long[5];
        String[] labels = new String[5];
        
        // 获取过去5年的数据
        for (int i = 4; i >= 0; i--) {
            LocalDate yearStart = now.minusYears(i).withDayOfYear(1);
            LocalDate yearEnd = yearStart.plusYears(1);
            
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getDeleted, 0);
            wrapper.lt(User::getCreateTime, yearEnd.atStartOfDay());
            
            long count = userMapper.selectCount(wrapper);
            yearlyCounts[4 - i] = count;
            labels[4 - i] = yearStart.format(DateTimeFormatter.ofPattern("yyyy年"));
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        return buildChartDataList(yearlyCounts, labels, maxCount);
    }

    /**
     * 构建图表数据列表
     */
    private List<DashboardStatistics.ChartData> buildChartDataList(long[] counts, String[] labels, long maxCount) {
        List<DashboardStatistics.ChartData> chartDataList = new ArrayList<>();
        
        // 如果maxCount为0，设置默认值避免除零错误
        if (maxCount == 0) {
            maxCount = 1;
        }
        
        // 构建图表数据
        for (int i = 0; i < counts.length; i++) {
            DashboardStatistics.ChartData chartData = new DashboardStatistics.ChartData();
            chartData.setLabel(labels[i]);
            chartData.setCount(counts[i]);
            // 计算百分比值（相对于最大值的百分比）
            int percentage = (int) Math.round((double) counts[i] / maxCount * 100);
            chartData.setValue(Math.max(percentage, MIN_CHART_VISIBILITY_PERCENT));
            chartDataList.add(chartData);
        }
        
        return chartDataList;
    }

    /**
     * 获取权限分布数据
     */
    private List<DashboardStatistics.PermissionDistribution> getPermissionDistribution() {
        List<DashboardStatistics.PermissionDistribution> distributions = new ArrayList<>();
        
        // 菜单权限数量
        LambdaQueryWrapper<Permission> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(Permission::getDeleted, 0);
        menuWrapper.eq(Permission::getResourceType, Permission.PermissionType.MENU.getCode());
        long menuCount = permissionMapper.selectCount(menuWrapper);
        
        // 按钮权限数量
        LambdaQueryWrapper<Permission> buttonWrapper = new LambdaQueryWrapper<>();
        buttonWrapper.eq(Permission::getDeleted, 0);
        buttonWrapper.eq(Permission::getResourceType, Permission.PermissionType.BUTTON.getCode());
        long buttonCount = permissionMapper.selectCount(buttonWrapper);
        
        // 接口权限数量
        LambdaQueryWrapper<Permission> apiWrapper = new LambdaQueryWrapper<>();
        apiWrapper.eq(Permission::getDeleted, 0);
        apiWrapper.eq(Permission::getResourceType, Permission.PermissionType.API.getCode());
        long apiCount = permissionMapper.selectCount(apiWrapper);
        
        long total = menuCount + buttonCount + apiCount;
        
        if (total > 0) {
            DashboardStatistics.PermissionDistribution menuDist = new DashboardStatistics.PermissionDistribution();
            menuDist.setName("菜单权限");
            menuDist.setValue(menuCount);
            menuDist.setColor("#409EFF");
            menuDist.setPercent((int) Math.round((double) menuCount / total * 100));
            distributions.add(menuDist);
            
            DashboardStatistics.PermissionDistribution buttonDist = new DashboardStatistics.PermissionDistribution();
            buttonDist.setName("按钮权限");
            buttonDist.setValue(buttonCount);
            buttonDist.setColor("#67C23A");
            buttonDist.setPercent((int) Math.round((double) buttonCount / total * 100));
            distributions.add(buttonDist);
            
            DashboardStatistics.PermissionDistribution apiDist = new DashboardStatistics.PermissionDistribution();
            apiDist.setName("接口权限");
            apiDist.setValue(apiCount);
            apiDist.setColor("#E6A23C");
            apiDist.setPercent((int) Math.round((double) apiCount / total * 100));
            distributions.add(apiDist);
        }
        
        return distributions;
    }

    /**
     * 获取最近活动数据
     */
    private List<DashboardStatistics.RecentActivity> getRecentActivities() {
        List<DashboardStatistics.RecentActivity> activities = new ArrayList<>();
        
        // 获取最近10条操作日志
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OperationLog::getDeleted, 0);
        wrapper.orderByDesc(OperationLog::getCreateTime);
        wrapper.last("LIMIT 10");
        
        List<OperationLog> logs = operationLogMapper.selectList(wrapper);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (OperationLog log : logs) {
            DashboardStatistics.RecentActivity activity = new DashboardStatistics.RecentActivity();
            activity.setTime(log.getCreateTime() != null ? log.getCreateTime().format(formatter) : "");
            activity.setUser(log.getUsername() != null ? log.getUsername() : "未知");
            activity.setAction(log.getOperation() != null ? log.getOperation() : "未知操作");
            activity.setStatus(log.getStatus() != null && log.getStatus() == 1 ? "成功" : "失败");
            activities.add(activity);
        }
        
        return activities;
    }
}
