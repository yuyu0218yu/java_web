package com.yushuang.demo.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 单元测试代码生成器
 * 生成Controller和Service的单元测试代码
 *
 * 使用方法：
 * UnitTestGenerator.generate("User", "用户");
 *
 * @author yushuang
 */
public class UnitTestGenerator {

    private static final String BASE_PACKAGE = "com.yushuang.demo";
    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/test/java/com/yushuang/demo";
    private static final String AUTHOR = "yushuang";

    public static void main(String[] args) {
        // 示例：生成User实体的测试代码
        generate("User", "用户");
    }

    /**
     * 生成单元测试代码
     *
     * @param entityName 实体类名（如：User, Product）
     * @param entityCnName 实体中文名（如：用户、产品）
     */
    public static void generate(String entityName, String entityCnName) {
        try {
            generateControllerTest(entityName, entityCnName);
            generateServiceTest(entityName, entityCnName);

            System.out.println("========================================");
            System.out.println("单元测试代码生成完成！");
            System.out.println("实体: " + entityName);
            System.out.println("生成文件:");
            System.out.println("  - " + entityName + "ControllerTest.java");
            System.out.println("  - " + entityName + "ServiceTest.java");
            System.out.println("========================================");
        } catch (IOException e) {
            System.err.println("生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成Controller测试
     */
    private static void generateControllerTest(String entityName, String entityCnName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
            package %s.controller;

            import com.baomidou.mybatisplus.core.metadata.IPage;
            import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
            import %s.entity.%s;
            import %s.service.%sService;
            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Test;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
            import org.springframework.boot.test.mock.mockito.MockBean;
            import org.springframework.http.MediaType;
            import org.springframework.security.test.context.support.WithMockUser;
            import org.springframework.test.web.servlet.MockMvc;

            import java.util.Arrays;
            import java.util.List;

            import static org.mockito.ArgumentMatchers.any;
            import static org.mockito.ArgumentMatchers.anyLong;
            import static org.mockito.Mockito.when;
            import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
            import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
            import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

            /**
             * %s控制器测试类
             *
             * @author %s
             * @since %s
             */
            @WebMvcTest(%sController.class)
            @DisplayName("%sController测试")
            class %sControllerTest {

                @Autowired
                private MockMvc mockMvc;

                @MockBean
                private %sService %sService;

                private %s test%s;

                @BeforeEach
                void setUp() {
                    test%s = new %s();
                    test%s.setId(1L);
                    // 设置测试数据的其他字段
                }

                @Test
                @WithMockUser(authorities = {"%s:view"})
                @DisplayName("测试分页查询%s列表")
                void testGetPage() throws Exception {
                    // 准备测试数据
                    Page<%s> page = new Page<>(1, 10);
                    page.setRecords(Arrays.asList(test%s));
                    page.setTotal(1);

                    when(%sService.page(any(Page.class))).thenReturn(page);

                    // 执行测试
                    mockMvc.perform(get("/api/%s/page")
                            .param("current", "1")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.data.total").value(1));
                }

                @Test
                @WithMockUser(authorities = {"%s:view"})
                @DisplayName("测试根据ID查询%s")
                void testGetById() throws Exception {
                    when(%sService.getById(anyLong())).thenReturn(test%s);

                    mockMvc.perform(get("/api/%s/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.data.id").value(1));
                }

                @Test
                @WithMockUser(authorities = {"%s:view"})
                @DisplayName("测试查询所有%s")
                void testList() throws Exception {
                    List<%s> list = Arrays.asList(test%s);
                    when(%sService.list()).thenReturn(list);

                    mockMvc.perform(get("/api/%s/list")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200))
                            .andExpect(jsonPath("$.data").isArray());
                }

                @Test
                @WithMockUser(authorities = {"%s:create"})
                @DisplayName("测试创建%s")
                void testCreate() throws Exception {
                    when(%sService.save(any(%s.class))).thenReturn(true);

                    String requestBody = "{}"; // 根据实体添加实际的JSON数据

                    mockMvc.perform(post("/api/%s")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200));
                }

                @Test
                @WithMockUser(authorities = {"%s:update"})
                @DisplayName("测试更新%s")
                void testUpdate() throws Exception {
                    when(%sService.updateById(any(%s.class))).thenReturn(true);

                    String requestBody = "{}"; // 根据实体添加实际的JSON数据

                    mockMvc.perform(put("/api/%s/{id}", 1L)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200));
                }

                @Test
                @WithMockUser(authorities = {"%s:delete"})
                @DisplayName("测试删除%s")
                void testDelete() throws Exception {
                    when(%sService.removeById(anyLong())).thenReturn(true);

                    mockMvc.perform(delete("/api/%s/{id}", 1L)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200));
                }

                @Test
                @WithMockUser(authorities = {"%s:delete"})
                @DisplayName("测试批量删除%s")
                void testDeleteBatch() throws Exception {
                    when(%sService.removeByIds(any(List.class))).thenReturn(true);

                    String requestBody = "[1, 2, 3]";

                    mockMvc.perform(delete("/api/%s/batch")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(200));
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDateTime(), entityName, entityName, entityName,
            entityName, lowerEntityName, entityName, entityName,
            entityName, entityName, entityName,
            lowerEntityName, entityCnName, entityName, entityName, lowerEntityName, urlPath,
            lowerEntityName, entityCnName, lowerEntityName, entityName, urlPath,
            lowerEntityName, entityCnName, entityName, entityName, lowerEntityName, urlPath,
            lowerEntityName, entityCnName, lowerEntityName, entityName, urlPath,
            lowerEntityName, entityCnName, lowerEntityName, entityName, urlPath,
            lowerEntityName, entityCnName, lowerEntityName, urlPath,
            lowerEntityName, entityCnName, lowerEntityName, urlPath
        );

        writeToFile("controller", entityName + "ControllerTest.java", content);
    }

    /**
     * 生成Service测试
     */
    private static void generateServiceTest(String entityName, String entityCnName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
            package %s.service;

            import com.baomidou.mybatisplus.core.metadata.IPage;
            import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
            import %s.entity.%s;
            import %s.mapper.%sMapper;
            import org.junit.jupiter.api.BeforeEach;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.Test;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.boot.test.context.SpringBootTest;
            import org.springframework.boot.test.mock.mockito.MockBean;

            import java.util.Arrays;
            import java.util.List;

            import static org.assertj.core.api.Assertions.assertThat;
            import static org.mockito.ArgumentMatchers.any;
            import static org.mockito.ArgumentMatchers.anyLong;
            import static org.mockito.Mockito.when;

            /**
             * %s服务测试类
             *
             * @author %s
             * @since %s
             */
            @SpringBootTest
            @DisplayName("%sService测试")
            class %sServiceTest {

                @Autowired
                private %sService %sService;

                @MockBean
                private %sMapper %sMapper;

                private %s test%s;

                @BeforeEach
                void setUp() {
                    test%s = new %s();
                    test%s.setId(1L);
                    // 设置测试数据的其他字段
                }

                @Test
                @DisplayName("测试保存%s")
                void testSave() {
                    when(%sMapper.insert(any(%s.class))).thenReturn(1);

                    boolean result = %sService.save(test%s);

                    assertThat(result).isTrue();
                }

                @Test
                @DisplayName("测试根据ID查询%s")
                void testGetById() {
                    when(%sMapper.selectById(anyLong())).thenReturn(test%s);

                    %s result = %sService.getById(1L);

                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isEqualTo(1L);
                }

                @Test
                @DisplayName("测试查询列表")
                void testList() {
                    when(%sMapper.selectList(any())).thenReturn(Arrays.asList(test%s));

                    List<%s> result = %sService.list();

                    assertThat(result).isNotEmpty();
                    assertThat(result).hasSize(1);
                }

                @Test
                @DisplayName("测试更新%s")
                void testUpdate() {
                    when(%sMapper.updateById(any(%s.class))).thenReturn(1);

                    boolean result = %sService.updateById(test%s);

                    assertThat(result).isTrue();
                }

                @Test
                @DisplayName("测试删除%s")
                void testDelete() {
                    when(%sMapper.deleteById(anyLong())).thenReturn(1);

                    boolean result = %sService.removeById(1L);

                    assertThat(result).isTrue();
                }

                @Test
                @DisplayName("测试批量删除%s")
                void testDeleteBatch() {
                    when(%sMapper.deleteBatchIds(any(List.class))).thenReturn(3);

                    boolean result = %sService.removeByIds(Arrays.asList(1L, 2L, 3L));

                    assertThat(result).isTrue();
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDateTime(), entityName, entityName,
            entityName, lowerEntityName, entityName, lowerEntityName,
            entityName, entityName, entityName, entityName, entityName,
            entityCnName, lowerEntityName, entityName, lowerEntityName, entityName,
            entityCnName, lowerEntityName, entityName, entityName, lowerEntityName,
            lowerEntityName, entityName, entityName, lowerEntityName,
            entityCnName, lowerEntityName, entityName, lowerEntityName, entityName,
            entityCnName, lowerEntityName, lowerEntityName,
            entityCnName, lowerEntityName, lowerEntityName
        );

        writeToFile("service", entityName + "ServiceTest.java", content);
    }

    /**
     * 将内容写入文件
     */
    private static void writeToFile(String packageName, String fileName, String content) throws IOException {
        String dirPath = BASE_PATH + "/" + packageName.replace(".", "/");
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    /**
     * 转换为小驼峰命名
     */
    private static String toLowerCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 转换为kebab-case命名（用于URL）
     */
    private static String toKebabCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('-');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 获取当前日期时间
     */
    private static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
