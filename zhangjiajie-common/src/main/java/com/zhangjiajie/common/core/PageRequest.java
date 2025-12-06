package com.zhangjiajie.common.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

/**
 * 通用分页请求参数
 * 用于接收前端的分页查询参数
 *
 * @author yushuang
 */
@Data
@Schema(description = "分页请求参数")
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer size = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段", example = "createTime")
    private String sortField;

    /**
     * 排序方式：asc-升序，desc-降序
     */
    @Schema(description = "排序方式", example = "desc", allowableValues = {"asc", "desc"})
    private String sortOrder = "desc";

    /**
     * 搜索关键字（通用搜索字段）
     */
    @Schema(description = "搜索关键字")
    private String keyword;

    /**
     * 获取排序方式（转换为小写）
     */
    public String getSortOrder() {
        return sortOrder != null ? sortOrder.toLowerCase() : "desc";
    }

    /**
     * 判断是否升序
     */
    public boolean isAsc() {
        return "asc".equalsIgnoreCase(sortOrder);
    }

    /**
     * 判断是否降序
     */
    public boolean isDesc() {
        return "desc".equalsIgnoreCase(sortOrder);
    }

    /**
     * 转换为MyBatis-Plus的Page对象
     */
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toMpPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
    }

    /**
     * 转换为MyBatis-Plus的Page对象（带排序）
     */
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toMpPageWithSort() {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = toMpPage();
        if (sortField != null && !sortField.trim().isEmpty()) {
            if (isAsc()) {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.asc(sortField));
            } else {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.desc(sortField));
            }
        }
        return page;
    }
}
