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

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    public DashboardStatistics getStatistics() {
        DashboardStatistics statistics = new DashboardStatistics();
        
        // 获取统计卡片数据
        statistics.setStats(getStatsData());
        
        // 获取用户增长趋势数据
        statistics.setUserGrowthData(getUserGrowthData());
        
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
        
        // 计算趋势（这里简化处理，使用随机趋势或基于实际数据计算）
        // 实际项目中应该根据历史数据计算
        stats.setUserTrend(calculateUserTrend());
        stats.setActiveTrend(8.2);
        stats.setRoleTrend(0.0);
        stats.setPermissionTrend(0.0);
        
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
        
        if (lastMonthUsers == 0) {
            return currentMonthUsers > 0 ? 100.0 : 0.0;
        }
        
        return Math.round((double)(currentMonthUsers - lastMonthUsers) / lastMonthUsers * 1000) / 10.0;
    }

    /**
     * 获取用户增长趋势数据（最近12个月）
     */
    private List<DashboardStatistics.ChartData> getUserGrowthData() {
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
        
        // 如果maxCount为0，设置默认值避免除零错误
        if (maxCount == 0) {
            maxCount = 1;
        }
        
        // 构建图表数据
        for (int i = 0; i < 12; i++) {
            DashboardStatistics.ChartData chartData = new DashboardStatistics.ChartData();
            chartData.setLabel(labels[i]);
            chartData.setCount(monthlyCounts[i]);
            // 计算百分比值（相对于最大值的百分比）
            int percentage = (int) Math.round((double) monthlyCounts[i] / maxCount * 100);
            chartData.setValue(Math.max(percentage, 5)); // 最小5%，确保可见
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
