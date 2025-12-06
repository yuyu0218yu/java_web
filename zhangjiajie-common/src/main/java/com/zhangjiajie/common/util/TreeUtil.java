package com.zhangjiajie.common.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 * 用于将平铺的列表数据转换为树形结构（如菜单、部门、分类等）
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class TreeUtil {

    private TreeUtil() {
        // 工具类禁止实例化
    }

    /**
     * 构建树形结构
     *
     * @param list          平铺列表
     * @param idGetter      ID获取函数
     * @param parentIdGetter 父ID获取函数
     * @param childrenSetter 子节点设置函数
     * @param rootParentId  根节点的父ID（通常为null或0）
     * @param <T>           节点类型
     * @param <ID>          ID类型
     * @return 树形结构列表
     */
    public static <T, ID> List<T> build(
            List<T> list,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter,
            ID rootParentId) {

        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 按父ID分组
        Map<ID, List<T>> parentIdMap = list.stream()
                .collect(Collectors.groupingBy(
                        item -> parentIdGetter.apply(item) == null ? rootParentId : parentIdGetter.apply(item),
                        Collectors.toList()
                ));

        // 为每个节点设置子节点
        list.forEach(item -> {
            ID id = idGetter.apply(item);
            List<T> children = parentIdMap.getOrDefault(id, new ArrayList<>());
            childrenSetter.accept(item, children);
        });

        // 返回根节点列表
        return parentIdMap.getOrDefault(rootParentId, new ArrayList<>());
    }

    /**
     * 构建树形结构（默认根节点父ID为null）
     */
    public static <T, ID> List<T> build(
            List<T> list,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter) {
        return build(list, idGetter, parentIdGetter, childrenSetter, null);
    }

    /**
     * 构建树形结构（Long类型ID，根节点父ID为0L）
     */
    public static <T> List<T> buildLongTree(
            List<T> list,
            Function<T, Long> idGetter,
            Function<T, Long> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter) {
        return build(list, idGetter, parentIdGetter, childrenSetter, 0L);
    }

    /**
     * 获取所有子节点ID（包括自身）
     *
     * @param list          平铺列表
     * @param idGetter      ID获取函数
     * @param parentIdGetter 父ID获取函数
     * @param nodeId        当前节点ID
     * @param <T>           节点类型
     * @param <ID>          ID类型
     * @return 所有子节点ID集合
     */
    public static <T, ID> Set<ID> getAllChildIds(
            List<T> list,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            ID nodeId) {

        Set<ID> result = new HashSet<>();
        result.add(nodeId);

        // 按父ID分组
        Map<ID, List<T>> parentIdMap = list.stream()
                .collect(Collectors.groupingBy(parentIdGetter, Collectors.toList()));

        // 递归获取所有子节点
        Queue<ID> queue = new LinkedList<>();
        queue.add(nodeId);

        while (!queue.isEmpty()) {
            ID currentId = queue.poll();
            List<T> children = parentIdMap.get(currentId);
            if (children != null) {
                for (T child : children) {
                    ID childId = idGetter.apply(child);
                    result.add(childId);
                    queue.add(childId);
                }
            }
        }

        return result;
    }

    /**
     * 获取所有父节点ID（包括自身）
     *
     * @param list          平铺列表
     * @param idGetter      ID获取函数
     * @param parentIdGetter 父ID获取函数
     * @param nodeId        当前节点ID
     * @param <T>           节点类型
     * @param <ID>          ID类型
     * @return 所有父节点ID列表（从根到当前节点的路径）
     */
    public static <T, ID> List<ID> getAllParentIds(
            List<T> list,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            ID nodeId) {

        LinkedList<ID> result = new LinkedList<>();

        // 构建ID到节点的映射
        Map<ID, T> idMap = list.stream()
                .collect(Collectors.toMap(idGetter, Function.identity(), (a, b) -> a));

        ID currentId = nodeId;
        while (currentId != null) {
            result.addFirst(currentId);
            T current = idMap.get(currentId);
            if (current == null) {
                break;
            }
            currentId = parentIdGetter.apply(current);
        }

        return result;
    }

    /**
     * 将树形结构展开为平铺列表
     *
     * @param tree           树形结构
     * @param childrenGetter 子节点获取函数
     * @param <T>            节点类型
     * @return 平铺列表
     */
    public static <T> List<T> flatten(
            List<T> tree,
            Function<T, List<T>> childrenGetter) {

        List<T> result = new ArrayList<>();
        if (tree == null || tree.isEmpty()) {
            return result;
        }

        Queue<T> queue = new LinkedList<>(tree);
        while (!queue.isEmpty()) {
            T node = queue.poll();
            result.add(node);
            List<T> children = childrenGetter.apply(node);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }
        }

        return result;
    }

    /**
     * 查找节点
     *
     * @param tree           树形结构
     * @param childrenGetter 子节点获取函数
     * @param idGetter       ID获取函数
     * @param targetId       目标ID
     * @param <T>            节点类型
     * @param <ID>           ID类型
     * @return 找到的节点，未找到返回null
     */
    public static <T, ID> T findNode(
            List<T> tree,
            Function<T, List<T>> childrenGetter,
            Function<T, ID> idGetter,
            ID targetId) {

        if (tree == null || tree.isEmpty() || targetId == null) {
            return null;
        }

        Queue<T> queue = new LinkedList<>(tree);
        while (!queue.isEmpty()) {
            T node = queue.poll();
            if (targetId.equals(idGetter.apply(node))) {
                return node;
            }
            List<T> children = childrenGetter.apply(node);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }
        }

        return null;
    }
}
