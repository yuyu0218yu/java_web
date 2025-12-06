package com.yushuang.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yushuang.demo.dto.RoleWithPermissionsDTO;
import com.yushuang.demo.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取所有启用状态的角色
     */
    List<Role> getEnabledRoles();

    /**
     * 根据角色编码查询角色
     */
    Role getByRoleCode(String roleCode);

    /**
     * 根据用户ID查询角色列表
     */
    List<Role> getRolesByUserId(Long userId);

    /**
     * 获取所有角色及其权限数量
     */
    List<RoleWithPermissionsDTO> getRolesWithPermissionCount();

    /**
     * 检查角色编码是否存在
     */
    boolean checkRoleCodeExists(String roleCode, Long excludeId);

    /**
     * 创建角色
     */
    boolean createRole(Role role);

    /**
     * 更新角色
     */
    boolean updateRole(Long id, Role role);

    /**
     * 删除角色
     */
    boolean deleteRole(Long id);
}
