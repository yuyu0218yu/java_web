package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.DictType;
import com.zhangjiajie.system.service.DictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型管理控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@RestController
@RequestMapping("/api/dict/types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "字典类型管理", description = "字典类型相关接口")
public class DictTypeController {

    private final DictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询字典类型")
    @PreAuthorize("hasAuthority('dict:view') or hasRole('SUPER_ADMIN')")
    public Result<Page<DictType>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) Integer status) {

        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dictName), DictType::getDictName, dictName)
               .like(StringUtils.hasText(dictType), DictType::getDictType, dictType)
               .eq(status != null, DictType::getStatus, status)
               .orderByDesc(DictType::getCreateTime);

        Page<DictType> page = dictTypeService.page(new Page<>(current, size), wrapper);
        return Result.success(page);
    }

    /**
     * 获取所有启用的字典类型
     */
    @GetMapping("/list")
    @Operation(summary = "获取字典类型列表")
    @PreAuthorize("hasAuthority('dict:view') or hasRole('SUPER_ADMIN')")
    public Result<List<DictType>> list() {
        List<DictType> list = dictTypeService.listEnabled();
        return Result.success(list);
    }

    /**
     * 获取字典类型详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典类型详情")
    @PreAuthorize("hasAuthority('dict:view') or hasRole('SUPER_ADMIN')")
    public Result<DictType> getById(@PathVariable Long id) {
        DictType dictType = dictTypeService.getById(id);
        if (dictType == null) {
            return Result.error("字典类型不存在");
        }
        return Result.success(dictType);
    }

    /**
     * 创建字典类型
     */
    @PostMapping
    @Operation(summary = "创建字典类型")
    @PreAuthorize("hasAuthority('dict:create') or hasRole('SUPER_ADMIN')")
    public Result<Void> create(@RequestBody DictType dictType) {
        // 检查字典类型是否已存在
        DictType existing = dictTypeService.getByDictType(dictType.getDictType());
        if (existing != null) {
            return Result.error("字典类型 [" + dictType.getDictType() + "] 已存在");
        }

        boolean success = dictTypeService.save(dictType);
        return success ? Result.success("创建成功") : Result.error("创建失败");
    }

    /**
     * 更新字典类型
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新字典类型")
    @PreAuthorize("hasAuthority('dict:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> update(@PathVariable Long id, @RequestBody DictType dictType) {
        DictType existing = dictTypeService.getById(id);
        if (existing == null) {
            return Result.error("字典类型不存在");
        }

        // 如果修改了字典类型编码，检查是否重复
        if (!existing.getDictType().equals(dictType.getDictType())) {
            DictType duplicate = dictTypeService.getByDictType(dictType.getDictType());
            if (duplicate != null) {
                return Result.error("字典类型 [" + dictType.getDictType() + "] 已存在");
            }
        }

        dictType.setId(id);
        boolean success = dictTypeService.updateById(dictType);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典类型")
    @PreAuthorize("hasAuthority('dict:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = dictTypeService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除字典类型
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除字典类型")
    @PreAuthorize("hasAuthority('dict:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        boolean success = dictTypeService.removeByIds(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
