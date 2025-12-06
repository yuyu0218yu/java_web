package com.zhangjiajie.generator.controller;

import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.generator.dto.GenerateRequest;
import com.zhangjiajie.generator.service.GeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器控制器
 * 提供Web界面进行代码生成
 */
@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
@Tag(name = "代码生成器")
public class GeneratorController {

    private final GeneratorService generatorService;

    /**
     * 获取所有数据库表信息
     */
    @GetMapping("/tables")
    @Operation(summary = "获取数据库表列表")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<List<Map<String, Object>>> getTableList() {
        return Result.success(generatorService.getTableList());
    }

    /**
     * 获取表的列信息
     */
    @GetMapping("/tables/{tableName}/columns")
    @Operation(summary = "获取表列信息")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<List<Map<String, Object>>> getTableColumns(@PathVariable String tableName) {
        return Result.success(generatorService.getTableColumns(tableName));
    }

    /**
     * 预览生成的代码
     */
    @PostMapping("/preview")
    @Operation(summary = "预览生成代码")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Map<String, String>> previewCode(@RequestBody GenerateRequest request) {
        return Result.success(generatorService.previewCode(request.getTableName(), request.getOptions()));
    }

    /**
     * 执行代码生成
     */
    @PostMapping("/generate")
    @Operation(summary = "生成代码")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<String> generateCode(@RequestBody GenerateRequest request) {
        generatorService.generateCode(request.getTableName(), request.getOptions());
        return Result.success("代码生成成功！");
    }
}
