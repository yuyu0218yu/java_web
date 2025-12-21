package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.common.util.TreeUtil;
import com.zhangjiajie.system.dto.PermissionTreeNode;
import com.zhangjiajie.system.entity.Menu;
import com.zhangjiajie.system.entity.RoleMenu;
import com.zhangjiajie.system.mapper.MenuMapper;
import com.zhangjiajie.system.mapper.RoleMenuMapper;
import com.zhangjiajie.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> getUserMenuTree(Long userId) {
        // 获取用户的菜单列表（不含按钮）
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        // 过滤掉按钮类型，只保留目录和菜单
        menus = menus.stream()
                .filter(m -> !Menu.TYPE_BTN.equals(m.getMenuType()))
                .collect(Collectors.toList());
        // 构建树形结构
        return buildMenuTree(menus);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return menuMapper.selectPermsByUserId(userId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        // 先删除原有关联
        roleMenuMapper.deleteByRoleId(roleId);
        
        // 批量插入新关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
        log.info("角色 {} 菜单权限更新成功，菜单数量: {}", roleId, menuIds != null ? menuIds.size() : 0);
    }

    @Override
    public List<Menu> getAllMenuTree() {
        // 使用LambdaQueryWrapper替代自定义SQL
        List<Menu> menus = lambdaQuery()
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum)
                .list();
        return buildMenuTree(menus);
    }

    @Override
    public List<PermissionTreeNode> getPermissionTree() {
        List<Menu> menus = lambdaQuery()
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum)
                .list();
        List<Menu> tree = buildMenuTree(menus);
        return tree.stream()
                .map(this::convertToPermissionNode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        // 使用TreeUtil构建树形结构
        return TreeUtil.buildLongTree(menus, Menu::getId, Menu::getParentId, Menu::setChildren);
    }

    private PermissionTreeNode convertToPermissionNode(Menu menu) {
        PermissionTreeNode node = new PermissionTreeNode();
        node.setId(menu.getId());
        node.setParentId(menu.getParentId());
        node.setName(menu.getMenuName());
        node.setCode(menu.getPerms());
        node.setType(PermissionTreeNode.resolveType(menu.getMenuType()));
        node.setOrderNum(menu.getOrderNum());

        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            List<PermissionTreeNode> children = menu.getChildren().stream()
                    .sorted(Comparator.comparing(Menu::getOrderNum, Comparator.nullsLast(Integer::compareTo)))
                    .map(this::convertToPermissionNode)
                    .collect(Collectors.toList());
            node.setChildren(children);
        }
        return node;
    }
}
