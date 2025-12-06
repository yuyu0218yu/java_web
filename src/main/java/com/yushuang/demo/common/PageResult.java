package com.yushuang.demo.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果封装
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    public PageResult() {
    }

    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = size > 0 ? (total + size - 1) / size : 0; // 防止除以零
    }

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        return new PageResult<>(records, total, current, size);
    }

    /**
     * 从MyBatis-Plus的IPage创建分页结果
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 创建空的分页结果
     */
    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(Collections.emptyList(), 0L, current, size);
    }

    /**
     * 判断是否有数据
     */
    public boolean hasRecords() {
        return records != null && !records.isEmpty();
    }

    /**
     * 获取数据数量
     */
    public int getRecordCount() {
        return records != null ? records.size() : 0;
    }

    /**
     * 判断是否为第一页
     */
    public boolean isFirstPage() {
        return current == null || current <= 1;
    }

    /**
     * 判断是否为最后一页
     */
    public boolean isLastPage() {
        return current == null || pages == null || current >= pages;
    }

    /**
     * 获取下一页页码
     */
    public Long getNextPage() {
        return isLastPage() ? null : (current == null ? 2 : current + 1);
    }

    /**
     * 获取上一页页码
     */
    public Long getPreviousPage() {
        return isFirstPage() ? null : (current == null ? null : current - 1);
    }
}