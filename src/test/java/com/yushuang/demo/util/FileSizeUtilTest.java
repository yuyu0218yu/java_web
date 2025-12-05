package com.yushuang.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileSizeUtil 单元测试
 */
class FileSizeUtilTest {

    @Test
    void testFormatFileSize_Bytes() {
        assertEquals("0 B", FileSizeUtil.formatFileSize(0L));
        assertEquals("100.00 B", FileSizeUtil.formatFileSize(100L));
        assertEquals("1023.00 B", FileSizeUtil.formatFileSize(1023L));
    }

    @Test
    void testFormatFileSize_Kilobytes() {
        assertEquals("1.00 KB", FileSizeUtil.formatFileSize(1024L));
        assertEquals("1.50 KB", FileSizeUtil.formatFileSize(1536L));
        assertEquals("1023.00 KB", FileSizeUtil.formatFileSize(1024L * 1023));
    }

    @Test
    void testFormatFileSize_Megabytes() {
        assertEquals("1.00 MB", FileSizeUtil.formatFileSize(1024L * 1024));
        assertEquals("10.00 MB", FileSizeUtil.formatFileSize(1024L * 1024 * 10));
    }

    @Test
    void testFormatFileSize_Gigabytes() {
        assertEquals("1.00 GB", FileSizeUtil.formatFileSize(1024L * 1024 * 1024));
    }

    @Test
    void testFormatFileSize_Null() {
        assertEquals("0 B", FileSizeUtil.formatFileSize(null));
    }

    @Test
    void testToKB() {
        assertEquals(1.0, FileSizeUtil.toKB(1024), 0.001);
        assertEquals(0.5, FileSizeUtil.toKB(512), 0.001);
    }

    @Test
    void testToMB() {
        assertEquals(1.0, FileSizeUtil.toMB(1024 * 1024), 0.001);
    }

    @Test
    void testToGB() {
        assertEquals(1.0, FileSizeUtil.toGB(1024L * 1024 * 1024), 0.001);
    }
}
