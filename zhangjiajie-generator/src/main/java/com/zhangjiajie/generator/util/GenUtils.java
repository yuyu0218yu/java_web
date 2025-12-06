package com.zhangjiajie.generator.util;

import com.zhangjiajie.generator.entity.GenTableColumn;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * 代码生成工具类
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class GenUtils {

    /** 数据库字符串类型 */
    private static final String[] COLUMN_TYPE_STR = {"char", "varchar", "nvarchar", "varchar2", "tinytext", "text",
            "mediumtext", "longtext"};

    /** 数据库文本类型 */
    private static final String[] COLUMN_TYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /** 数据库时间类型 */
    private static final String[] COLUMN_TYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /** 数据库数字类型 */
    private static final String[] COLUMN_TYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal"};

    /** 需要移除的表前缀 */
    private static final String[] TABLE_PREFIX = {"sys_", "tb_", "t_"};

    /** 不需要显示的列 */
    private static final String[] COLUMN_NAME_NOT_EDIT = {"id", "create_by", "create_time", "update_by",
            "update_time", "deleted", "del_flag"};

    /** 不需要查询的列 */
    private static final String[] COLUMN_NAME_NOT_QUERY = {"id", "create_by", "create_time", "update_by",
            "update_time", "deleted", "del_flag", "remark"};

    /** 不需要列表展示的列 */
    private static final String[] COLUMN_NAME_NOT_LIST = {"id", "create_by", "update_by", "deleted", "del_flag"};

    /**
     * 表名转换成类名
     */
    public static String tableNameToClassName(String tableName) {
        // 移除表前缀
        for (String prefix : TABLE_PREFIX) {
            if (tableName.toLowerCase().startsWith(prefix)) {
                tableName = tableName.substring(prefix.length());
                break;
            }
        }
        return toCamelCase(tableName, true);
    }

    /**
     * 获取模块名
     */
    public static String getModuleName(String tableName) {
        // 从表前缀获取模块名
        for (String prefix : TABLE_PREFIX) {
            if (tableName.toLowerCase().startsWith(prefix)) {
                return prefix.replace("_", "");
            }
        }
        return "system";
    }

    /**
     * 获取业务名
     */
    public static String getBusinessName(String tableName) {
        // 移除表前缀
        for (String prefix : TABLE_PREFIX) {
            if (tableName.toLowerCase().startsWith(prefix)) {
                tableName = tableName.substring(prefix.length());
                break;
            }
        }
        return toCamelCase(tableName, false);
    }

    /**
     * 初始化列字段
     */
    public static GenTableColumn initColumnField(Map<String, Object> columnMap, Long tableId) {
        String columnName = (String) columnMap.get("columnName");
        String columnType = (String) columnMap.get("columnType");
        String columnComment = (String) columnMap.get("columnComment");

        GenTableColumn column = new GenTableColumn();
        column.setTableId(tableId);
        column.setColumnName(columnName);
        column.setColumnType(columnType);
        column.setColumnComment(columnComment);
        column.setJavaField(toCamelCase(columnName, false));
        column.setJavaType(columnTypeToJavaType(columnType));
        column.setIsPk((String) columnMap.get("isPk"));
        column.setIsIncrement((String) columnMap.get("isIncrement"));
        column.setIsRequired((String) columnMap.get("isRequired"));
        column.setQueryType(GenTableColumn.QUERY_EQ);
        column.setHtmlType(getHtmlType(columnType, columnName));

        // 设置是否插入
        column.setIsInsert(GenTableColumn.YES);

        // 设置是否编辑
        if (!isColumnNameNotEdit(columnName)) {
            column.setIsEdit(GenTableColumn.YES);
        } else {
            column.setIsEdit(GenTableColumn.NO);
        }

        // 设置是否列表
        if (!isColumnNameNotList(columnName)) {
            column.setIsList(GenTableColumn.YES);
        } else {
            column.setIsList(GenTableColumn.NO);
        }

        // 设置是否查询
        if (!isColumnNameNotQuery(columnName)) {
            column.setIsQuery(GenTableColumn.NO); // 默认不查询，用户自己选
        } else {
            column.setIsQuery(GenTableColumn.NO);
        }

        // 名称类型默认模糊查询
        if (columnName.toLowerCase().contains("name")) {
            column.setQueryType(GenTableColumn.QUERY_LIKE);
            column.setIsQuery(GenTableColumn.YES);
        }

        // 状态类型默认等于查询
        if (columnName.toLowerCase().contains("status") || columnName.toLowerCase().contains("type")) {
            column.setQueryType(GenTableColumn.QUERY_EQ);
            column.setIsQuery(GenTableColumn.YES);
        }

        // 时间类型默认范围查询
        if (columnName.toLowerCase().contains("time") || columnName.toLowerCase().contains("date")) {
            column.setQueryType(GenTableColumn.QUERY_BETWEEN);
        }

        return column;
    }

    /**
     * 更新列字段
     */
    public static void updateColumnField(GenTableColumn column, Map<String, Object> columnMap) {
        String columnType = (String) columnMap.get("columnType");
        column.setColumnType(columnType);
        column.setJavaType(columnTypeToJavaType(columnType));
        column.setIsPk((String) columnMap.get("isPk"));
        column.setIsIncrement((String) columnMap.get("isIncrement"));
        column.setIsRequired((String) columnMap.get("isRequired"));
    }

    /**
     * 数据库类型转Java类型
     */
    public static String columnTypeToJavaType(String columnType) {
        if (columnType == null) {
            return "String";
        }
        String dbType = columnType.toLowerCase();

        if (arraysContains(COLUMN_TYPE_STR, dbType) || arraysContains(COLUMN_TYPE_TEXT, dbType)) {
            return "String";
        }

        if (arraysContains(COLUMN_TYPE_TIME, dbType)) {
            if (dbType.equals("date")) {
                return "LocalDate";
            }
            return "LocalDateTime";
        }

        if (arraysContains(COLUMN_TYPE_NUMBER, dbType)) {
            if (dbType.startsWith("bigint")) {
                return "Long";
            }
            if (dbType.startsWith("tinyint(1)") || dbType.equals("bit")) {
                return "Boolean";
            }
            if (dbType.startsWith("decimal") || dbType.startsWith("numeric")) {
                return "BigDecimal";
            }
            if (dbType.startsWith("float")) {
                return "Float";
            }
            if (dbType.startsWith("double")) {
                return "Double";
            }
            return "Integer";
        }

        return "String";
    }

    /**
     * 获取HTML显示类型
     */
    public static String getHtmlType(String columnType, String columnName) {
        if (columnType == null) {
            return GenTableColumn.HTML_INPUT;
        }

        String lowerColumnName = columnName.toLowerCase();

        // 文本域
        if (arraysContains(COLUMN_TYPE_TEXT, columnType.toLowerCase())) {
            return GenTableColumn.HTML_TEXTAREA;
        }

        // 状态类型：单选框
        if (lowerColumnName.contains("status") || lowerColumnName.contains("sex")
                || lowerColumnName.contains("gender")) {
            return GenTableColumn.HTML_RADIO;
        }

        // 类型：下拉框
        if (lowerColumnName.contains("type") || lowerColumnName.contains("category")) {
            return GenTableColumn.HTML_SELECT;
        }

        // 时间类型
        if (arraysContains(COLUMN_TYPE_TIME, columnType.toLowerCase())) {
            return GenTableColumn.HTML_DATETIME;
        }

        // 图片
        if (lowerColumnName.contains("image") || lowerColumnName.contains("img")
                || lowerColumnName.contains("avatar") || lowerColumnName.contains("logo")) {
            return GenTableColumn.HTML_IMAGE;
        }

        // 文件
        if (lowerColumnName.contains("file") || lowerColumnName.contains("attachment")) {
            return GenTableColumn.HTML_UPLOAD;
        }

        // 内容、描述：富文本
        if (lowerColumnName.contains("content") || lowerColumnName.equals("description")
                || lowerColumnName.equals("remark")) {
            return GenTableColumn.HTML_TEXTAREA;
        }

        return GenTableColumn.HTML_INPUT;
    }

    /**
     * 下划线转驼峰
     */
    public static String toCamelCase(String str, boolean capitalizeFirst) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = capitalizeFirst;

        for (char c : str.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    /**
     * 驼峰转下划线
     */
    public static String toUnderlineCase(String str) {
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
     * 判断数组是否包含指定值
     */
    private static boolean arraysContains(String[] arr, String targetValue) {
        if (targetValue == null) {
            return false;
        }
        for (String s : arr) {
            if (targetValue.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不需要编辑的列
     */
    private static boolean isColumnNameNotEdit(String columnName) {
        return Arrays.asList(COLUMN_NAME_NOT_EDIT).contains(columnName.toLowerCase());
    }

    /**
     * 是否不需要查询的列
     */
    private static boolean isColumnNameNotQuery(String columnName) {
        return Arrays.asList(COLUMN_NAME_NOT_QUERY).contains(columnName.toLowerCase());
    }

    /**
     * 是否不需要列表的列
     */
    private static boolean isColumnNameNotList(String columnName) {
        return Arrays.asList(COLUMN_NAME_NOT_LIST).contains(columnName.toLowerCase());
    }
}
