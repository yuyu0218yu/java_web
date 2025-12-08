package com.zhangjiajie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangjiajie.system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门Mapper
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 查询部门及所有子部门ID
     *
     * @param deptId 部门ID
     * @return 子部门ID列表
     */
    @Select("SELECT id FROM sys_dept WHERE deleted = 0 AND (id = #{deptId} OR FIND_IN_SET(#{deptId}, ancestors))")
    List<Long> selectChildDeptIds(@Param("deptId") Long deptId);

    /**
     * 根据部门ID查询部门名称
     *
     * @param deptId 部门ID
     * @return 部门名称
     */
    @Select("SELECT dept_name FROM sys_dept WHERE id = #{deptId} AND deleted = 0")
    String selectDeptNameById(@Param("deptId") Long deptId);

    /**
     * 查询指定部门的所有子部门
     *
     * @param deptId 部门ID
     * @return 子部门列表
     */
    @Select("SELECT * FROM sys_dept WHERE deleted = 0 AND FIND_IN_SET(#{deptId}, ancestors) ORDER BY sort_order")
    List<Dept> selectChildDepts(@Param("deptId") Long deptId);

    /**
     * 获取部门完整路径（包含所有父级部门名称）
     *
     * @param deptId 部门ID
     * @return 部门路径，如：张家界旅游公司/技术部/后端组
     */
    @Select("SELECT CASE " +
            "  WHEN d.ancestors IS NULL OR d.ancestors = '' THEN d.dept_name " +
            "  ELSE CONCAT(" +
            "    (SELECT GROUP_CONCAT(dept_name ORDER BY id SEPARATOR '/') " +
            "     FROM sys_dept " +
            "     WHERE FIND_IN_SET(id, d.ancestors) AND deleted = 0), " +
            "    '/', d.dept_name) " +
            "END as dept_path " +
            "FROM sys_dept d WHERE d.id = #{deptId} AND d.deleted = 0")
    String selectDeptPathById(@Param("deptId") Long deptId);
}
