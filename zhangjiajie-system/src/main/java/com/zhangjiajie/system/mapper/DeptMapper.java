package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 结构Mapper
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 查询结构及所有子结构ID
     *
     * @param deptId 结构ID
     * @return 子结构ID列表
     */
    @Select("SELECT id FROM sys_dept WHERE deleted = 0 AND (id = #{deptId} OR FIND_IN_SET(#{deptId}, ancestors))")
    List<Long> selectChildDeptIds(@Param("deptId") Long deptId);

    /**
     * 根据结构ID查询结构名称
     *
     * @param deptId 结构ID
     * @return 结构名称
     */
    @Select("SELECT dept_name FROM sys_dept WHERE id = #{deptId} AND deleted = 0")
    String selectDeptNameById(@Param("deptId") Long deptId);

    /**
     * 查询指定结构的所有子结构
     *
     * @param deptId 结构ID
     * @return 子结构列表
     */
    @Select("SELECT * FROM sys_dept WHERE deleted = 0 AND FIND_IN_SET(#{deptId}, ancestors) ORDER BY sort_order")
    List<Dept> selectChildDepts(@Param("deptId") Long deptId);
}
