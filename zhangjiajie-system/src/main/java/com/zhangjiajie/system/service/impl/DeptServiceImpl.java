package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.common.service.CodeCheckService;
import com.zhangjiajie.common.util.Assert;
import com.zhangjiajie.common.util.TreeUtil;
import com.zhangjiajie.system.entity.Dept;
import com.zhangjiajie.system.mapper.DeptMapper;
import com.zhangjiajie.system.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门服务实现类
 *
 * @author yushuang
 * @since 2025-12-06
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public List<Dept> getDeptTree() {
        List<Dept> depts = lambdaQuery()
                .orderByAsc(Dept::getParentId)
                .orderByAsc(Dept::getSortOrder)
                .list();
        // 使用 TreeUtil 构建树形结构，根节点的 parentId 为 0
        return TreeUtil.buildLongTree(depts, Dept::getId, Dept::getParentId, Dept::setChildren);
    }

    @Override
    public List<Dept> getDeptOptions() {
        return lambdaQuery()
                .eq(Dept::getStatus, 1)
                .orderByAsc(Dept::getParentId)
                .orderByAsc(Dept::getSortOrder)
                .list();
    }

    @Override
    public Dept getDeptById(Long id) {
        Dept dept = getById(id);
        Assert.found(dept, "部门不存在");
        return dept;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(Dept dept) {
        Assert.notEmpty(dept.getDeptName(), "部门名称不能为空");

        // 检查编码是否重复
        if (StringUtils.hasText(dept.getDeptCode())) {
            Assert.isFalse(checkDeptCodeExists(dept.getDeptCode(), null), "部门编码已存在");
        }

        // 设置祖先节点
        if (dept.getParentId() != null && dept.getParentId() > 0) {
            Dept parent = getById(dept.getParentId());
            Assert.found(parent, "父部门不存在");
            dept.setAncestors(parent.getAncestors() + "," + parent.getId());
        } else {
            dept.setParentId(0L);
            dept.setAncestors("0");
        }

        // 设置默认值
        if (dept.getSortOrder() == null) {
            dept.setSortOrder(0);
        }
        if (dept.getStatus() == null) {
            dept.setStatus(1);
        }

        save(dept);
        log.info("创建部门成功: {}", dept.getDeptName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Long id, Dept dept) {
        Assert.notNull(id, "部门ID不能为空");

        Dept existing = getById(id);
        Assert.found(existing, "部门不存在");

        // 检查编码是否重复
        if (StringUtils.hasText(dept.getDeptCode())) {
            Assert.isFalse(checkDeptCodeExists(dept.getDeptCode(), id), "部门编码已存在");
        }

        // 不能将自己设为父部门
        if (dept.getParentId() != null && dept.getParentId().equals(id)) {
            throw new IllegalArgumentException("不能将自己设为父部门");
        }

        // 如果父部门变更，需要更新 ancestors
        if (dept.getParentId() != null && !dept.getParentId().equals(existing.getParentId())) {
            // 检查新父部门是否是自己的子部门（防止循环引用）
            List<Long> childIds = getChildDeptIds(id);
            Assert.isFalse(childIds.contains(dept.getParentId()), "不能将子部门设为父部门");

            // 更新自己的 ancestors
            if (dept.getParentId() > 0) {
                Dept newParent = getById(dept.getParentId());
                Assert.found(newParent, "父部门不存在");
                dept.setAncestors(newParent.getAncestors() + "," + newParent.getId());
            } else {
                dept.setAncestors("0");
            }

            // 更新所有子部门的 ancestors
            updateChildrenAncestors(id, existing.getAncestors(), dept.getAncestors());
        }

        dept.setId(id);
        updateById(dept);
        log.info("更新部门成功: {}", dept.getDeptName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(Long id) {
        Assert.notNull(id, "部门ID不能为空");

        Dept dept = getById(id);
        Assert.found(dept, "部门不存在");

        // 检查是否有子部门
        long childCount = lambdaQuery().eq(Dept::getParentId, id).count();
        Assert.isTrue(childCount == 0, "存在子部门，无法删除");

        // TODO: 检查是否有用户属于该部门
        // long userCount = userMapper.countByDeptId(id);
        // Assert.isTrue(userCount == 0, "部门下存在用户，无法删除");

        removeById(id);
        log.info("删除部门成功: {}", dept.getDeptName());
    }

    @Override
    public List<Long> getChildDeptIds(Long deptId) {
        return baseMapper.selectChildDeptIds(deptId);
    }

    @Override
    public boolean checkDeptCodeExists(String deptCode, Long excludeId) {
        return CodeCheckService.checkCodeExists(
                this,
                deptCode,
                Dept::getDeptCode,
                Dept::getId,
                excludeId
        );
    }

    @Override
    public String getDeptNameById(Long deptId) {
        if (deptId == null) {
            return null;
        }
        return baseMapper.selectDeptNameById(deptId);
    }

    /**
     * 更新子部门的祖先节点（批量更新优化）
     *
     * @param deptId       部门ID
     * @param oldAncestors 旧祖先节点
     * @param newAncestors 新祖先节点
     */
    private void updateChildrenAncestors(Long deptId, String oldAncestors, String newAncestors) {
        List<Dept> children = baseMapper.selectChildDepts(deptId);
        if (children != null && !children.isEmpty()) {
            String oldPrefix = oldAncestors + "," + deptId;
            String newPrefix = newAncestors + "," + deptId;
            
            // 批量更新子部门的ancestors字段
            List<Dept> updateList = new ArrayList<>();
            for (Dept child : children) {
                String childAncestors = child.getAncestors();
                // 确保只替换开头部分，避免误替换
                if (childAncestors.startsWith(oldPrefix + ",") || childAncestors.equals(oldPrefix)) {
                    childAncestors = childAncestors.replaceFirst("^" + oldPrefix.replace(",", "\\,"), newPrefix);
                    child.setAncestors(childAncestors);
                    updateList.add(child);
                }
            }
            
            // 使用MyBatis-Plus的批量更新
            if (!updateList.isEmpty()) {
                updateBatchById(updateList);
            }
        }
    }
}
