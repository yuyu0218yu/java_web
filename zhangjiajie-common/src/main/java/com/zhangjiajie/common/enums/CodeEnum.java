package com.zhangjiajie.common.enums;

/**
 * 通用编码枚举接口
 * 提取实体类中重复的枚举代码模式
 *
 * @param <T> 编码类型
 * @author yushuang
 * @since 2025-12-05
 */
public interface CodeEnum<T> {

    /**
     * 获取编码
     */
    T getCode();

    /**
     * 获取描述
     */
    String getDesc();

    /**
     * 根据编码获取枚举值
     *
     * @param enumClass 枚举类
     * @param code 编码
     * @param defaultValue 默认值
     * @param <E> 枚举类型
     * @param <T> 编码类型
     * @return 匹配的枚举值，如果没有找到则返回默认值
     */
    static <E extends Enum<E> & CodeEnum<T>, T> E getByCode(Class<E> enumClass, T code, E defaultValue) {
        if (code == null) {
            return defaultValue;
        }
        for (E e : enumClass.getEnumConstants()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return defaultValue;
    }
}
