package com.zhangjiajie.common.enums;

/**
 * 通用操作结果状态枚举
 * 用于登录日志、操作日志等记录成功/失败状态
 *
 * @author yushuang
 * @since 2025-12-05
 */
public enum ResultStatus implements CodeEnum<Integer> {
    
    FAILURE(0, "失败"),
    SUCCESS(1, "成功");

    private final Integer code;
    private final String desc;

    ResultStatus(Integer code, String desc) {
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
    public static ResultStatus getByCode(Integer code) {
        return CodeEnum.getByCode(ResultStatus.class, code, FAILURE);
    }
}
