package com.zhangjiajie.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhangjiajie.common.core.Result;
import com.zhangjiajie.system.entity.DictData;
import com.zhangjiajie.system.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据管理控制器
 *
 * @author yushuang
 * @since 2025-12-09
 */
@RestController
@RequestMapping("/api/dict/data")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "字典数据管理", description = "字典数据相关接口")
public class DictDataController {

    private final DictService dictService;

    /**
     * 分页查询字典数据
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询字典数据")
    @PreAuthorize("hasAuthority('dict:view') or hasRole('SUPER_ADMIN')")
    public Result<Page<DictData>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String dictLabel,
            @RequestParam(required = false) Integer status) {

        LambdaQueryWrapper<DictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(dictType), DictData::getDictType, dictType)
               .like(StringUtils.hasText(dictLabel), DictData::getDictLabel, dictLabel)
               .eq(status != null, DictData::getStatus, status)
               .orderByAsc(DictData::getDictSort);

        Page<DictData> page = dictService.page(new Page<>(current, size), wrapper);
        return Result.success(page);
    }

    /**
     * 根据字典类型获取字典数据列表
     */
    @GetMapping("/type/{dictType}")
    @Operation(summary = "根据字典类型获取字典数据")
    public Result<List<DictData>> getByDictType(@PathVariable String dictType) {
        List<DictData> list = dictService.getByDictType(dictType);
        return Result.success(list);
    }

    /**
     * 获取字典数据详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典数据详情")
    @PreAuthorize("hasAuthority('dict:view') or hasRole('SUPER_ADMIN')")
    public Result<DictData> getById(@PathVariable Long id) {
        DictData dictData = dictService.getById(id);
        if (dictData == null) {
            return Result.error("字典数据不存在");
        }
        return Result.success(dictData);
    }

    /**
     * 创建字典数据
     */
    @PostMapping
    @Operation(summary = "创建字典数据")
    @PreAuthorize("hasAuthority('dict:create') or hasRole('SUPER_ADMIN')")
    public Result<Void> create(@RequestBody DictData dictData) {
        // 检查同类型下字典值是否重复
        LambdaQueryWrapper<DictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictData::getDictType, dictData.getDictType())
               .eq(DictData::getDictValue, dictData.getDictValue());
        if (dictService.count(wrapper) > 0) {
            return Result.error("该字典类型下已存在相同的字典值");
        }

        boolean success = dictService.save(dictData);
        return success ? Result.success("创建成功") : Result.error("创建失败");
    }

    /**
     * 更新字典数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新字典数据")
    @PreAuthorize("hasAuthority('dict:update') or hasRole('SUPER_ADMIN')")
    public Result<Void> update(@PathVariable Long id, @RequestBody DictData dictData) {
        DictData existing = dictService.getById(id);
        if (existing == null) {
            return Result.error("字典数据不存在");
        }

        // 如果修改了字典值，检查是否重复
        if (!existing.getDictValue().equals(dictData.getDictValue())) {
            LambdaQueryWrapper<DictData> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DictData::getDictType, dictData.getDictType())
                   .eq(DictData::getDictValue, dictData.getDictValue());
            if (dictService.count(wrapper) > 0) {
                return Result.error("该字典类型下已存在相同的字典值");
            }
        }

        dictData.setId(id);
        boolean success = dictService.updateById(dictData);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典数据")
    @PreAuthorize("hasAuthority('dict:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = dictService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除字典数据
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除字典数据")
    @PreAuthorize("hasAuthority('dict:delete') or hasRole('SUPER_ADMIN')")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        boolean success = dictService.removeByIds(ids);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
