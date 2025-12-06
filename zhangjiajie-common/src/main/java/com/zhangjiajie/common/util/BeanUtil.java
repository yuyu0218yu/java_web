package com.zhangjiajie.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean工具类
 * 提供对象复制、转换等功能
 *
 * @author yushuang
 * @since 2025-12-06
 */
public final class BeanUtil {

    private BeanUtil() {
        // 工具类禁止实例化
    }

    /**
     * 复制属性（忽略null值）
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 复制属性
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * 复制对象
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param <T>         目标类型
     * @return 目标对象
     */
    public static <T> T copyObject(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("复制对象失败", e);
        }
    }

    /**
     * 复制对象（使用Supplier创建目标对象）
     *
     * @param source         源对象
     * @param targetSupplier 目标对象创建函数
     * @param <T>            目标类型
     * @return 目标对象
     */
    public static <T> T copyObject(Object source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 复制列表
     *
     * @param sourceList  源列表
     * @param targetClass 目标类
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标列表
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> copyObject(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 复制列表（使用Supplier创建目标对象）
     *
     * @param sourceList     源列表
     * @param targetSupplier 目标对象创建函数
     * @param <S>            源类型
     * @param <T>            目标类型
     * @return 目标列表
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> targetSupplier) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(source -> copyObject(source, targetSupplier))
                .collect(Collectors.toList());
    }

    /**
     * 将对象转换为Map
     *
     * @param obj 对象
     * @return Map
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }

        Map<String, Object> map = new HashMap<>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            String name = pd.getName();
            if (!"class".equals(name)) {
                Object value = beanWrapper.getPropertyValue(name);
                if (value != null) {
                    map.put(name, value);
                }
            }
        }

        return map;
    }

    /**
     * 将Map转换为对象
     *
     * @param map         Map
     * @param targetClass 目标类
     * @param <T>         目标类型
     * @return 目标对象
     */
    public static <T> T toObject(Map<String, Object> map, Class<T> targetClass) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanWrapper beanWrapper = new BeanWrapperImpl(target);
            beanWrapper.setAutoGrowNestedPaths(true);

            map.forEach((key, value) -> {
                if (beanWrapper.isWritableProperty(key) && value != null) {
                    beanWrapper.setPropertyValue(key, value);
                }
            });

            return target;
        } catch (Exception e) {
            throw new RuntimeException("Map转对象失败", e);
        }
    }

    /**
     * 获取对象中值为null的属性名数组
     *
     * @param source 源对象
     * @return null属性名数组
     */
    private static String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> nullNames = new HashSet<>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Object value = beanWrapper.getPropertyValue(pd.getName());
            if (value == null) {
                nullNames.add(pd.getName());
            }
        }

        return nullNames.toArray(new String[0]);
    }

    /**
     * 判断对象的所有属性是否都为null
     *
     * @param obj 对象
     * @return true-所有属性都为null，false-存在非null属性
     */
    public static boolean isAllFieldsNull(Object obj) {
        if (obj == null) {
            return true;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            String name = pd.getName();
            if (!"class".equals(name)) {
                Object value = beanWrapper.getPropertyValue(name);
                if (value != null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 获取对象的所有非null属性名
     *
     * @param obj 对象
     * @return 非null属性名列表
     */
    public static List<String> getNonNullPropertyNames(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }

        List<String> names = new ArrayList<>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            String name = pd.getName();
            if (!"class".equals(name)) {
                Object value = beanWrapper.getPropertyValue(name);
                if (value != null) {
                    names.add(name);
                }
            }
        }

        return names;
    }
}
