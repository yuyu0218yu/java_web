package com.zhangjiajie.common.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.StringUtils;

/**
 * 通用编码检查服务接口
 * 提供检查编码是否存在的通用方法
 *
 * @author yushuang
 * @since 2025-12-08
 */
public interface CodeCheckService {

    /**
     * 检查编码是否存在
     *
     * @param service       服务实例
     * @param code          编码值
     * @param codeGetter    获取编码字段的函数（使用SFunction以支持MyBatis-Plus的Lambda表达式）
     * @param idGetter      获取ID字段的函数
     * @param excludeId     排除的ID
     * @param <T>           实体类型
     * @param <ID>          ID类型
     * @return true-存在，false-不存在
     */
    static <T, ID> boolean checkCodeExists(
            IService<T> service,
            String code,
            SFunction<T, String> codeGetter,
            SFunction<T, ID> idGetter,
            ID excludeId) {
        
        if (!StringUtils.hasText(code)) {
            return false;
        }
        
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(codeGetter, code);
        
        if (excludeId != null) {
            wrapper.ne(idGetter, excludeId);
        }
        
        return service.count(wrapper) > 0;
    }
}
