package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.dto.DashboardStatistics;
import com.zhangjiajie.system.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表板控制器
 *
 * @author yushuang
 * @since 2025-12-06
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "仪表板", description = "仪表板统计接口")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取仪表板统计数据
     * @param period 图表周期：week（周）、month（月）、year（年），默认为month
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取仪表板统计数据", description = "获取系统仪表板所需的统计数据，包括用户数、角色数、权限分布等")
    public Result<DashboardStatistics> getStatistics(
            @Parameter(description = "图表周期：week（周）、month（月）、year（年），默认为month")
            @RequestParam(value = "period", defaultValue = "month") String period) {
        try {
            DashboardStatistics statistics = dashboardService.getStatistics(period);
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取仪表板统计数据失败", e);
            return Result.error("获取统计数据失败");
        }
    }
}
