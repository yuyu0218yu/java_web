package com.yushuang.demo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 通用CRUD Controller基类
 * 提供标准的CRUD接口，子类Controller可以直接继承使用
 *
 * @param <S> Service接口类型
 * @param <T> 实体类型
 * @author yushuang
 */
@Validated
public abstract class BaseController<S extends IService<T>, T> {

    @Autowired
    protected S baseService;

    /**
     * 获取权限前缀，用于权限控制
     * 子类需要重写此方法，返回对应的权限前缀
     * 例如：return "user" 对应权限 user:view, user:create 等
     */
    protected abstract String getPermissionPrefix();

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result<PageResult<T>> page(@Valid PageRequest request) {
        Page<T> page = request.toMpPageWithSort();
        IPage<T> result = baseService.page(page);

        PageResult<T> pageResult = PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );

        return Result.success(pageResult);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public Result<T> getById(@Parameter(description = "ID") @PathVariable Serializable id) {
        T entity = baseService.getById(id);
        if (entity == null) {
            return Result.notFound("记录不存在");
        }
        return Result.success(entity);
    }

    /**
     * 查询所有
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有")
    public Result<List<T>> list() {
        List<T> list = baseService.list();
        return Result.success(list);
    }

    /**
     * 创建
     */
    @PostMapping
    @Operation(summary = "创建")
    public Result<Void> create(@Valid @RequestBody T entity) {
        boolean success = baseService.save(entity);
        if (success) {
            return Result.success("创建成功");
        }
        return Result.error("创建失败");
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新")
    public Result<Void> update(
            @Parameter(description = "ID") @PathVariable Serializable id,
            @Valid @RequestBody T entity) {
        // 通过反射设置ID
        setEntityId(entity, id);
        boolean success = baseService.updateById(entity);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Serializable id) {
        boolean success = baseService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除")
    public Result<Void> deleteBatch(@RequestBody List<? extends Serializable> ids) {
        boolean success = baseService.removeByIds(ids);
        if (success) {
            return Result.success("批量删除成功");
        }
        return Result.error("批量删除失败");
    }

    /**
     * 通过反射设置实体ID
     */
    private void setEntityId(T entity, Serializable id) {
        try {
            java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", id.getClass());
            setIdMethod.invoke(entity, id);
        } catch (Exception e) {
            // 如果没有setId方法或设置失败，尝试Long类型
            try {
                java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", Long.class);
                setIdMethod.invoke(entity, Long.parseLong(id.toString()));
            } catch (Exception ex) {
                throw new RuntimeException("设置实体ID失败", ex);
            }
        }
    }
}
