package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.entity.Dept;

import java.util.List;

/**
 * 结构服务接口
 *
 * @author yushuang
 * @since 2025-12-06
 */
public interface DeptService extends IService<Dept> {

    /**
     * 获取结构树
     *
     * @return 树形结构列表
     */
    List<Dept> getDeptTree();

    /**
     * 获取结构下拉选项（启用状态）
     *
     * @return 结构列表
     */
    List<Dept> getDeptOptions();

    /**
     * 根据ID获取结构
     *
     * @param id 结构ID
     * @return 结构信息
     */
    Dept getDeptById(Long id);

    /**
     * 创建结构
     *
     * @param dept 结构信息
     */
    void createDept(Dept dept);

    /**
     * 更新结构
     *
     * @param id   结构ID
     * @param dept 结构信息
     */
    void updateDept(Long id, Dept dept);

    /**
     * 删除结构
     *
     * @param id 结构ID
     */
    void deleteDept(Long id);

    /**
     * 获取结构及所有子结构ID
     *
     * @param deptId 结构ID
     * @return 子结构ID列表
     */
    List<Long> getChildDeptIds(Long deptId);

    /**
     * 检查结构编码是否存在
     *
     * @param deptCode  结构编码
     * @param excludeId 排除的ID
     * @return true-存在，false-不存在
     */
    boolean checkDeptCodeExists(String deptCode, Long excludeId);

    /**
     * 根据结构ID获取结构名称
     *
     * @param deptId 结构ID
     * @return 结构名称
     */
    String getDeptNameById(Long deptId);
}
