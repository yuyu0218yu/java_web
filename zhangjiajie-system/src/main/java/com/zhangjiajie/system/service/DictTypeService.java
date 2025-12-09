package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.DictType;
import com.zhangjiajie.system.mapper.DictTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典类型服务
 */
@Service
public class DictTypeService extends ServiceImpl<DictTypeMapper, DictType> {

    /**
     * 获取所有启用的字典类型
     */
    public List<DictType> listEnabled() {
        return list(new LambdaQueryWrapper<DictType>()
                .eq(DictType::getStatus, 1)
                .orderByAsc(DictType::getId));
    }

    /**
     * 根据字典类型编码查询
     */
    public DictType getByDictType(String dictType) {
        return getOne(new LambdaQueryWrapper<DictType>()
                .eq(DictType::getDictType, dictType)
                .eq(DictType::getStatus, 1));
    }
}
