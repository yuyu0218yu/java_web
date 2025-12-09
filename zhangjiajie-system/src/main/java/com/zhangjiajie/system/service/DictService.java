package com.zhangjiajie.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjiajie.system.entity.DictData;
import com.zhangjiajie.system.mapper.DictDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务
 */
@Service
public class DictService extends ServiceImpl<DictDataMapper, DictData> {

    /**
     * 根据字典类型查询字典数据
     */
    public List<DictData> getByDictType(String dictType) {
        return list(new LambdaQueryWrapper<DictData>()
                .eq(DictData::getDictType, dictType)
                .eq(DictData::getStatus, 1)
                .orderByAsc(DictData::getDictSort));
    }
}
