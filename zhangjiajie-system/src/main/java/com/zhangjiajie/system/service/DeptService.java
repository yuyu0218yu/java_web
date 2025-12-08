package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.Dept;

import java.util.List;

/**
 * 部门服务接口
 *
 * @author yushuang
 * @since 2025-12-06
 */
public interface DeptService extends IService<Dept> {

    /**
     * 获取部门树
     *
     * @return 树形部门列表
     */
    List<Dept> getDeptTree();

    /**
     * 获取部门下拉选项（启用状态）
     *
     * @return 部门列表
     */
    List<Dept> getDeptOptions();

    /**
     * 根据ID获取部门
     *
     * @param id 部门ID
     * @return 部门信息
     */
    Dept getDeptById(Long id);

    /**
     * 创建部门
     *
     * @param dept 部门信息
     */
    void createDept(Dept dept);

    /**
     * 更新部门
     *
     * @param id   部门ID
     * @param dept 部门信息
     */
    void updateDept(Long id, Dept dept);

    /**
     * 删除部门
     *
     * @param id 部门ID
     */
    void deleteDept(Long id);

    /**
     * 获取部门及所有子部门ID
     *
     * @param deptId 部门ID
     * @return 子部门ID列表
     */
    List<Long> getChildDeptIds(Long deptId);

    /**
     * 检查部门编码是否存在
     *
     * @param deptCode  部门编码
     * @param excludeId 排除的ID
     * @return true-存在，false-不存在
     */
    boolean checkDeptCodeExists(String deptCode, Long excludeId);

    /**
     * 根据部门ID获取部门名称
     *
     * @param deptId 部门ID
     * @return 部门名称
     */
    String getDeptNameById(Long deptId);
}
