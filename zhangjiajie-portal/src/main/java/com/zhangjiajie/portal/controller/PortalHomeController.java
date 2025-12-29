package com.zhangjiajie.portal.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.portal.dto.HomeDataDTO;
import com.zhangjiajie.portal.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页Controller
 */
@RestController
@RequestMapping("/api/portal/home")
@RequiredArgsConstructor
@Tag(name = "用户端-首页", description = "首页相关接口")
public class PortalHomeController {

    private final HomeService homeService;

    /**
     * 获取首页数据
     */
    @GetMapping
    @Operation(summary = "获取首页数据")
    public Result<HomeDataDTO> getHomeData() {
        HomeDataDTO data = homeService.getHomeData();
        return Result.success(data);
    }
}
