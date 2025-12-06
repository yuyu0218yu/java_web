package com.zhangjiajie.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.generator.entity.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 代码生成业务表字段 Mapper
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Mapper
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    /**
     * 根据表ID查询字段列表
     *
     * @param tableId 表ID
     * @return 字段列表
     */
    @Select("SELECT * FROM gen_table_column WHERE table_id = #{tableId} ORDER BY sort")
    List<GenTableColumn> selectByTableId(@Param("tableId") Long tableId);

    /**
     * 根据表ID删除字段
     *
     * @param tableId 表ID
     * @return 影响行数
     */
    @Delete("DELETE FROM gen_table_column WHERE table_id = #{tableId}")
    int deleteByTableId(@Param("tableId") Long tableId);

    /**
     * 批量删除表字段
     *
     * @param tableIds 表ID列表
     * @return 影响行数
     */
    @Delete("""
            <script>
            DELETE FROM gen_table_column WHERE table_id IN
            <foreach collection="tableIds" item="tableId" open="(" separator="," close=")">
                #{tableId}
            </foreach>
            </script>
            """)
    int deleteByTableIds(@Param("tableIds") List<Long> tableIds);
}
