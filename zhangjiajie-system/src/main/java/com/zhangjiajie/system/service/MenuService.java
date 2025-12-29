package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangjiajie.system.dto.PermissionTreeNode;
import com.zhangjiajie.system.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author yushuang
 * @since 2025-12-05
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户ID查询菜单树
     */
    List<Menu> getUserMenuTree(Long userId);

    /**
     * 根据用户ID查询权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 保存角色菜单关联
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询所有菜单树
     */
    List<Menu> getAllMenuTree();

    /**
     * 查询权限树（含按钮）
     */
    List<PermissionTreeNode> getPermissionTree();

    /**
     * 构建菜单树
     */
    List<Menu> buildMenuTree(List<Menu> menus);
}
