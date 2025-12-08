package com.zhangjiajie.system.converter;

import com.zhangjiajie.system.dto.CreateDeptRequest;
import com.zhangjiajie.system.dto.UpdateDeptRequest;
import com.zhangjiajie.system.entity.Dept;
import org.springframework.beans.BeanUtils;

/**
 * 部门DTO转换器
 *
 * @author yushuang
 * @since 2025-12-08
 */
public final class DeptConverter {

    private DeptConverter() {
        // 工具类禁止实例化
    }

    /**
     * 将CreateDeptRequest转换为Dept实体
     *
     * @param request 创建请求DTO
     * @return Dept实体
     */
    public static Dept toEntity(CreateDeptRequest request) {
        if (request == null) {
            return null;
        }
        Dept dept = new Dept();
        BeanUtils.copyProperties(request, dept);
        return dept;
    }

    /**
     * 将UpdateDeptRequest转换为Dept实体
     *
     * @param request 更新请求DTO
     * @return Dept实体
     */
    public static Dept toEntity(UpdateDeptRequest request) {
        if (request == null) {
            return null;
        }
        Dept dept = new Dept();
        BeanUtils.copyProperties(request, dept);
        return dept;
    }

    /**
     * 将UpdateDeptRequest的非空属性复制到Dept实体
     *
     * @param request 更新请求DTO
     * @param target  目标实体
     */
    public static void updateEntity(UpdateDeptRequest request, Dept target) {
        if (request == null || target == null) {
            return;
        }
        // 只复制非空属性
        if (request.getParentId() != null) {
            target.setParentId(request.getParentId());
        }
        if (request.getDeptName() != null) {
            target.setDeptName(request.getDeptName());
        }
        if (request.getDeptCode() != null) {
            target.setDeptCode(request.getDeptCode());
        }
        if (request.getLeader() != null) {
            target.setLeader(request.getLeader());
        }
        if (request.getPhone() != null) {
            target.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            target.setEmail(request.getEmail());
        }
        if (request.getSortOrder() != null) {
            target.setSortOrder(request.getSortOrder());
        }
        if (request.getStatus() != null) {
            target.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            target.setRemark(request.getRemark());
        }
    }
}
