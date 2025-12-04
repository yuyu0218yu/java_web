package com.yushuang.demo.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushuang.demo.annotation.AuditLog;
import com.yushuang.demo.entity.OperationLog;
import com.yushuang.demo.mapper.OperationLogMapper;
import com.yushuang.demo.util.IpUtil;
import com.yushuang.demo.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志AOP切面
 * 异步记录操作日志，避免影响主业务性能
 *
 * @author yushuang
 * @since 2025-12-05
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    /**
     * 定义切点：标记了@AuditLog注解的方法
     */
    @Pointcut("@annotation(com.yushuang.demo.annotation.AuditLog)")
    public void auditLogPointCut() {}

    /**
     * 环绕通知
     */
    @Around("auditLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog operationLog = new OperationLog();

        try {
            // 执行方法前准备日志信息
            prepareLogBefore(joinPoint, operationLog);

            // 执行方法
            Object result = joinPoint.proceed();

            // 执行成功后更新日志
            long endTime = System.currentTimeMillis();
            operationLog.setTime(endTime - startTime);
            operationLog.setStatus(OperationLog.Status.SUCCESS.getCode());

            if (getAuditLogAnnotation(joinPoint).saveResponseData()) {
                operationLog.setParams(serializeResponse(result));
            }

            return result;

        } catch (Exception e) {
            // 执行失败后更新日志
            long endTime = System.currentTimeMillis();
            operationLog.setTime(endTime - startTime);
            operationLog.setStatus(OperationLog.Status.FAILURE.getCode());
            operationLog.setErrorMsg(e.getMessage());

            log.error("操作执行失败: {}", e.getMessage(), e);
            throw e;
        } finally {
            // 异步保存日志
            saveLogAsync(operationLog);
        }
    }

    /**
     * 准备日志信息
     */
    private void prepareLogBefore(ProceedingJoinPoint joinPoint, OperationLog operationLog) {
        // 获取注解信息
        AuditLog auditLog = getAuditLogAnnotation(joinPoint);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 基本信息
        operationLog.setOperation(getOperationDescription(auditLog, method));
        operationLog.setMethod(method.getDeclaringClass().getName() + "." + method.getName());

        // 用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            operationLog.setUsername(authentication.getName());
            // 如果能获取到用户ID，也可以设置
            // operationLog.setUserId(getUserIdFromAuthentication(authentication));
        }

        // 请求信息
        HttpServletRequest request = WebUtil.getRequest();
        if (request != null) {
            operationLog.setIp(IpUtil.getClientIp(request));
            operationLog.setUserAgent(request.getHeader("User-Agent"));

            if (auditLog.saveRequestData()) {
                operationLog.setParams(serializeRequest(joinPoint.getArgs(), auditLog));
            }
        }
    }

    /**
     * 获取注解信息
     */
    private AuditLog getAuditLogAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(AuditLog.class);
    }

    /**
     * 获取操作描述
     */
    private String getOperationDescription(AuditLog auditLog, Method method) {
        if (auditLog != null && !auditLog.operation().isEmpty()) {
            return auditLog.operation();
        }

        if (auditLog != null && !auditLog.module().isEmpty()) {
            return auditLog.module();
        }

        return method.getName();
    }

    /**
     * 序列化请求参数
     */
    private String serializeRequest(Object[] args, AuditLog auditLog) {
        try {
            if (args == null || args.length == 0) {
                return "";
            }

            List<Object> filteredArgs = Arrays.stream(args)
                    .filter(arg -> !isExcludeType(arg, auditLog))
                    .collect(Collectors.toList());

            if (filteredArgs.isEmpty()) {
                return "";
            }

            String json = objectMapper.writeValueAsString(filteredArgs);

            // 敏感信息脱敏
            if (auditLog.sensitiveFields() != null && auditLog.sensitiveFields().length > 0) {
                json = maskSensitiveFields(json, auditLog.sensitiveFields());
            }

            // 限制长度
            if (json.length() > 2000) {
                json = json.substring(0, 2000) + "...";
            }

            return json;

        } catch (Exception e) {
            log.warn("序列化请求参数失败: {}", e.getMessage());
            return "序列化失败: " + e.getMessage();
        }
    }

    /**
     * 序列化响应数据
     */
    private String serializeResponse(Object response) {
        try {
            if (response == null) {
                return "";
            }

            String json = objectMapper.writeValueAsString(response);

            // 限制长度
            if (json.length() > 2000) {
                json = json.substring(0, 2000) + "...";
            }

            return json;

        } catch (Exception e) {
            log.warn("序列化响应数据失败: {}", e.getMessage());
            return "序列化失败: " + e.getMessage();
        }
    }

    /**
     * 判断是否为排除的类型
     */
    private boolean isExcludeType(Object arg, AuditLog auditLog) {
        if (arg == null) {
            return true;
        }

        Class<?>[] excludeTypes = auditLog.excludeTypes();
        for (Class<?> excludeType : excludeTypes) {
            if (excludeType.isInstance(arg)) {
                return true;
            }
        }

        // 特殊处理文件上传
        if (arg instanceof MultipartFile) {
            return true;
        }

        return false;
    }

    /**
     * 敏感信息脱敏
     */
    private String maskSensitiveFields(String json, String[] sensitiveFields) {
        String maskedJson = json;
        for (String field : sensitiveFields) {
            // 简单的正则替换，更复杂的脱敏逻辑可以根据需要扩展
            String regex = "\"" + field + "\"\\s*:\\s*\"[^\"]*\"";
            maskedJson = maskedJson.replaceAll(regex, "\"" + field + "\":\"******\"");
        }
        return maskedJson;
    }

    /**
     * 异步保存日志
     */
    @Async("logExecutor")
    public void saveLogAsync(OperationLog operationLog) {
        try {
            operationLog.setCreateTime(LocalDateTime.now());
            operationLog.setUpdateTime(LocalDateTime.now());
            operationLogMapper.insert(operationLog);
            log.debug("操作日志记录成功: {}", operationLog.getOperation());
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }
}