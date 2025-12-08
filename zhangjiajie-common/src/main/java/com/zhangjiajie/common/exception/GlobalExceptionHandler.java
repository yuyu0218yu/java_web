package com.zhangjiajie.common.exception;

import com.zhangjiajie.common.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理业务异常、校验异常、认证异常等
 *
 * @author yushuang
 * @since 2025-12-05
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("null")
public class GlobalExceptionHandler {

    private static final String FIELD_ERROR_SEPARATOR = "; ";

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.error(e.getCode(), e.getMessage()));
    }

    /**
     * 处理参数校验异常 - @Valid 注解校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = buildFieldErrorMessage(e.getBindingResult().getFieldErrors());
        log.warn("参数校验失败: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError(message));
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException e) {
        String message = buildFieldErrorMessage(e.getBindingResult().getFieldErrors());
        log.warn("参数绑定失败: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError(message));
    }

    /**
     * 处理约束校验异常 - @Validated 注解校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String message = buildConstraintViolationMessage(e.getConstraintViolations());
        log.warn("约束校验失败: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError(message));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<Void>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        String message = "缺少必需参数: " + e.getParameterName();
        log.warn("缺少请求参数: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError(message));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        String message = "参数类型错误: " + e.getName();
        log.warn("参数类型不匹配: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError(message));
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证失败: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, Result.unauthorized("认证失败: " + e.getMessage()));
    }

    /**
     * 处理凭证错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result<Void>> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("用户名或密码错误");
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, Result.unauthorized("用户名或密码错误"));
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("访问被拒绝: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, Result.forbidden("没有权限访问此资源"));
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMethod());
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, 
                Result.error(405, "请求方法 " + e.getMethod() + " 不支持"));
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("资源不存在: {}", e.getRequestURL());
        return buildErrorResponse(HttpStatus.NOT_FOUND, Result.notFound("资源不存在: " + e.getRequestURL()));
    }

    /**
     * 处理资源不存在业务异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Result<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("资源不存在: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, Result.notFound(e.getMessage()));
    }

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件上传大小超限: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, Result.paramError("文件大小超过限制"));
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常: ", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, Result.error("系统异常，请稍后重试"));
    }

    /**
     * 构建错误响应
     */
    private ResponseEntity<Result<Void>> buildErrorResponse(HttpStatus status, Result<Void> result) {
        return ResponseEntity.status(status).body(result);
    }

    /**
     * 构建字段错误信息
     */
    private String buildFieldErrorMessage(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(FIELD_ERROR_SEPARATOR));
    }

    /**
     * 构建约束违反信息
     */
    private String buildConstraintViolationMessage(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(FIELD_ERROR_SEPARATOR));
    }
}
