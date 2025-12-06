package com.zhangjiajie.generator.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * SQL脚本生成器
 * 生成建表语句、初始化数据SQL、权限配置SQL等
 *
 * 使用方法：
 *   SqlScriptGenerator.generateCreateTable("User", "用户", columns);
 *   SqlScriptGenerator.generatePermissionSql("User", "用户", "用户管理");
 *   SqlScriptGenerator.generateInitData("User", "用户");
 *
 * 生成内容：
 * 1. 建表语句（从 Entity 结构生成）
 * 2. 初始化数据 SQL（示例数据）
 * 3. 权限配置 SQL（菜单/权限/角色关联）
 * 4. 索引建议 SQL
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class SqlScriptGenerator {

    public static void main(String[] args) {
        // 示例：生成Product模块的权限SQL
        generatePermissionSql("Product", "产品", "产品管理");
    }

    /**
     * 生成完整的SQL脚本（建表+权限+初始化）
     */
    public static void generateAll(String entityName, String entityCnName, String moduleName,
                                   List<Map<String, Object>> columns) {
        try {
            generateCreateTableSql(entityName, entityCnName, columns);
            generatePermissionSql(entityName, entityCnName, moduleName);
            generateInitDataSql(entityName, entityCnName);
            generateIndexSql(entityName, columns);

            printSuccess("SQL脚本生成", entityName,
                    entityName.toLowerCase() + "_create.sql",
                    entityName.toLowerCase() + "_permission.sql",
                    entityName.toLowerCase() + "_init.sql",
                    entityName.toLowerCase() + "_index.sql");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成建表SQL
     */
    public static void generateCreateTable(String entityName, String entityCnName,
                                           List<Map<String, Object>> columns) {
        try {
            generateCreateTableSql(entityName, entityCnName, columns);
            printSuccess("建表SQL生成", entityName, entityName.toLowerCase() + "_create.sql");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成权限SQL
     */
    public static void generatePermissionSql(String entityName, String entityCnName, String moduleName) {
        try {
            generatePermissionConfigSql(entityName, entityCnName, moduleName);
            printSuccess("权限SQL生成", entityName, entityName.toLowerCase() + "_permission.sql");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成初始化数据SQL
     */
    public static void generateInitData(String entityName, String entityCnName) {
        try {
            generateInitDataSql(entityName, entityCnName);
            printSuccess("初始化数据SQL生成", entityName, entityName.toLowerCase() + "_init.sql");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 生成建表SQL
     */
    private static void generateCreateTableSql(String entityName, String entityCnName,
                                               List<Map<String, Object>> columns) throws IOException {
        String tableName = toSnakeCase(entityName);
        StringBuilder sql = new StringBuilder();

        sql.append(String.format("""
                -- ==========================================
                -- %s表 建表语句
                -- 生成时间: %s
                -- 作者: %s
                -- ==========================================

                -- 如果表存在则删除（慎用！生产环境请注释此行）
                -- DROP TABLE IF EXISTS `%s`;

                CREATE TABLE IF NOT EXISTS `%s` (
                """, entityCnName, getCurrentDate(), getAuthor(), tableName, tableName));

        // 主键
        sql.append("    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n");

        // 根据列信息生成字段
        if (columns != null && !columns.isEmpty()) {
            for (Map<String, Object> column : columns) {
                String columnName = (String) column.get("columnName");
                String dataType = (String) column.get("dataType");
                String columnType = (String) column.get("columnType");
                String comment = (String) column.get("columnComment");
                String isNullable = (String) column.get("isNullable");

                // 跳过id字段
                if ("id".equalsIgnoreCase(columnName)) continue;

                String nullableStr = "YES".equalsIgnoreCase(isNullable) ? "NULL" : "NOT NULL";
                String defaultStr = getDefaultValue(dataType);

                sql.append(String.format("    `%s` %s %s%s COMMENT '%s',\n",
                        columnName,
                        columnType != null ? columnType.toUpperCase() : dataType.toUpperCase(),
                        nullableStr,
                        defaultStr,
                        comment != null ? comment : columnName));
            }
        } else {
            // 默认字段模板
            sql.append("""
                        `name` VARCHAR(100) NOT NULL COMMENT '名称',
                        `code` VARCHAR(50) NULL COMMENT '编码',
                        `description` VARCHAR(500) NULL COMMENT '描述',
                        `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 (0-禁用, 1-启用)',
                        `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
                    """);
        }

        // 系统字段
        sql.append("""
                    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 (0-否, 1-是)',
                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                    `create_by` VARCHAR(50) NULL COMMENT '创建人',
                    `update_by` VARCHAR(50) NULL COMMENT '更新人',
                """);

        // 主键和索引
        sql.append(String.format("""
                    PRIMARY KEY (`id`),
                    INDEX `idx_%s_status` (`status`),
                    INDEX `idx_%s_deleted` (`deleted`),
                    INDEX `idx_%s_create_time` (`create_time`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='%s表';

                """, tableName, tableName, tableName, entityCnName));

        writeToSqlFile(entityName.toLowerCase() + "_create.sql", sql.toString());
    }

    /**
     * 生成权限配置SQL
     */
    private static void generatePermissionConfigSql(String entityName, String entityCnName,
                                                    String moduleName) throws IOException {
        String permissionPrefix = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String sql = String.format("""
                -- ==========================================
                -- %s模块 权限配置SQL
                -- 生成时间: %s
                -- 作者: %s
                -- ==========================================

                -- 使用说明：
                -- 1. 根据实际情况修改 parent_id (菜单父级ID)
                -- 2. 根据实际情况修改 sort (排序号)
                -- 3. 执行前请确保不存在重复数据

                -- ==================== 1. 菜单配置 ====================

                -- 插入菜单 (请根据实际父菜单ID修改 parent_id)
                INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `icon`, `sort`, `type`, `visible`, `status`)
                VALUES
                    -- 一级菜单
                    (0, '%s', '/%s', 'Layout', 'menu', 100, 0, 1, 1),
                    -- 二级菜单 (假设父菜单ID为上一条插入的ID，实际使用时需要替换)
                    (LAST_INSERT_ID(), '%s列表', '/%s/list', '/%s/list', 'list', 1, 1, 1, 1);

                -- 记录菜单ID（用于后续权限关联）
                SET @menu_id = LAST_INSERT_ID();

                -- ==================== 2. 权限配置 ====================

                -- 插入操作权限
                INSERT INTO `sys_permission` (`name`, `code`, `description`, `status`) VALUES
                    ('%s查看', '%s:view', '查看%s列表和详情', 1),
                    ('%s新增', '%s:create', '新增%s', 1),
                    ('%s编辑', '%s:update', '编辑%s信息', 1),
                    ('%s删除', '%s:delete', '删除%s', 1),
                    ('%s导出', '%s:export', '导出%s数据', 1),
                    ('%s导入', '%s:import', '导入%s数据', 1);

                -- ==================== 3. 角色-权限关联 ====================

                -- 为管理员角色分配所有权限（假设管理员角色ID为1）
                INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
                SELECT 1, id FROM `sys_permission` WHERE `code` LIKE '%s:%%';

                -- 为普通用户角色分配查看权限（假设普通用户角色ID为2）
                INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
                SELECT 2, id FROM `sys_permission` WHERE `code` = '%s:view';

                -- ==================== 4. 菜单-权限关联 ====================

                -- 将权限关联到菜单
                INSERT INTO `sys_menu_permission` (`menu_id`, `permission_id`)
                SELECT @menu_id, id FROM `sys_permission` WHERE `code` LIKE '%s:%%';

                -- ==================== 5. 数据字典（可选） ====================

                -- %s状态字典
                INSERT INTO `sys_dict_type` (`name`, `type`, `status`, `remark`) VALUES
                    ('%s状态', '%s_status', 1, '%s状态字典');

                INSERT INTO `sys_dict_data` (`dict_type`, `label`, `value`, `sort`, `status`) VALUES
                    ('%s_status', '禁用', '0', 1, 1),
                    ('%s_status', '启用', '1', 2, 1);

                -- ==========================================
                -- 执行完成后请验证：
                -- 1. SELECT * FROM sys_menu WHERE name LIKE '%%%s%%';
                -- 2. SELECT * FROM sys_permission WHERE code LIKE '%s:%%';
                -- 3. SELECT * FROM sys_role_permission WHERE permission_id IN (SELECT id FROM sys_permission WHERE code LIKE '%s:%%');
                -- ==========================================
                """,
                moduleName, getCurrentDate(), getAuthor(),
                moduleName, urlPath, entityCnName, urlPath, urlPath,
                entityCnName, permissionPrefix, entityCnName,
                entityCnName, permissionPrefix, entityCnName,
                entityCnName, permissionPrefix, entityCnName,
                entityCnName, permissionPrefix, entityCnName,
                entityCnName, permissionPrefix, entityCnName,
                entityCnName, permissionPrefix, entityCnName,
                permissionPrefix, permissionPrefix, permissionPrefix,
                entityCnName, entityCnName, permissionPrefix, entityCnName,
                permissionPrefix, permissionPrefix,
                entityCnName, permissionPrefix, permissionPrefix
        );

        writeToSqlFile(entityName.toLowerCase() + "_permission.sql", sql);
    }

    /**
     * 生成初始化数据SQL
     */
    private static void generateInitDataSql(String entityName, String entityCnName) throws IOException {
        String tableName = toSnakeCase(entityName);

        String sql = String.format("""
                -- ==========================================
                -- %s表 初始化数据
                -- 生成时间: %s
                -- 作者: %s
                -- ==========================================

                -- 清空表数据（慎用！生产环境请注释此行）
                -- TRUNCATE TABLE `%s`;

                -- 插入示例数据
                INSERT INTO `%s` (`name`, `code`, `description`, `status`, `sort`, `create_by`) VALUES
                    ('示例%s1', 'SAMPLE_001', '这是第一个示例%s', 1, 1, 'admin'),
                    ('示例%s2', 'SAMPLE_002', '这是第二个示例%s', 1, 2, 'admin'),
                    ('示例%s3', 'SAMPLE_003', '这是第三个示例%s（已禁用）', 0, 3, 'admin');

                -- 验证数据
                -- SELECT * FROM `%s` WHERE deleted = 0;

                """,
                entityCnName, getCurrentDate(), getAuthor(),
                tableName, tableName,
                entityCnName, entityCnName,
                entityCnName, entityCnName,
                entityCnName, entityCnName,
                tableName
        );

        writeToSqlFile(entityName.toLowerCase() + "_init.sql", sql);
    }

    /**
     * 生成索引建议SQL
     */
    private static void generateIndexSql(String entityName, List<Map<String, Object>> columns) throws IOException {
        String tableName = toSnakeCase(entityName);
        StringBuilder sql = new StringBuilder();

        sql.append(String.format("""
                -- ==========================================
                -- %s表 索引优化建议
                -- 生成时间: %s
                -- 作者: %s
                -- ==========================================

                -- 注意：请根据实际查询场景选择性添加索引
                -- 索引过多会影响写入性能，请谨慎使用

                -- ==================== 建议索引 ====================

                """, tableName, getCurrentDate(), getAuthor()));

        if (columns != null) {
            for (Map<String, Object> column : columns) {
                String columnName = (String) column.get("columnName");
                String dataType = (String) column.get("dataType");

                // 跳过已有索引的字段
                if ("id".equalsIgnoreCase(columnName) ||
                    "status".equalsIgnoreCase(columnName) ||
                    "deleted".equalsIgnoreCase(columnName) ||
                    "create_time".equalsIgnoreCase(columnName)) {
                    continue;
                }

                // 根据字段类型和命名规则建议索引
                if (columnName.endsWith("_id") || columnName.endsWith("_code")) {
                    sql.append(String.format("-- 外键/编码字段，建议添加索引\n"));
                    sql.append(String.format("ALTER TABLE `%s` ADD INDEX `idx_%s_%s` (`%s`);\n\n",
                            tableName, tableName, columnName, columnName));
                } else if ("varchar".equalsIgnoreCase(dataType) && columnName.contains("name")) {
                    sql.append(String.format("-- 名称字段，如需模糊查询建议添加前缀索引\n"));
                    sql.append(String.format("-- ALTER TABLE `%s` ADD INDEX `idx_%s_%s` (`%s`(20));\n\n",
                            tableName, tableName, columnName, columnName));
                }
            }
        }

        // 组合索引建议
        sql.append(String.format("""

                -- ==================== 组合索引建议 ====================

                -- 常用查询条件组合索引
                -- ALTER TABLE `%s` ADD INDEX `idx_%s_status_create_time` (`status`, `create_time`);

                -- 分页查询优化索引
                -- ALTER TABLE `%s` ADD INDEX `idx_%s_deleted_create_time` (`deleted`, `create_time` DESC);

                -- ==================== 全文索引（如需全文搜索） ====================

                -- ALTER TABLE `%s` ADD FULLTEXT INDEX `ft_%s_name_desc` (`name`, `description`);

                """, tableName, tableName, tableName, tableName, tableName, tableName));

        writeToSqlFile(entityName.toLowerCase() + "_index.sql", sql.toString());
    }

    // ==================== 工具方法 ====================

    /**
     * 写入SQL文件
     */
    private static void writeToSqlFile(String fileName, String content) throws IOException {
        String sqlPath = getMainResourcesPath() + "/sql/generated";
        writeToFile(sqlPath, "", fileName, content);
    }

    /**
     * 驼峰转下划线
     */
    private static String toSnakeCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 获取字段默认值
     */
    private static String getDefaultValue(String dataType) {
        if (dataType == null) return "";
        return switch (dataType.toLowerCase()) {
            case "int", "integer", "tinyint", "smallint", "bigint" -> " DEFAULT 0";
            case "varchar", "char", "text" -> " DEFAULT ''";
            case "decimal", "numeric", "float", "double" -> " DEFAULT 0.00";
            case "datetime", "timestamp" -> " DEFAULT CURRENT_TIMESTAMP";
            case "date" -> " DEFAULT (CURRENT_DATE)";
            default -> "";
        };
    }
}
