package com.zhangjiajie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 代码生成业务表 Mapper
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Mapper
public interface GenTableMapper extends BaseMapper<GenTable> {

    /**
     * 查询数据库中的表列表
     *
     * @param tableName 表名（模糊查询）
     * @param tableComment 表注释（模糊查询）
     * @return 表列表
     */
    @Select("""
            <script>
            SELECT table_name AS tableName, table_comment AS tableComment, create_time AS createTime, update_time AS updateTime
            FROM information_schema.tables
            WHERE table_schema = (SELECT DATABASE())
            AND table_name NOT LIKE 'gen_%'
            AND table_name NOT IN (SELECT table_name FROM gen_table)
            <if test="tableName != null and tableName != ''">
                AND table_name LIKE CONCAT('%', #{tableName}, '%')
            </if>
            <if test="tableComment != null and tableComment != ''">
                AND table_comment LIKE CONCAT('%', #{tableComment}, '%')
            </if>
            ORDER BY create_time DESC
            </script>
            """)
    List<Map<String, Object>> selectDbTableList(@Param("tableName") String tableName,
                                                 @Param("tableComment") String tableComment);

    /**
     * 根据表名查询数据库表信息
     *
     * @param tableName 表名
     * @return 表信息
     */
    @Select("""
            SELECT table_name AS tableName, table_comment AS tableComment, create_time AS createTime, update_time AS updateTime
            FROM information_schema.tables
            WHERE table_schema = (SELECT DATABASE())
            AND table_name = #{tableName}
            """)
    Map<String, Object> selectDbTableByName(@Param("tableName") String tableName);

    /**
     * 根据表名列表查询数据库表信息
     *
     * @param tableNames 表名列表
     * @return 表信息列表
     */
    @Select("""
            <script>
            SELECT table_name AS tableName, table_comment AS tableComment, create_time AS createTime, update_time AS updateTime
            FROM information_schema.tables
            WHERE table_schema = (SELECT DATABASE())
            AND table_name IN
            <foreach collection="tableNames" item="tableName" open="(" separator="," close=")">
                #{tableName}
            </foreach>
            </script>
            """)
    List<Map<String, Object>> selectDbTableListByNames(@Param("tableNames") List<String> tableNames);

    /**
     * 查询表的列信息
     *
     * @param tableName 表名
     * @return 列信息列表
     */
    @Select("""
            SELECT column_name AS columnName,
                   (CASE WHEN (is_nullable = 'NO' AND column_key != 'PRI') THEN '1' ELSE '0' END) AS isRequired,
                   (CASE WHEN column_key = 'PRI' THEN '1' ELSE '0' END) AS isPk,
                   ordinal_position AS sort,
                   column_comment AS columnComment,
                   (CASE WHEN extra = 'auto_increment' THEN '1' ELSE '0' END) AS isIncrement,
                   column_type AS columnType
            FROM information_schema.columns
            WHERE table_schema = (SELECT DATABASE())
            AND table_name = #{tableName}
            ORDER BY ordinal_position
            """)
    List<Map<String, Object>> selectDbTableColumnsByName(@Param("tableName") String tableName);
}
