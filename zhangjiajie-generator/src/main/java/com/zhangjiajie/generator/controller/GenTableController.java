package com.zhangjiajie.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.generator.entity.GenTable;
import com.zhangjiajie.generator.service.GenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器控制器
 * 提供表导入、配置编辑、代码预览、代码生成下载等功能
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Slf4j
@RestController
@RequestMapping("/api/gen")
@RequiredArgsConstructor
@Tag(name = "代码生成器", description = "代码生成器相关接口")
public class GenTableController {

    private final GenTableService genTableService;

    // ==================== 表管理 ====================

    /**
     * 分页查询已导入的表列表
     */
    @GetMapping("/table/page")
    @Operation(summary = "分页查询已导入的表列表")
    public Result<IPage<GenTable>> getTablePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String tableComment) {
        Page<GenTable> page = new Page<>(current, size);
        IPage<GenTable> result = genTableService.selectGenTablePage(page, tableName, tableComment);
        return Result.success(result);
    }

    /**
     * 查询数据库表列表（未导入的）
     */
    @GetMapping("/db/list")
    @Operation(summary = "查询数据库表列表", description = "查询数据库中未导入的表")
    public Result<List<Map<String, Object>>> getDbTableList(
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String tableComment) {
        List<Map<String, Object>> list = genTableService.selectDbTableList(tableName, tableComment);
        return Result.success(list);
    }

    /**
     * 获取表详情
     */
    @GetMapping("/table/{tableId}")
    @Operation(summary = "获取表详情", description = "获取表配置及字段信息")
    public Result<GenTable> getTableById(
            @PathVariable @Parameter(description = "表ID") Long tableId) {
        GenTable genTable = genTableService.selectGenTableById(tableId);
        return Result.success(genTable);
    }

    /**
     * 导入表结构
     */
    @PostMapping("/table/import")
    @Operation(summary = "导入表结构", description = "从数据库导入表结构")
    public Result<Void> importTable(
            @RequestBody @Parameter(description = "表名列表") List<String> tableNames) {
        // TODO: 从Security获取当前用户
        String operName = "admin";
        genTableService.importTableList(tableNames, operName);
        return Result.success("导入成功");
    }

    /**
     * 更新表配置
     */
    @PutMapping("/table")
    @Operation(summary = "更新表配置")
    public Result<Void> updateTable(@RequestBody GenTable genTable) {
        genTableService.updateGenTable(genTable);
        return Result.success("更新成功");
    }

    /**
     * 删除表
     */
    @DeleteMapping("/table/{tableIds}")
    @Operation(summary = "删除表", description = "删除表及其字段配置")
    public Result<Void> deleteTable(
            @PathVariable @Parameter(description = "表ID，多个用逗号分隔") List<Long> tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return Result.success("删除成功");
    }

    /**
     * 同步数据库表结构
     */
    @PostMapping("/table/sync/{tableId}")
    @Operation(summary = "同步数据库", description = "同步数据库表结构到配置")
    public Result<Void> syncDb(
            @PathVariable @Parameter(description = "表ID") Long tableId) {
        genTableService.syncDb(tableId);
        return Result.success("同步成功");
    }

    // ==================== 代码生成 ====================

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    @Operation(summary = "预览代码", description = "预览生成的代码")
    public Result<Map<String, String>> previewCode(
            @PathVariable @Parameter(description = "表ID") Long tableId) {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return Result.success(dataMap);
    }

    /**
     * 下载代码（单表）
     */
    @GetMapping("/download/{tableId}")
    @Operation(summary = "下载代码", description = "生成代码并下载ZIP包")
    public void downloadCode(
            @PathVariable @Parameter(description = "表ID") Long tableId,
            HttpServletResponse response) throws IOException {
        byte[] data = genTableService.downloadCode(tableId);
        genCode(response, data);
    }

    /**
     * 批量下载代码
     */
    @GetMapping("/download/batch")
    @Operation(summary = "批量下载代码", description = "批量生成代码并下载ZIP包")
    public void downloadCodeBatch(
            @RequestParam @Parameter(description = "表ID列表") List<Long> tableIds,
            HttpServletResponse response) throws IOException {
        byte[] data = genTableService.downloadCodeBatch(tableIds);
        genCode(response, data);
    }

    /**
     * 生成代码（写入zip）
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
