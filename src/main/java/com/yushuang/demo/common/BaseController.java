package com.yushuang.demo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.Method;
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
        return Result.success(PageResult.of(result));
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
        return success ? Result.success("创建成功") : Result.error("创建失败");
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新")
    public Result<Void> update(
            @Parameter(description = "ID") @PathVariable Serializable id,
            @Valid @RequestBody T entity) {
        setEntityId(entity, id);
        boolean success = baseService.updateById(entity);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Serializable id) {
        boolean success = baseService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除")
    public Result<Void> deleteBatch(@RequestBody List<? extends Serializable> ids) {
        boolean success = baseService.removeByIds(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }

    /**
     * 通过反射设置实体ID
     * 支持Long、Integer和String类型的ID
     */
    private void setEntityId(T entity, Serializable id) {
        Class<?> entityClass = entity.getClass();
        Class<?> idClass = id.getClass();
        
        // 首先尝试ID的实际类型
        if (trySetId(entityClass, entity, id, idClass)) {
            return;
        }
        
        // 尝试常见的ID类型（排除已尝试的类型）
        Class<?>[] commonTypes = {Long.class, Integer.class, String.class};
        for (Class<?> idType : commonTypes) {
            if (!idType.equals(idClass) && trySetId(entityClass, entity, id, idType)) {
                return;
            }
        }
        
        throw new RuntimeException("无法找到合适的setId方法");
    }

    /**
     * 尝试使用指定类型设置ID
     */
    private boolean trySetId(Class<?> entityClass, T entity, Serializable id, Class<?> idType) {
        try {
            Method setIdMethod = entityClass.getMethod("setId", idType);
            Object convertedId = convertId(id, idType);
            if (convertedId != null) {
                setIdMethod.invoke(entity, convertedId);
                return true;
            }
        } catch (NoSuchMethodException ignored) {
            // 方法不存在，返回false继续尝试
        } catch (Exception e) {
            throw new RuntimeException("设置实体ID失败", e);
        }
        return false;
    }

    /**
     * 将ID转换为目标类型
     */
    private Object convertId(Serializable id, Class<?> targetType) {
        if (targetType.isInstance(id)) {
            return id;
        }
        
        String idStr = id.toString();
        try {
            if (targetType == Long.class) {
                return Long.parseLong(idStr);
            } else if (targetType == Integer.class) {
                return Integer.parseInt(idStr);
            } else if (targetType == String.class) {
                return idStr;
            }
        } catch (NumberFormatException ignored) {
            // 转换失败，返回null
        }
        return null;
    }
}
