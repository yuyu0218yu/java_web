package com.zhangjiajie.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.generator.entity.GenTable;
import com.zhangjiajie.generator.entity.GenTableColumn;
import com.zhangjiajie.generator.mapper.GenTableColumnMapper;
import com.zhangjiajie.generator.mapper.GenTableMapper;
import com.zhangjiajie.generator.service.GenTableService;
import com.zhangjiajie.generator.util.GenUtils;
import com.zhangjiajie.generator.util.VelocityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成业务表 Service 实现
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable>
        implements GenTableService {

    private final GenTableMapper genTableMapper;
    private final GenTableColumnMapper genTableColumnMapper;

    @Override
    public IPage<GenTable> selectGenTablePage(Page<GenTable> page, String tableName, String tableComment) {
        LambdaQueryWrapper<GenTable> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(tableName)) {
            wrapper.like(GenTable::getTableName, tableName);
        }
        if (StringUtils.hasText(tableComment)) {
            wrapper.like(GenTable::getTableComment, tableComment);
        }
        wrapper.orderByDesc(GenTable::getUpdateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectDbTableList(String tableName, String tableComment) {
        return genTableMapper.selectDbTableList(tableName, tableComment);
    }

    @Override
    public List<Map<String, Object>> selectDbTableListByNames(List<String> tableNames) {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    @Override
    public GenTable selectGenTableById(Long tableId) {
        GenTable genTable = this.getById(tableId);
        if (genTable != null) {
            List<GenTableColumn> columns = genTableColumnMapper.selectByTableId(tableId);
            genTable.setColumns(columns);
            // 设置主键列
            for (GenTableColumn column : columns) {
                if (column.isPk()) {
                    genTable.setPkColumn(column);
                    break;
                }
            }
        }
        return genTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTableList(List<String> tableNames, String operName) {
        List<Map<String, Object>> tableList = genTableMapper.selectDbTableListByNames(tableNames);

        for (Map<String, Object> tableMap : tableList) {
            String tableName = (String) tableMap.get("tableName");
            String tableComment = (String) tableMap.get("tableComment");

            // 创建表配置
            GenTable genTable = new GenTable();
            genTable.setTableName(tableName);
            genTable.setTableComment(tableComment);
            genTable.setClassName(GenUtils.tableNameToClassName(tableName));
            genTable.setPackageName("com.zhangjiajie.system");
            genTable.setModuleName(GenUtils.getModuleName(tableName));
            genTable.setBusinessName(GenUtils.getBusinessName(tableName));
            genTable.setFunctionName(tableComment);
            genTable.setFunctionAuthor(operName);
            genTable.setTplCategory(GenTable.TPL_CRUD);
            genTable.setTplWebType("element-plus");
            genTable.setGenType(GenTable.GEN_TYPE_ZIP);
            genTable.setCreateBy(operName);
            genTable.setCreateTime(LocalDateTime.now());
            genTable.setUpdateBy(operName);
            genTable.setUpdateTime(LocalDateTime.now());

            this.save(genTable);

            // 查询并保存列信息
            List<Map<String, Object>> columnList = genTableMapper.selectDbTableColumnsByName(tableName);
            int sort = 0;
            for (Map<String, Object> columnMap : columnList) {
                GenTableColumn column = GenUtils.initColumnField(columnMap, genTable.getTableId());
                column.setSort(sort++);
                column.setCreateBy(operName);
                column.setCreateTime(LocalDateTime.now());
                genTableColumnMapper.insert(column);
            }

            log.info("导入表 {} 成功", tableName);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGenTable(GenTable genTable) {
        genTable.setUpdateTime(LocalDateTime.now());
        this.updateById(genTable);

        // 更新字段
        if (genTable.getColumns() != null) {
            for (GenTableColumn column : genTable.getColumns()) {
                column.setUpdateTime(LocalDateTime.now());
                genTableColumnMapper.updateById(column);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGenTableByIds(List<Long> tableIds) {
        // 删除字段
        genTableColumnMapper.deleteByTableIds(tableIds);
        // 删除表
        this.removeByIds(tableIds);
    }

    @Override
    public Map<String, String> previewCode(Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        GenTable table = selectGenTableById(tableId);
        if (table == null) {
            return dataMap;
        }

        // 初始化Velocity
        VelocityUtils.initVelocity();

        // 设置模板变量
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());

        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, StandardCharsets.UTF_8.name());
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }

        return dataMap;
    }

    @Override
    public byte[] downloadCode(Long tableId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        GenTable table = selectGenTableById(tableId);
        if (table != null) {
            generatorCode(table, zip);
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] downloadCodeBatch(List<Long> tableIds) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (Long tableId : tableIds) {
            GenTable table = selectGenTableById(tableId);
            if (table != null) {
                generatorCode(table, zip);
            }
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncDb(Long tableId) {
        GenTable genTable = selectGenTableById(tableId);
        if (genTable == null) {
            throw new RuntimeException("表不存在");
        }

        List<GenTableColumn> tableColumns = Optional.ofNullable(genTable.getColumns()).orElse(Collections.emptyList());
        Map<String, GenTableColumn> tableColumnMap = new HashMap<>();
        for (GenTableColumn column : tableColumns) {
            tableColumnMap.put(column.getColumnName(), column);
        }

        // 查询数据库当前字段
        List<Map<String, Object>> dbColumns = genTableMapper.selectDbTableColumnsByName(genTable.getTableName());

        if (dbColumns == null || dbColumns.isEmpty()) {
            throw new RuntimeException("同步数据失败，原表结构不存在");
        }

        List<String> dbColumnNames = new ArrayList<>();
        int sort = 0;
        for (Map<String, Object> columnMap : dbColumns) {
            String columnName = (String) columnMap.get("columnName");
            dbColumnNames.add(columnName);

            if (tableColumnMap.containsKey(columnName)) {
                // 更新已有字段
                GenTableColumn column = tableColumnMap.get(columnName);
                GenUtils.updateColumnField(column, columnMap);
                column.setSort(sort++);
                genTableColumnMapper.updateById(column);
            } else {
                // 新增字段
                GenTableColumn column = GenUtils.initColumnField(columnMap, tableId);
                column.setSort(sort++);
                genTableColumnMapper.insert(column);
            }
        }

        // 删除已不存在的字段
        for (GenTableColumn column : tableColumns) {
            if (!dbColumnNames.contains(column.getColumnName())) {
                genTableColumnMapper.deleteById(column.getColumnId());
            }
        }
    }

    /**
     * 生成代码到ZIP
     */
    private void generatorCode(GenTable table, ZipOutputStream zip) {
        // 初始化Velocity
        VelocityUtils.initVelocity();

        // 设置模板变量
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());

        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, StandardCharsets.UTF_8.name());
            tpl.merge(context, sw);

            try {
                // 添加到zip
                String fileName = VelocityUtils.getFileName(template, table);
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(sw.toString(), zip, StandardCharsets.UTF_8.name());
                zip.flush();
                zip.closeEntry();
            } catch (Exception e) {
                log.error("渲染模板失败: {}", template, e);
            }
        }
    }
}
