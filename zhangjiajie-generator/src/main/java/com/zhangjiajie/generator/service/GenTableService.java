package com.zhangjiajie.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.generator.entity.GenTable;

import java.util.List;
import java.util.Map;

/**
 * 代码生成业务表 Service
 *
 * @author yushuang
 * @since 2025-12-06
 */
public interface GenTableService extends IService<GenTable> {

    /**
     * 分页查询已导入的表列表
     *
     * @param page 分页参数
     * @param tableName 表名
     * @param tableComment 表注释
     * @return 分页结果
     */
    IPage<GenTable> selectGenTablePage(Page<GenTable> page, String tableName, String tableComment);

    /**
     * 查询数据库中的表列表（未导入的）
     *
     * @param tableName 表名
     * @param tableComment 表注释
     * @return 表列表
     */
    List<Map<String, Object>> selectDbTableList(String tableName, String tableComment);

    /**
     * 根据表名查询数据库表信息
     *
     * @param tableNames 表名列表
     * @return 表信息列表
     */
    List<Map<String, Object>> selectDbTableListByNames(List<String> tableNames);

    /**
     * 查询表详情（包含字段信息）
     *
     * @param tableId 表ID
     * @return 表详情
     */
    GenTable selectGenTableById(Long tableId);

    /**
     * 导入表结构
     *
     * @param tableNames 表名列表
     * @param operName 操作人
     */
    void importTableList(List<String> tableNames, String operName);

    /**
     * 更新表配置
     *
     * @param genTable 表配置
     */
    void updateGenTable(GenTable genTable);

    /**
     * 删除表（包含字段）
     *
     * @param tableIds 表ID列表
     */
    void deleteGenTableByIds(List<Long> tableIds);

    /**
     * 预览代码
     *
     * @param tableId 表ID
     * @return 预览代码Map（文件名 -> 代码内容）
     */
    Map<String, String> previewCode(Long tableId);

    /**
     * 生成代码（下载ZIP）
     *
     * @param tableId 表ID
     * @return ZIP字节数组
     */
    byte[] downloadCode(Long tableId);

    /**
     * 批量生成代码（下载ZIP）
     *
     * @param tableIds 表ID列表
     * @return ZIP字节数组
     */
    byte[] downloadCodeBatch(List<Long> tableIds);

    /**
     * 同步数据库表结构
     *
     * @param tableId 表ID
     */
    void syncDb(Long tableId);
}
