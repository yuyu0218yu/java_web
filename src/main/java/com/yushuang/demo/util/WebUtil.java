package com.yushuang.demo.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Web请求工具类
 */
public class WebUtil {

    /**
     * 获取当前请求对象
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getRequest();
    }

    /**
     * 获取当前响应对象
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) attributes).getResponse();
    }

    /**
     * 获取请求参数
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request != null ? request.getParameter(name) : null;
    }

    /**
     * 获取请求参数（带默认值）
     */
    public static String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取请求参数转为整数
     */
    public static Integer getIntParameter(String name) {
        String value = getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取请求参数转为整数（带默认值）
     */
    public static Integer getIntParameter(String name, Integer defaultValue) {
        Integer value = getIntParameter(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取请求参数转为长整数
     */
    public static Long getLongParameter(String name) {
        String value = getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取请求参数转为长整数（带默认值）
     */
    public static Long getLongParameter(String name, Long defaultValue) {
        Long value = getLongParameter(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取请求参数转为布尔值
     */
    public static Boolean getBooleanParameter(String name) {
        String value = getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return "true".equalsIgnoreCase(value.trim()) || "1".equals(value.trim());
    }

    /**
     * 获取请求参数转为布尔值（带默认值）
     */
    public static Boolean getBooleanParameter(String name, Boolean defaultValue) {
        Boolean value = getBooleanParameter(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取所有请求参数
     */
    public static Map<String, String> getAllParameters() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return new HashMap<>();
        }

        Map<String, String> parameterMap = new HashMap<>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            parameterMap.put(name, request.getParameter(name));
        }
        return parameterMap;
    }

    /**
     * 获取请求头信息
     */
    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        return request != null ? request.getHeader(name) : null;
    }

    /**
     * 获取请求头信息（带默认值）
     */
    public static String getHeader(String name, String defaultValue) {
        String value = getHeader(name);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取所有请求头信息
     */
    public static Map<String, String> getAllHeaders() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return new HashMap<>();
        }

        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            headerMap.put(name, request.getHeader(name));
        }
        return headerMap;
    }

    /**
     * 获取客户端IP地址
     * 委托给IpUtil处理，避免重复代码
     */
    public static String getClientIpAddress() {
        return IpUtil.getClientIp();
    }

    /**
     * 获取User-Agent
     */
    public static String getUserAgent() {
        return getHeader("User-Agent");
    }

    /**
     * 获取请求URL
     */
    public static String getRequestURL() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getRequestURL().toString();
    }

    /**
     * 获取请求URI
     */
    public static String getRequestURI() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getRequestURI();
    }

    /**
     * 获取请求方法
     */
    public static String getRequestMethod() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getMethod();
    }

    /**
     * 获取ContextPath
     */
    public static String getContextPath() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getContextPath();
    }

    /**
     * 判断是否为AJAX请求
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return false;
        }

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        return (accept != null && accept.contains("application/json")) ||
               (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest"));
    }

    /**
     * 判断是否为GET请求
     */
    public static boolean isGetRequest() {
        return "GET".equalsIgnoreCase(getRequestMethod());
    }

    /**
     * 判断是否为POST请求
     */
    public static boolean isPostRequest() {
        return "POST".equalsIgnoreCase(getRequestMethod());
    }

    /**
     * 判断是否为PUT请求
     */
    public static boolean isPutRequest() {
        return "PUT".equalsIgnoreCase(getRequestMethod());
    }

    /**
     * 判断是否为DELETE请求
     */
    public static boolean isDeleteRequest() {
        return "DELETE".equalsIgnoreCase(getRequestMethod());
    }

    /**
     * 设置响应内容
     */
    public static void renderResponse(HttpServletResponse response, String content) {
        renderResponse(response, content, "application/json;charset=UTF-8");
    }

    /**
     * 设置响应内容（指定ContentType）
     */
    public static void renderResponse(HttpServletResponse response, String content, String contentType) {
        try {
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("响应输出失败", e);
        }
    }

    /**
     * 渲染JSON响应
     */
    public static void renderJsonResponse(HttpServletResponse response, Object obj) {
        String json = JsonUtil.toJson(obj);
        renderResponse(response, json);
    }

    /**
     * 设置响应头
     */
    public static void setResponseHeader(String name, String value) {
        HttpServletResponse response = getResponse();
        if (response != null) {
            response.setHeader(name, value);
        }
    }

    /**
     * 获取Session中的属性
     */
    public static Object getSessionAttribute(String name) {
        HttpServletRequest request = getRequest();
        if (request == null || request.getSession(false) == null) {
            return null;
        }
        return request.getSession().getAttribute(name);
    }

    /**
     * 设置Session属性
     */
    public static void setSessionAttribute(String name, Object value) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.getSession().setAttribute(name, value);
        }
    }

    /**
     * 移除Session属性
     */
    public static void removeSessionAttribute(String name) {
        HttpServletRequest request = getRequest();
        if (request != null && request.getSession(false) != null) {
            request.getSession().removeAttribute(name);
        }
    }

    /**
     * 使Session失效
     */
    public static void invalidateSession() {
        HttpServletRequest request = getRequest();
        if (request != null && request.getSession(false) != null) {
            request.getSession().invalidate();
        }
    }

    /**
     * 获取Request中的属性
     */
    public static Object getRequestAttribute(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getAttribute(name);
    }

    /**
     * 设置Request属性
     */
    public static void setRequestAttribute(String name, Object value) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute(name, value);
        }
    }

    /**
     * 移除Request属性
     */
    public static void removeRequestAttribute(String name) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.removeAttribute(name);
        }
    }
}