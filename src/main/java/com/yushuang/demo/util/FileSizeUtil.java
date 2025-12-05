package com.yushuang.demo.util;

/**
 * 文件大小工具类
 * 用于文件大小的格式化和计算
 *
 * @author yushuang
 * @since 2025-12-05
 */
public class FileSizeUtil {

    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB"};
    private static final long UNIT_THRESHOLD = 1024L;

    /**
     * 私有构造函数，防止实例化
     */
    private FileSizeUtil() {
    }

    /**
     * 格式化文件大小为友好显示格式
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串，如 "1.50 MB"
     */
    public static String formatFileSize(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }

        int unitIndex = 0;
        double fileSize = size.doubleValue();

        while (fileSize >= UNIT_THRESHOLD && unitIndex < SIZE_UNITS.length - 1) {
            fileSize /= UNIT_THRESHOLD;
            unitIndex++;
        }

        return String.format("%.2f %s", fileSize, SIZE_UNITS[unitIndex]);
    }

    /**
     * 格式化文件大小（使用整数显示）
     *
     * @param size 文件大小（字节）
     * @return 格式化后的字符串
     */
    public static String formatFileSizeSimple(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }

        int unitIndex = 0;
        long fileSize = size;

        while (fileSize >= UNIT_THRESHOLD && unitIndex < SIZE_UNITS.length - 1) {
            fileSize /= UNIT_THRESHOLD;
            unitIndex++;
        }

        return String.format("%d %s", fileSize, SIZE_UNITS[unitIndex]);
    }

    /**
     * 将字节数转换为KB
     *
     * @param bytes 字节数
     * @return KB数
     */
    public static double toKB(long bytes) {
        return bytes / 1024.0;
    }

    /**
     * 将字节数转换为MB
     *
     * @param bytes 字节数
     * @return MB数
     */
    public static double toMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }

    /**
     * 将字节数转换为GB
     *
     * @param bytes 字节数
     * @return GB数
     */
    public static double toGB(long bytes) {
        return bytes / (1024.0 * 1024.0 * 1024.0);
    }
}
