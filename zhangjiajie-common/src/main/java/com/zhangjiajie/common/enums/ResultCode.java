package com.zhangjiajie.common.enums;

/**
 * API响应状态码枚举
 * 集中管理HTTP状态码和消息，避免Result.java中的魔法数字
 *
 * @author yushuang
 * @since 2025-12-06
 */
public enum ResultCode implements CodeEnum<Integer> {
    
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),
    
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    
    INTERNAL_ERROR(500, "操作失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据编码获取枚举
     */
    public static ResultCode getByCode(Integer code) {
        return CodeEnum.getByCode(ResultCode.class, code, INTERNAL_ERROR);
    }
}
