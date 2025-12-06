package com.zhangjiajie.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjiajie.common.annotation.AuditLog;
import com.zhangjiajie.system.entity.OperationLog;
import com.zhangjiajie.system.mapper.OperationLogMapper;
import com.zhangjiajie.common.util.IpUtil;
import com.zhangjiajie.common.util.WebUtil;
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
import java.lang.reflect.Method;
import java.time.LocalDateTime;
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

    private static final int MAX_PARAM_LENGTH = 2000;
    private static final String TRUNCATION_SUFFIX = "...";
    private static final String MASKED_VALUE = "******";
    private static final String SERIALIZATION_ERROR_PREFIX = "序列化失败: ";

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(com.zhangjiajie.common.annotation.AuditLog)")
    public void auditLogPointCut() {}

    @Around("auditLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog operationLog = new OperationLog();

        try {
            prepareLogBefore(joinPoint, operationLog);
            Object result = joinPoint.proceed();

            updateLogOnSuccess(joinPoint, operationLog, result, startTime);
            return result;

        } catch (Exception e) {
            updateLogOnFailure(operationLog, e, startTime);
            throw e;
        } finally {
            saveLogAsync(operationLog);
        }
    }

    private void prepareLogBefore(ProceedingJoinPoint joinPoint, OperationLog operationLog) {
        AuditLog auditLog = getAuditLogAnnotation(joinPoint);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        operationLog.setOperation(getOperationDescription(auditLog, method));
        operationLog.setMethod(method.getDeclaringClass().getName() + "." + method.getName());

        setUserInfo(operationLog);
        setRequestInfo(joinPoint, operationLog, auditLog);
    }

    private void setUserInfo(OperationLog operationLog) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            operationLog.setUsername(authentication.getName());
        }
    }

    private void setRequestInfo(ProceedingJoinPoint joinPoint, OperationLog operationLog, AuditLog auditLog) {
        HttpServletRequest request = WebUtil.getRequest();
        if (request != null) {
            operationLog.setIp(IpUtil.getClientIp(request));
            operationLog.setUserAgent(request.getHeader("User-Agent"));

            if (auditLog.saveRequestData()) {
                operationLog.setParams(serializeArgs(joinPoint.getArgs(), auditLog));
            }
        }
    }

    private void updateLogOnSuccess(ProceedingJoinPoint joinPoint, OperationLog operationLog, 
                                     Object result, long startTime) {
        operationLog.setTime(System.currentTimeMillis() - startTime);
        operationLog.setStatus(OperationLog.Status.SUCCESS.getCode());

        if (getAuditLogAnnotation(joinPoint).saveResponseData()) {
            operationLog.setParams(serializeObject(result));
        }
    }

    private void updateLogOnFailure(OperationLog operationLog, Exception e, long startTime) {
        operationLog.setTime(System.currentTimeMillis() - startTime);
        operationLog.setStatus(OperationLog.Status.FAILURE.getCode());
        operationLog.setErrorMsg(e.getMessage());
        log.error("操作执行失败: {}", e.getMessage(), e);
    }

    private AuditLog getAuditLogAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(AuditLog.class);
    }

    private String getOperationDescription(AuditLog auditLog, Method method) {
        if (auditLog != null && !auditLog.operation().isEmpty()) {
            return auditLog.operation();
        }
        if (auditLog != null && !auditLog.module().isEmpty()) {
            return auditLog.module();
        }
        return method.getName();
    }

    private String serializeArgs(Object[] args, AuditLog auditLog) {
        if (args == null || args.length == 0) {
            return "";
        }

        try {
            List<Object> filteredArgs = Arrays.stream(args)
                    .filter(arg -> !isExcludeType(arg, auditLog))
                    .collect(Collectors.toList());

            if (filteredArgs.isEmpty()) {
                return "";
            }

            String json = objectMapper.writeValueAsString(filteredArgs);
            json = maskSensitiveFields(json, auditLog.sensitiveFields());
            return truncateIfNeeded(json);

        } catch (Exception e) {
            log.warn("序列化请求参数失败: {}", e.getMessage());
            return SERIALIZATION_ERROR_PREFIX + e.getMessage();
        }
    }

    private String serializeObject(Object obj) {
        if (obj == null) {
            return "";
        }

        try {
            return truncateIfNeeded(objectMapper.writeValueAsString(obj));
        } catch (Exception e) {
            log.warn("序列化响应数据失败: {}", e.getMessage());
            return SERIALIZATION_ERROR_PREFIX + e.getMessage();
        }
    }

    private String truncateIfNeeded(String str) {
        if (str.length() > MAX_PARAM_LENGTH) {
            return str.substring(0, MAX_PARAM_LENGTH) + TRUNCATION_SUFFIX;
        }
        return str;
    }

    private boolean isExcludeType(Object arg, AuditLog auditLog) {
        if (arg == null || arg instanceof MultipartFile) {
            return true;
        }

        for (Class<?> excludeType : auditLog.excludeTypes()) {
            if (excludeType.isInstance(arg)) {
                return true;
            }
        }
        return false;
    }

    private String maskSensitiveFields(String json, String[] sensitiveFields) {
        if (sensitiveFields == null || sensitiveFields.length == 0) {
            return json;
        }

        String maskedJson = json;
        for (String field : sensitiveFields) {
            String regex = "\"" + field + "\"\\s*:\\s*\"[^\"]*\"";
            maskedJson = maskedJson.replaceAll(regex, "\"" + field + "\":\"" + MASKED_VALUE + "\"");
        }
        return maskedJson;
    }

    @Async("logExecutor")
    public void saveLogAsync(OperationLog operationLog) {
        try {
            LocalDateTime now = LocalDateTime.now();
            operationLog.setCreateTime(now);
            operationLog.setUpdateTime(now);
            operationLogMapper.insert(operationLog);
            log.debug("操作日志记录成功: {}", operationLog.getOperation());
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }
}