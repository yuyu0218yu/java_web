package com.zhangjiajie.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP地址工具类
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    
    /**
     * IP请求头列表，按优先级排序
     */
    private static final String[] IP_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取客户端IP地址
     */
    public static String getClientIp() {
        return getClientIp(WebUtil.getRequest());
    }

    /**
     * 获取客户端IP地址（从HttpServletRequest）
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = extractIpFromHeaders(request);
        
        // 如果从请求头未获取到，使用远程地址
        if (isEmptyOrUnknown(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果是多个IP地址，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
    
    /**
     * 从请求头中提取IP地址
     */
    private static String extractIpFromHeaders(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (!isEmptyOrUnknown(ip)) {
                return ip;
            }
        }
        return null;
    }
    
    /**
     * 判断IP是否为空或unknown
     */
    private static boolean isEmptyOrUnknown(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 判断是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // 本地环回地址
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return true;
        }

        // IPv4内网地址段
        if (ip.contains(".")) {
            return isIPv4Internal(ip);
        }

        // IPv6内网地址段
        if (ip.contains(":")) {
            return isIPv6Internal(ip);
        }

        return false;
    }

    /**
     * 判断是否为IPv4内网地址
     */
    private static boolean isIPv4Internal(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            // 10.0.0.0 - 10.255.255.255
            if (first == 10) {
                return true;
            }

            // 172.16.0.0 - 172.31.255.255
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }

            // 192.168.0.0 - 192.168.255.255
            if (first == 192 && second == 168) {
                return true;
            }

            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否为IPv6内网地址
     */
    private static boolean isIPv6Internal(String ip) {
        // 本地环回地址
        if (ip.startsWith("::1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return true;
        }

        // 链路本地地址 fe80::/10
        if (ip.startsWith("fe80:") || ip.startsWith("FE80:")) {
            return true;
        }

        // 站点本地地址 fec0::/10 (已废弃，但某些系统可能仍在使用)
        if (ip.startsWith("fec0:") || ip.startsWith("FEC0:")) {
            return true;
        }

        // 唯一本地地址 fc00::/7
        if (ip.startsWith("fc") || ip.startsWith("FC")) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为有效的IP地址
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        return isValidIPv4(ip) || isValidIPv6(ip);
    }

    /**
     * 判断是否为有效的IPv4地址
     */
    public static boolean isValidIPv4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否为有效的IPv6地址
     */
    public static boolean isValidIPv6(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        try {
            // 简单的IPv6格式验证
            java.net.InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 获取本机IP地址
     */
    public static String getLocalIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    /**
     * 获取本机主机名
     */
    public static String getLocalHostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    /**
     * IP地址转换为长整型
     */
    public static long ipToLong(String ip) {
        if (!isValidIPv4(ip)) {
            return 0;
        }

        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            try {
                int part = Integer.parseInt(parts[i]);
                result += part * Math.pow(256, 3 - i);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return result;
    }

    /**
     * 长整型转换为IP地址
     */
    public static String longToIp(long ip) {
        if (ip < 0 || ip > 4294967295L) {
            return "0.0.0.0";
        }

        return String.format("%d.%d.%d.%d",
            (ip >> 24) & 0xFF,
            (ip >> 16) & 0xFF,
            (ip >> 8) & 0xFF,
            ip & 0xFF);
    }

    /**
     * 获取IP地址掩码
     */
    public static String getSubnetMask(int prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            return "0.0.0.0";
        }

        long mask = 0xFFFFFFFFL << (32 - prefixLength);
        return longToIp(mask);
    }

    /**
     * 判断两个IP地址是否在同一网段
     */
    public static boolean isSameNetwork(String ip1, String ip2, int prefixLength) {
        if (!isValidIPv4(ip1) || !isValidIPv4(ip2)) {
            return false;
        }

        long longIp1 = ipToLong(ip1);
        long longIp2 = ipToLong(ip2);
        long mask = 0xFFFFFFFFL << (32 - prefixLength);

        return (longIp1 & mask) == (longIp2 & mask);
    }

    /**
     * 获取IP地址的网络部分
     */
    public static String getNetworkAddress(String ip, int prefixLength) {
        if (!isValidIPv4(ip)) {
            return "0.0.0.0";
        }

        long longIp = ipToLong(ip);
        long mask = 0xFFFFFFFFL << (32 - prefixLength);
        return longToIp(longIp & mask);
    }

    /**
     * 获取IP地址的广播地址
     */
    public static String getBroadcastAddress(String ip, int prefixLength) {
        if (!isValidIPv4(ip)) {
            return "255.255.255.255";
        }

        long longIp = ipToLong(ip);
        long mask = 0xFFFFFFFFL << (32 - prefixLength);
        long broadcast = (longIp & mask) | (~mask & 0xFFFFFFFFL);
        return longToIp(broadcast);
    }
}