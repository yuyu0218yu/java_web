package com.zhangjiajie.system.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.DictData;
import com.zhangjiajie.system.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统配置接口
 * 从数据库字典表读取配置
 */
@Tag(name = "系统配置", description = "系统配置相关接口")
@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final DictService dictService;

    /**
     * 获取可用的页面组件列表（从数据库）
     */
    @Operation(summary = "获取组件列表")
    @GetMapping("/components")
    public Result<List<Map<String, String>>> getComponents() {
        return Result.success(getDictOptions("sys_component"));
    }

    /**
     * 获取可用的图标列表（从数据库）
     */
    @Operation(summary = "获取图标列表")
    @GetMapping("/icons")
    public Result<List<Map<String, String>>> getIcons() {
        return Result.success(getDictOptions("sys_icon"));
    }

    /**
     * 获取权限标识列表（从数据库）
     */
    @Operation(summary = "获取权限标识列表")
    @GetMapping("/permissions")
    public Result<List<Map<String, String>>> getPermissions() {
        return Result.success(getDictOptions("sys_permission"));
    }

    /**
     * 根据字典类型获取字典数据（通用接口）
     */
    @Operation(summary = "根据类型获取字典数据")
    @GetMapping("/dict/{dictType}")
    public Result<List<Map<String, String>>> getDictByType(@PathVariable String dictType) {
        return Result.success(getDictOptions(dictType));
    }

    /**
     * 转换字典数据为前端需要的格式
     */
    private List<Map<String, String>> getDictOptions(String dictType) {
        List<DictData> dictList = dictService.getByDictType(dictType);
        return dictList.stream().map(dict -> {
            Map<String, String> option = new LinkedHashMap<>();
            option.put("value", dict.getDictValue());
            option.put("label", dict.getDictLabel());
            return option;
        }).collect(Collectors.toList());
    }
}
