package com.zhangjiajie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.Menu;
import com.zhangjiajie.system.entity.RoleMenu;
import com.zhangjiajie.system.mapper.MenuMapper;
import com.zhangjiajie.system.mapper.RoleMenuMapper;
import com.zhangjiajie.system.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> result = new ArrayList<>();
        List<Long> menuIds = menus.stream().map(Menu::getId).collect(Collectors.toList());
        
        for (Menu menu : menus) {
            // 找到顶级菜单（parent_id为0或parent_id不在当前列表中）
            if (menu.getParentId() == null || menu.getParentId() == 0 || !menuIds.contains(menu.getParentId())) {
                buildChildren(menu, menus);
                result.add(menu);
            }
        }
        return result;
    }

    /**
     * 递归构建子菜单
     */
    private void buildChildren(Menu parent, List<Menu> allMenus) {
        List<Menu> children = allMenus.stream()
                .filter(m -> parent.getId().equals(m.getParentId()))
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            parent.setChildren(children);
            for (Menu child : children) {
                buildChildren(child, allMenus);
            }
        }
    }
}
