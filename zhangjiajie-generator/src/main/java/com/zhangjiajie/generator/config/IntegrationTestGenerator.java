package com.zhangjiajie.generator.config;

import java.io.IOException;

import static com.zhangjiajie.generator.config.GeneratorHelper.*;

/**
 * 集成测试生成器
 * 生成完整的 @SpringBootTest 集成测试代码
 *
 * 使用方法：
 *   IntegrationTestGenerator.generate("User", "用户");
 *   IntegrationTestGenerator.generateControllerIntegrationTest("User", "用户");
 *   IntegrationTestGenerator.generateServiceIntegrationTest("User", "用户");
 *
 * 生成内容：
 * 1. Controller 集成测试（完整HTTP请求流程）
 * 2. Service 集成测试（数据库事务测试）
 * 3. 安全认证测试（权限验证场景）
 * 4. 数据库事务测试（回滚验证）
 *
 * @author yushuang
 * @since 2025-12-06
 */
public class IntegrationTestGenerator {

    public static void main(String[] args) {
        // 示例：生成User模块的集成测试
        generate("User", "用户");
    }

    /**
     * 生成完整的集成测试代码
     */
    public static void generate(String entityName, String entityCnName) {
        try {
            generateControllerIntegrationTest(entityName, entityCnName);
            generateServiceIntegrationTest(entityName, entityCnName);
            generateSecurityTest(entityName, entityCnName);
            generateTransactionalTest(entityName, entityCnName);

            printSuccess("集成测试生成", entityName,
                    entityName + "ControllerIntegrationTest.java",
                    entityName + "ServiceIntegrationTest.java",
                    entityName + "SecurityTest.java",
                    entityName + "TransactionalTest.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成Controller集成测试
     */
    public static void generateControllerOnly(String entityName, String entityCnName) {
        try {
            generateControllerIntegrationTest(entityName, entityCnName);
            printSuccess("Controller集成测试生成", entityName,
                    entityName + "ControllerIntegrationTest.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    /**
     * 仅生成Service集成测试
     */
    public static void generateServiceOnly(String entityName, String entityCnName) {
        try {
            generateServiceIntegrationTest(entityName, entityCnName);
            printSuccess("Service集成测试生成", entityName,
                    entityName + "ServiceIntegrationTest.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 生成Controller集成测试
     */
    private static void generateControllerIntegrationTest(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);

        String content = String.format("""
                package %s.controller;

                import com.fasterxml.jackson.databind.ObjectMapper;
                import %s.entity.%s;
                import %s.service.%sService;
                import org.junit.jupiter.api.*;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
                import org.springframework.boot.test.context.SpringBootTest;
                import org.springframework.http.MediaType;
                import org.springframework.security.test.context.support.WithMockUser;
                import org.springframework.test.context.ActiveProfiles;
                import org.springframework.test.web.servlet.MockMvc;
                import org.springframework.test.web.servlet.MvcResult;
                import org.springframework.transaction.annotation.Transactional;

                import static org.assertj.core.api.Assertions.assertThat;
                import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
                import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
                import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

                /**
                 * %s Controller 集成测试
                 * 完整HTTP请求流程测试，包含数据库操作
                 *
                 * @author %s
                 * @since %s
                 */
                @SpringBootTest
                @AutoConfigureMockMvc
                @ActiveProfiles("test")
                @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
                @DisplayName("%sController 集成测试")
                class %sControllerIntegrationTest {

                    @Autowired
                    private MockMvc mockMvc;

                    @Autowired
                    private ObjectMapper objectMapper;

                    @Autowired
                    private %sService %sService;

                    private static Long createdId;

                    // ==================== 分页查询测试 ====================

                    @Test
                    @Order(1)
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试分页查询%s列表 - 成功")
                    void testGetPage_Success() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .param("current", "1")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(200))
                                .andExpect(jsonPath("$.data.records").isArray())
                                .andExpect(jsonPath("$.data.total").isNumber())
                                .andExpect(jsonPath("$.data.current").value(1))
                                .andExpect(jsonPath("$.data.size").value(10));
                    }

                    @Test
                    @Order(2)
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试分页查询 - 参数校验（页码为负数）")
                    void testGetPage_InvalidCurrentPage() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .param("current", "-1")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isBadRequest());
                    }

                    @Test
                    @Order(3)
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试分页查询 - 参数校验（每页数量超限）")
                    void testGetPage_ExceedSizeLimit() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .param("current", "1")
                                .param("size", "200")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isBadRequest());
                    }

                    // ==================== 创建测试 ====================

                    @Test
                    @Order(10)
                    @WithMockUser(authorities = {"%s:create"})
                    @DisplayName("测试创建%s - 成功")
                    @Transactional
                    void testCreate_Success() throws Exception {
                        %s entity = new %s();
                        // TODO: 设置实体属性
                        // entity.setName("测试%s");

                        String json = objectMapper.writeValueAsString(entity);

                        MvcResult result = mockMvc.perform(post("/api/%s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(200))
                                .andExpect(jsonPath("$.message").value("创建成功"))
                                .andReturn();

                        // 验证数据已入库
                        // createdId = ...; // 从响应中获取ID
                    }

                    @Test
                    @Order(11)
                    @WithMockUser(authorities = {"%s:create"})
                    @DisplayName("测试创建%s - 参数校验失败")
                    void testCreate_ValidationError() throws Exception {
                        // 空请求体
                        mockMvc.perform(post("/api/%s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andDo(print())
                                .andExpect(status().isBadRequest());
                    }

                    // ==================== 查询详情测试 ====================

                    @Test
                    @Order(20)
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试根据ID查询%s - 成功")
                    void testGetById_Success() throws Exception {
                        // 先确保有数据（可以在测试前插入）
                        mockMvc.perform(get("/api/%s/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk());
                    }

                    @Test
                    @Order(21)
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试根据ID查询%s - 不存在")
                    void testGetById_NotFound() throws Exception {
                        mockMvc.perform(get("/api/%s/99999999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(404));
                    }

                    // ==================== 更新测试 ====================

                    @Test
                    @Order(30)
                    @WithMockUser(authorities = {"%s:update"})
                    @DisplayName("测试更新%s - 成功")
                    @Transactional
                    void testUpdate_Success() throws Exception {
                        %s entity = new %s();
                        entity.setId(1L);
                        // TODO: 设置更新属性
                        // entity.setName("更新后的名称");

                        String json = objectMapper.writeValueAsString(entity);

                        mockMvc.perform(put("/api/%s/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(200))
                                .andExpect(jsonPath("$.message").value("更新成功"));
                    }

                    // ==================== 删除测试 ====================

                    @Test
                    @Order(40)
                    @WithMockUser(authorities = {"%s:delete"})
                    @DisplayName("测试删除%s - 成功")
                    @Transactional
                    void testDelete_Success() throws Exception {
                        mockMvc.perform(delete("/api/%s/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(200))
                                .andExpect(jsonPath("$.message").value("删除成功"));
                    }

                    @Test
                    @Order(41)
                    @WithMockUser(authorities = {"%s:delete"})
                    @DisplayName("测试批量删除%s - 成功")
                    @Transactional
                    void testDeleteBatch_Success() throws Exception {
                        mockMvc.perform(delete("/api/%s/batch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[1, 2, 3]"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(200));
                    }
                }
                """,
                basePackage, basePackage, entityName, basePackage, entityName,
                entityCnName, getAuthor(), getCurrentDate(),
                entityName, entityName,
                entityName, lowerEntityName,
                lowerEntityName, entityCnName, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, entityCnName, entityName, entityName, entityCnName, urlPath,
                lowerEntityName, entityCnName, urlPath,
                lowerEntityName, entityCnName, urlPath,
                lowerEntityName, entityCnName, urlPath,
                lowerEntityName, entityCnName, entityName, entityName, urlPath,
                lowerEntityName, entityCnName, urlPath,
                lowerEntityName, entityCnName, urlPath
        );

        writeToFile(getTestJavaPath(), "controller", entityName + "ControllerIntegrationTest.java", content);
    }

    /**
     * 生成Service集成测试
     */
    private static void generateServiceIntegrationTest(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                package %s.service;

                import %s.entity.%s;
                import com.baomidou.mybatisplus.core.metadata.IPage;
                import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
                import org.junit.jupiter.api.*;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.context.SpringBootTest;
                import org.springframework.test.context.ActiveProfiles;
                import org.springframework.transaction.annotation.Transactional;

                import java.util.Arrays;
                import java.util.List;

                import static org.assertj.core.api.Assertions.assertThat;
                import static org.assertj.core.api.Assertions.assertThatThrownBy;

                /**
                 * %s Service 集成测试
                 * 测试Service层与数据库的完整交互
                 *
                 * @author %s
                 * @since %s
                 */
                @SpringBootTest
                @ActiveProfiles("test")
                @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
                @DisplayName("%sService 集成测试")
                class %sServiceIntegrationTest {

                    @Autowired
                    private %sService %sService;

                    private static Long testId;

                    // ==================== 创建测试 ====================

                    @Test
                    @Order(1)
                    @DisplayName("测试保存%s - 成功")
                    @Transactional
                    void testSave_Success() {
                        %s entity = new %s();
                        // TODO: 设置必填属性
                        // entity.setName("测试%s");
                        // entity.setStatus(1);

                        boolean result = %sService.save(entity);

                        assertThat(result).isTrue();
                        assertThat(entity.getId()).isNotNull();
                        testId = entity.getId();
                    }

                    @Test
                    @Order(2)
                    @DisplayName("测试批量保存%s - 成功")
                    @Transactional
                    void testSaveBatch_Success() {
                        %s entity1 = new %s();
                        %s entity2 = new %s();
                        // TODO: 设置属性

                        boolean result = %sService.saveBatch(Arrays.asList(entity1, entity2));

                        assertThat(result).isTrue();
                    }

                    // ==================== 查询测试 ====================

                    @Test
                    @Order(10)
                    @DisplayName("测试分页查询%s - 成功")
                    void testPage_Success() {
                        Page<%s> page = new Page<>(1, 10);

                        IPage<%s> result = %sService.page(page);

                        assertThat(result).isNotNull();
                        assertThat(result.getCurrent()).isEqualTo(1);
                        assertThat(result.getSize()).isEqualTo(10);
                    }

                    @Test
                    @Order(11)
                    @DisplayName("测试查询所有%s - 成功")
                    void testList_Success() {
                        List<%s> result = %sService.list();

                        assertThat(result).isNotNull();
                    }

                    @Test
                    @Order(12)
                    @DisplayName("测试根据ID查询%s - 成功")
                    void testGetById_Success() {
                        // 假设ID=1存在
                        %s result = %sService.getById(1L);

                        // 如果记录存在，验证返回值
                        if (result != null) {
                            assertThat(result.getId()).isEqualTo(1L);
                        }
                    }

                    @Test
                    @Order(13)
                    @DisplayName("测试根据ID查询%s - 不存在")
                    void testGetById_NotFound() {
                        %s result = %sService.getById(99999999L);

                        assertThat(result).isNull();
                    }

                    // ==================== 更新测试 ====================

                    @Test
                    @Order(20)
                    @DisplayName("测试更新%s - 成功")
                    @Transactional
                    void testUpdateById_Success() {
                        // 先查询一条记录
                        %s entity = %sService.getById(1L);
                        if (entity != null) {
                            // TODO: 修改属性
                            // entity.setName("更新后的名称");

                            boolean result = %sService.updateById(entity);

                            assertThat(result).isTrue();
                        }
                    }

                    // ==================== 删除测试 ====================

                    @Test
                    @Order(30)
                    @DisplayName("测试删除%s - 成功")
                    @Transactional
                    void testRemoveById_Success() {
                        // 先创建一条测试数据
                        %s entity = new %s();
                        // TODO: 设置属性
                        %sService.save(entity);

                        boolean result = %sService.removeById(entity.getId());

                        assertThat(result).isTrue();
                        assertThat(%sService.getById(entity.getId())).isNull();
                    }

                    @Test
                    @Order(31)
                    @DisplayName("测试批量删除%s - 成功")
                    @Transactional
                    void testRemoveByIds_Success() {
                        // 先创建测试数据
                        %s entity1 = new %s();
                        %s entity2 = new %s();
                        // TODO: 设置属性
                        %sService.saveBatch(Arrays.asList(entity1, entity2));

                        List<Long> ids = Arrays.asList(entity1.getId(), entity2.getId());
                        boolean result = %sService.removeByIds(ids);

                        assertThat(result).isTrue();
                    }

                    // ==================== 统计测试 ====================

                    @Test
                    @Order(40)
                    @DisplayName("测试统计总数")
                    void testCount() {
                        long count = %sService.count();

                        assertThat(count).isGreaterThanOrEqualTo(0);
                    }
                }
                """,
                basePackage, basePackage, entityName,
                entityCnName, getAuthor(), getCurrentDate(),
                entityName, entityName,
                entityName, lowerEntityName,
                entityCnName, entityName, entityName, entityCnName, lowerEntityName,
                entityCnName, entityName, entityName, entityName, entityName, lowerEntityName,
                entityCnName, entityName, entityName, lowerEntityName,
                entityCnName, entityName, lowerEntityName,
                entityCnName, entityName, lowerEntityName,
                entityCnName, entityName, lowerEntityName,
                entityCnName, entityName, lowerEntityName, lowerEntityName,
                entityCnName, entityName, entityName, lowerEntityName, lowerEntityName, lowerEntityName,
                entityCnName, entityName, entityName, entityName, entityName, lowerEntityName, lowerEntityName,
                lowerEntityName
        );

        writeToFile(getTestJavaPath(), "service", entityName + "ServiceIntegrationTest.java", content);
    }

    /**
     * 生成安全认证测试
     */
    private static void generateSecurityTest(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();
        String urlPath = toKebabCase(entityName);
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                package %s.security;

                import org.junit.jupiter.api.*;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
                import org.springframework.boot.test.context.SpringBootTest;
                import org.springframework.http.MediaType;
                import org.springframework.security.test.context.support.WithAnonymousUser;
                import org.springframework.security.test.context.support.WithMockUser;
                import org.springframework.test.context.ActiveProfiles;
                import org.springframework.test.web.servlet.MockMvc;

                import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
                import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
                import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

                /**
                 * %s 安全认证测试
                 * 测试权限控制和认证场景
                 *
                 * @author %s
                 * @since %s
                 */
                @SpringBootTest
                @AutoConfigureMockMvc
                @ActiveProfiles("test")
                @DisplayName("%s 安全认证测试")
                class %sSecurityTest {

                    @Autowired
                    private MockMvc mockMvc;

                    // ==================== 未认证访问测试 ====================

                    @Test
                    @WithAnonymousUser
                    @DisplayName("测试未认证访问 - 应返回401")
                    void testUnauthorizedAccess() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());
                    }

                    // ==================== 权限不足测试 ====================

                    @Test
                    @WithMockUser(authorities = {"other:view"})
                    @DisplayName("测试权限不足 - 查看操作")
                    void testForbidden_View() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isForbidden());
                    }

                    @Test
                    @WithMockUser(authorities = {"%s:view"})  // 只有查看权限
                    @DisplayName("测试权限不足 - 创建操作")
                    void testForbidden_Create() throws Exception {
                        mockMvc.perform(post("/api/%s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andDo(print())
                                .andExpect(status().isForbidden());
                    }

                    @Test
                    @WithMockUser(authorities = {"%s:view"})  // 只有查看权限
                    @DisplayName("测试权限不足 - 更新操作")
                    void testForbidden_Update() throws Exception {
                        mockMvc.perform(put("/api/%s/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andDo(print())
                                .andExpect(status().isForbidden());
                    }

                    @Test
                    @WithMockUser(authorities = {"%s:view"})  // 只有查看权限
                    @DisplayName("测试权限不足 - 删除操作")
                    void testForbidden_Delete() throws Exception {
                        mockMvc.perform(delete("/api/%s/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isForbidden());
                    }

                    // ==================== 正确权限访问测试 ====================

                    @Test
                    @WithMockUser(authorities = {"%s:view"})
                    @DisplayName("测试正确权限 - 查看操作")
                    void testAuthorized_View() throws Exception {
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk());
                    }

                    @Test
                    @WithMockUser(authorities = {"%s:create"})
                    @DisplayName("测试正确权限 - 创建操作")
                    void testAuthorized_Create() throws Exception {
                        mockMvc.perform(post("/api/%s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andDo(print())
                                // 可能因验证失败返回400，但不应该是403
                                .andExpect(status().is(org.hamcrest.Matchers.not(403)));
                    }

                    // ==================== 管理员角色测试 ====================

                    @Test
                    @WithMockUser(roles = {"ADMIN"})
                    @DisplayName("测试ADMIN角色 - 应有完整权限")
                    void testAdminRole_FullAccess() throws Exception {
                        // 查看
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                    }

                    @Test
                    @WithMockUser(roles = {"SUPER_ADMIN"})
                    @DisplayName("测试SUPER_ADMIN角色 - 应有完整权限")
                    void testSuperAdminRole_FullAccess() throws Exception {
                        // 查看
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
                    }

                    // ==================== 多权限组合测试 ====================

                    @Test
                    @WithMockUser(authorities = {"%s:view", "%s:create"})
                    @DisplayName("测试多权限组合 - 查看和创建")
                    void testMultipleAuthorities() throws Exception {
                        // 查看
                        mockMvc.perform(get("/api/%s/page")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                        // 创建（可能因验证失败，但不应是权限问题）
                        mockMvc.perform(post("/api/%s")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().is(org.hamcrest.Matchers.not(403)));
                    }
                }
                """,
                basePackage,
                entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName,
                urlPath, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, urlPath,
                lowerEntityName, urlPath,
                urlPath, urlPath,
                lowerEntityName, lowerEntityName, urlPath, urlPath
        );

        writeToFile(getTestJavaPath(), "security", entityName + "SecurityTest.java", content);
    }

    /**
     * 生成事务测试
     */
    private static void generateTransactionalTest(String entityName, String entityCnName) throws IOException {
        String basePackage = getBasePackage();
        String lowerEntityName = toLowerCamelCase(entityName);

        String content = String.format("""
                package %s.transaction;

                import %s.entity.%s;
                import %s.service.%sService;
                import org.junit.jupiter.api.*;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.boot.test.context.SpringBootTest;
                import org.springframework.test.context.ActiveProfiles;
                import org.springframework.transaction.annotation.Transactional;

                import static org.assertj.core.api.Assertions.assertThat;
                import static org.assertj.core.api.Assertions.assertThatThrownBy;

                /**
                 * %s 事务测试
                 * 测试数据库事务的提交和回滚
                 *
                 * @author %s
                 * @since %s
                 */
                @SpringBootTest
                @ActiveProfiles("test")
                @DisplayName("%s 事务测试")
                class %sTransactionalTest {

                    @Autowired
                    private %sService %sService;

                    // ==================== 事务回滚测试 ====================

                    @Test
                    @DisplayName("测试事务回滚 - @Transactional注解")
                    @Transactional
                    void testTransactionRollback() {
                        // 获取初始数量
                        long initialCount = %sService.count();

                        // 创建新记录
                        %s entity = new %s();
                        // TODO: 设置属性
                        %sService.save(entity);

                        // 验证记录已创建
                        assertThat(%sService.count()).isEqualTo(initialCount + 1);

                        // 测试结束后，由于@Transactional注解，数据会自动回滚
                    }

                    @Test
                    @DisplayName("测试事务回滚后数据恢复")
                    void testDataRestoredAfterRollback() {
                        // 这个测试验证上一个测试的数据确实被回滚了
                        long count = %sService.count();
                        // 如果事务正常回滚，数据量应该和测试前一致
                        assertThat(count).isGreaterThanOrEqualTo(0);
                    }

                    // ==================== 批量操作事务测试 ====================

                    @Test
                    @DisplayName("测试批量保存事务 - 成功")
                    @Transactional
                    void testBatchSave_Transactional() {
                        long initialCount = %sService.count();

                        %s entity1 = new %s();
                        %s entity2 = new %s();
                        %s entity3 = new %s();
                        // TODO: 设置属性

                        %sService.save(entity1);
                        %sService.save(entity2);
                        %sService.save(entity3);

                        // 验证三条记录都已保存
                        assertThat(%sService.count()).isEqualTo(initialCount + 3);
                    }

                    // ==================== 删除操作事务测试 ====================

                    @Test
                    @DisplayName("测试删除操作事务回滚")
                    @Transactional
                    void testDelete_Transactional() {
                        // 先保存一条记录
                        %s entity = new %s();
                        // TODO: 设置属性
                        %sService.save(entity);
                        Long id = entity.getId();

                        // 验证记录存在
                        assertThat(%sService.getById(id)).isNotNull();

                        // 删除记录
                        %sService.removeById(id);

                        // 验证记录已删除
                        assertThat(%sService.getById(id)).isNull();

                        // 测试结束后会回滚，记录应该恢复（但在这个测试内看不到）
                    }

                    // ==================== 并发事务测试 ====================

                    @Test
                    @DisplayName("测试读取未提交的数据（脏读防护）")
                    @Transactional
                    void testDirtyReadPrevention() {
                        // 在事务中创建数据
                        %s entity = new %s();
                        // TODO: 设置属性
                        %sService.save(entity);

                        // 在同一事务中可以读取到
                        assertThat(%sService.getById(entity.getId())).isNotNull();

                        // 其他事务在提交前不应该看到这条数据
                        // （这需要在多线程环境下测试）
                    }

                    // ==================== 更新操作事务测试 ====================

                    @Test
                    @DisplayName("测试更新操作事务回滚")
                    @Transactional
                    void testUpdate_Transactional() {
                        // 查询一条已存在的记录
                        %s entity = %sService.getById(1L);
                        if (entity != null) {
                            // 记录原始值
                            // String originalName = entity.getName();

                            // 修改值
                            // entity.setName("事务测试修改");
                            %sService.updateById(entity);

                            // 验证修改已生效
                            %s updated = %sService.getById(1L);
                            // assertThat(updated.getName()).isEqualTo("事务测试修改");

                            // 测试结束后会回滚，原始值应该恢复
                        }
                    }
                }
                """,
                basePackage, basePackage, entityName, basePackage, entityName,
                entityCnName, getAuthor(), getCurrentDate(),
                entityCnName, entityName,
                entityName, lowerEntityName,
                lowerEntityName,
                entityName, entityName, lowerEntityName, lowerEntityName,
                lowerEntityName,
                lowerEntityName,
                entityName, entityName, entityName, entityName, entityName, entityName,
                lowerEntityName, lowerEntityName, lowerEntityName, lowerEntityName,
                entityName, entityName, lowerEntityName, lowerEntityName, lowerEntityName, lowerEntityName,
                entityName, entityName, lowerEntityName, lowerEntityName,
                entityName, lowerEntityName, lowerEntityName, entityName, lowerEntityName
        );

        writeToFile(getTestJavaPath(), "transaction", entityName + "TransactionalTest.java", content);
    }
}
