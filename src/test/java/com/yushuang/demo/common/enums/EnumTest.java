package com.yushuang.demo.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnableStatus 和 ResultStatus 枚举单元测试
 */
class EnumTest {

    @Test
    void testEnableStatus_Values() {
        assertEquals(0, EnableStatus.DISABLED.getCode());
        assertEquals("禁用", EnableStatus.DISABLED.getDesc());
        
        assertEquals(1, EnableStatus.ENABLED.getCode());
        assertEquals("启用", EnableStatus.ENABLED.getDesc());
    }

    @Test
    void testEnableStatus_GetByCode() {
        assertEquals(EnableStatus.DISABLED, EnableStatus.getByCode(0));
        assertEquals(EnableStatus.ENABLED, EnableStatus.getByCode(1));
        assertEquals(EnableStatus.DISABLED, EnableStatus.getByCode(99)); // 默认值
        assertEquals(EnableStatus.DISABLED, EnableStatus.getByCode(null)); // null处理
    }

    @Test
    void testResultStatus_Values() {
        assertEquals(0, ResultStatus.FAILURE.getCode());
        assertEquals("失败", ResultStatus.FAILURE.getDesc());
        
        assertEquals(1, ResultStatus.SUCCESS.getCode());
        assertEquals("成功", ResultStatus.SUCCESS.getDesc());
    }

    @Test
    void testResultStatus_GetByCode() {
        assertEquals(ResultStatus.FAILURE, ResultStatus.getByCode(0));
        assertEquals(ResultStatus.SUCCESS, ResultStatus.getByCode(1));
        assertEquals(ResultStatus.FAILURE, ResultStatus.getByCode(99)); // 默认值
        assertEquals(ResultStatus.FAILURE, ResultStatus.getByCode(null)); // null处理
    }

    @Test
    void testCodeEnum_GenericGetByCode() {
        // 测试通用的 CodeEnum.getByCode 方法
        EnableStatus result = CodeEnum.getByCode(EnableStatus.class, 1, EnableStatus.DISABLED);
        assertEquals(EnableStatus.ENABLED, result);
        
        ResultStatus resultStatus = CodeEnum.getByCode(ResultStatus.class, 0, ResultStatus.SUCCESS);
        assertEquals(ResultStatus.FAILURE, resultStatus);
    }
}
