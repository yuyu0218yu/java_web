package com.zhangjiajie.common.enums;

/**
 * 通用启用/禁用状态枚举
 * 用于多个实体类共享的状态定义
 *
 * @author yushuang
 * @since 2025-12-05
 */
public enum EnableStatus implements CodeEnum<Integer> {
    
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String desc;

    EnableStatus(Integer code, String desc) {
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

    /**
     * 根据编码获取枚举
     */
    public static EnableStatus getByCode(Integer code) {
        return CodeEnum.getByCode(EnableStatus.class, code, DISABLED);
    }
}
