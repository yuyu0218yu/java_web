package com.zhangjiajie.common.core;

import com.zhangjiajie.common.enums.ResultCode;
import lombok.Data;

/**
 * 统一响应结果封装
 * @param <T> 响应数据类型
 */
@Data
public class Result<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 根据ResultCode创建响应
     */
    public static <T> Result<T> of(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 根据ResultCode创建响应（带数据）
     */
    public static <T> Result<T> of(ResultCode resultCode, T data) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * 根据ResultCode创建响应（自定义消息）
     */
    public static <T> Result<T> of(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message);
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return of(ResultCode.SUCCESS);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return of(ResultCode.SUCCESS, data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功响应（仅消息）
     */
    public static <T> Result<T> success(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return of(ResultCode.INTERNAL_ERROR);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return of(ResultCode.INTERNAL_ERROR, message);
    }

    /**
     * 失败响应（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 参数错误响应
     */
    public static <T> Result<T> paramError() {
        return of(ResultCode.BAD_REQUEST);
    }

    /**
     * 参数错误响应（自定义消息）
     */
    public static <T> Result<T> paramError(String message) {
        return of(ResultCode.BAD_REQUEST, message);
    }

    /**
     * 未授权响应
     */
    public static <T> Result<T> unauthorized() {
        return of(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权响应（自定义消息）
     */
    public static <T> Result<T> unauthorized(String message) {
        return of(ResultCode.UNAUTHORIZED, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> Result<T> forbidden() {
        return of(ResultCode.FORBIDDEN);
    }

    /**
     * 禁止访问响应（自定义消息）
     */
    public static <T> Result<T> forbidden(String message) {
        return of(ResultCode.FORBIDDEN, message);
    }

    /**
     * 资源不存在响应
     */
    public static <T> Result<T> notFound() {
        return of(ResultCode.NOT_FOUND);
    }

    /**
     * 资源不存在响应（自定义消息）
     */
    public static <T> Result<T> notFound(String message) {
        return of(ResultCode.NOT_FOUND, message);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code.equals(ResultCode.SUCCESS.getCode());
    }
}