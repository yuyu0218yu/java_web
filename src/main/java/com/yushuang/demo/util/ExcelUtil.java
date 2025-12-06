package com.yushuang.demo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel导入导出工具类
 * 基于EasyExcel实现
 *
 * @author yushuang
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出Excel到响应流
     *
     * @param response  HttpServletResponse
     * @param data      数据列表
     * @param clazz     实体类Class（需要标注@ExcelProperty注解）
     * @param sheetName Sheet名称
     * @param fileName  文件名（不含扩展名）
     */
    public static <T> void export(HttpServletResponse response, List<T> data,
                                   Class<T> clazz, String sheetName, String fileName) {
        try {
            // 设置响应头
            setExcelResponseHeader(response, fileName);

            // 导出Excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(data);

        } catch (IOException e) {
            log.error("Excel导出失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel导出失败", e);
        }
    }

    /**
     * 导出Excel到响应流（简化版，使用默认Sheet名称）
     */
    public static <T> void export(HttpServletResponse response, List<T> data,
                                   Class<T> clazz, String fileName) {
        export(response, data, clazz, "Sheet1", fileName);
    }

    /**
     * 导出Excel到输出流
     *
     * @param outputStream 输出流
     * @param data         数据列表
     * @param clazz        实体类Class
     * @param sheetName    Sheet名称
     */
    public static <T> void export(OutputStream outputStream, List<T> data,
                                   Class<T> clazz, String sheetName) {
        try {
            EasyExcel.write(outputStream, clazz)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(data);
        } catch (Exception e) {
            log.error("Excel导出失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel导出失败", e);
        }
    }

    /**
     * 导出Excel到文件
     *
     * @param filePath  文件路径
     * @param data      数据列表
     * @param clazz     实体类Class
     * @param sheetName Sheet名称
     */
    public static <T> void exportToFile(String filePath, List<T> data,
                                         Class<T> clazz, String sheetName) {
        try {
            EasyExcel.write(filePath, clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(data);
            log.info("Excel导出成功: {}", filePath);
        } catch (Exception e) {
            log.error("Excel导出到文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel导出失败", e);
        }
    }

    /**
     * 导出多Sheet的Excel
     *
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @param sheets   多个Sheet数据
     */
    public static void exportMultiSheet(HttpServletResponse response, String fileName,
                                         List<SheetData<?>> sheets) {
        try {
            // 设置响应头
            setExcelResponseHeader(response, fileName);

            // 创建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .build();

            // 写入多个Sheet
            for (int i = 0; i < sheets.size(); i++) {
                SheetData<?> sheetData = sheets.get(i);
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetData.getSheetName())
                        .head(sheetData.getClazz())
                        .build();
                excelWriter.write(sheetData.getData(), writeSheet);
            }

            // 关闭
            excelWriter.finish();

        } catch (IOException e) {
            log.error("多Sheet Excel导出失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel导出失败", e);
        }
    }

    /**
     * 从Excel文件导入数据
     *
     * @param filePath 文件路径
     * @param clazz    实体类Class
     * @return 数据列表
     */
    public static <T> List<T> importFromFile(String filePath, Class<T> clazz) {
        try {
            return EasyExcel.read(filePath)
                    .head(clazz)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.error("Excel导入失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel导入失败", e);
        }
    }

    /**
     * 设置Excel响应头
     */
    private static void setExcelResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");

            // 编码文件名
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
        } catch (Exception e) {
            log.error("设置响应头失败: {}", e.getMessage(), e);
        }
    }

    /**
     * Sheet数据封装类（用于多Sheet导出）
     */
    public static class SheetData<T> {
        private String sheetName;
        private Class<T> clazz;
        private List<T> data;

        public SheetData(String sheetName, Class<T> clazz, List<T> data) {
            this.sheetName = sheetName;
            this.clazz = clazz;
            this.data = data;
        }

        public String getSheetName() {
            return sheetName;
        }

        public Class<T> getClazz() {
            return clazz;
        }

        public List<T> getData() {
            return data;
        }

        public static <T> SheetData<T> of(String sheetName, Class<T> clazz, List<T> data) {
            return new SheetData<>(sheetName, clazz, data);
        }
    }
}
