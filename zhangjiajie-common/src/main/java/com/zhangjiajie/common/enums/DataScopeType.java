package com.zhangjiajie.common.enums;

/**
 * 数据权限范围类型枚举
 *
 * @author yushuang
 * @since 2025-12-08
 */
public enum DataScopeType implements CodeEnum<Integer> {

    /**
     * 全部数据权限
     */
    ALL(1, "全部数据"),

    /**
     * 本部门及下级数据权限
     */
    DEPT_AND_CHILD(2, "本部门及下级"),

    /**
     * 本部门数据权限
     */
    DEPT(3, "本部门"),

    /**
     * 仅本人数据权限
     */
    SELF(4, "仅本人");

    private final Integer code;
    private final String desc;

    DataScopeType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static DataScopeType getByCode(Integer code) {
        return CodeEnum.getByCode(DataScopeType.class, code, ALL);
    }
}
