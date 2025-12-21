package com.zhangjiajie.system.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限树节点 DTO
 */
@Data
public class PermissionTreeNode {

    private Long id;

    private Long parentId;

    private String name;

    private String code;

    private String type;

    private Integer orderNum;

    private List<PermissionTreeNode> children = new ArrayList<>();

    public static String resolveType(String menuType) {
        if ("F".equalsIgnoreCase(menuType)) {
            return "button";
        }
        if ("C".equalsIgnoreCase(menuType)) {
            return "menu";
        }
        return "directory";
    }
}
