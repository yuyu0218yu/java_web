package com.yushuang.demo.controller;

import com.yushuang.demo.common.Result;
import com.yushuang.demo.dto.DashboardStatistics;
import com.yushuang.demo.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取仪表板统计数据", description = "获取系统仪表板所需的统计数据，包括用户数、角色数、权限分布等")
    public Result<DashboardStatistics> getStatistics() {
        try {
            DashboardStatistics statistics = dashboardService.getStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取仪表板统计数据失败", e);
            return Result.error("获取统计数据失败");
        }
    }
}
