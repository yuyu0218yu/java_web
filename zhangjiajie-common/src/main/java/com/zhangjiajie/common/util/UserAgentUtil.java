package com.zhangjiajie.common.util;

/**
 * 用户代理工具类
 * 用于解析浏览器和操作系统信息
 *
 * @author yushuang
 * @since 2025-12-05
 */
public class UserAgentUtil {

    /**
     * 解析用户代理字符串
     */
    public static UserAgentInfo parseUserAgent(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return null;
        }

        UserAgentInfo info = new UserAgentInfo();
        info.setRaw(userAgent);

        // 解析浏览器
        info.setBrowser(parseBrowser(userAgent));

        // 解析操作系统
        info.setOperatingSystem(parseOperatingSystem(userAgent));

        // 解析地理位置（这里简化处理，实际可以通过IP地址查询）
        info.setLocation(parseLocation(userAgent));

        return info;
    }

    /**
     * 解析浏览器信息
     */
    private static String parseBrowser(String userAgent) {
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Edge") || userAgent.contains("Edg")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            return "Google Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            return "Opera";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else {
            return "Unknown Browser";
        }
    }

    /**
     * 解析操作系统信息
     */
    private static String parseOperatingSystem(String userAgent) {
        if (userAgent.contains("Windows NT")) {
            if (userAgent.contains("Windows NT 10.0")) {
                return "Windows 10";
            } else if (userAgent.contains("Windows NT 6.3")) {
                return "Windows 8.1";
            } else if (userAgent.contains("Windows NT 6.2")) {
                return "Windows 8";
            } else if (userAgent.contains("Windows NT 6.1")) {
                return "Windows 7";
            } else {
                return "Windows";
            }
        } else if (userAgent.contains("Mac OS X")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            if (userAgent.contains("Ubuntu")) {
                return "Ubuntu";
            } else {
                return "Linux";
            }
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        } else {
            return "Unknown OS";
        }
    }

    /**
     * 解析地理位置（简化版本，实际应该通过IP地址查询）
     */
    private static String parseLocation(String userAgent) {
        // 这里可以集成IP地理位置查询服务，如GeoIP2等
        // 现在返回默认值
        return "Unknown Location";
    }

    /**
     * 通过IP地址获取地理位置信息
     * 这是一个简化的实现，实际项目中可以集成专业的地理位置服务
     */
    public static String getLocationByIp(String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return "Unknown Location";
        }

        // 本地IP地址
        if (ip.startsWith("127.") || ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
            return "Local Network";
        }

        // 这里可以集成第三方IP地理位置查询服务
        // 比如调用高德地图、百度地图等API
        // 现在返回简化版本
        return "Unknown Location";
    }

    /**
     * 用户代理信息类
     */
    public static class UserAgentInfo {
        private String raw;
        private String browser;
        private String operatingSystem;
        private String location;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(String browser) {
            this.browser = browser;
        }

        public String getOperatingSystem() {
            return operatingSystem;
        }

        public void setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return String.format("Browser: %s, OS: %s, Location: %s", browser, operatingSystem, location);
        }
    }
}