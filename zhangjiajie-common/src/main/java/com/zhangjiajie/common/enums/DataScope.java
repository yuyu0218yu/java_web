package com.zhangjiajie.common.enums;

import lombok.Getter;

/**
 * 数据范围枚举
 * 定义角色可以访问的数据范围
 *
 * @author system
 * @since 2025-12-08
 */
@Getter
public enum DataScope implements CodeEnum<Integer> {
    
    /**
     * 全部数据权限 - 可以查看和管理所有部门的数据
     */
    ALL(1, "全部数据"),
    
    /**
     * 本部门及下级 - 只能查看和管理用户所在部门及其下级部门的数据
     */
    DEPT_AND_CHILD(2, "本部门及下级"),
    
    /**
     * 仅本部门 - 只能查看和管理用户所在部门的数据
     */
    DEPT_ONLY(3, "仅本部门"),
    
    /**
     * 仅本人 - 只能查看和管理自己的数据
     */
    SELF_ONLY(4, "仅本人");

    private final Integer code;
    private final String desc;

    DataScope(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取数据范围的详细描述
     *
     * @param code 数据范围代码
     * @return 详细描述
     */
    public static String getDetailedDesc(Integer code) {
        if (code == null) {
            return "未设置";
        }
        switch (code) {
            case 1:
                return "全部数据 - 可以查看和管理所有部门的数据";
            case 2:
                return "本部门及下级 - 只能查看和管理用户所在部门及其下级部门的数据";
            case 3:
                return "本部门 - 只能查看和管理用户所在部门的数据";
            case 4:
                return "仅本人 - 只能查看和管理自己的数据";
            default:
                return "未知范围";
        }
    }

    /**
     * 根据code获取枚举
     *
     * @param code 数据范围代码
     * @return 数据范围枚举
     */
    public static DataScope getByCode(Integer code) {
        return CodeEnum.getByCode(DataScope.class, code, null);
    }
}
