package com.yushuang.demo.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CRUD模板代码生成器
 * 生成标准的Controller、Service、ServiceImpl代码
 *
 * 使用方法：
 * CrudTemplateGenerator.generate("User", "用户", "用户管理");
 *
 * @author yushuang
 */
public class CrudTemplateGenerator {

    private static final String BASE_PACKAGE = "com.yushuang.demo";
    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/java/com/yushuang/demo";
    private static final String AUTHOR = "yushuang";

    public static void main(String[] args) {
        // 示例：生成Product实体的CRUD代码
        generate("Product", "产品", "产品管理");
    }

    /**
     * 生成CRUD代码
     *
     * @param entityName 实体类名（如：User, Product）
     * @param entityCnName 实体中文名（如：用户、产品）
     * @param moduleName 模块名称（如：用户管理、产品管理）
     */
    public static void generate(String entityName, String entityCnName, String moduleName) {
        try {
            generateController(entityName, entityCnName, moduleName);
            generateService(entityName, entityCnName);
            generateServiceImpl(entityName, entityCnName);

            System.out.println("========================================");
            System.out.println("CRUD代码生成完成！");
            System.out.println("实体: " + entityName);
            System.out.println("生成文件:");
            System.out.println("  - " + entityName + "Controller.java");
            System.out.println("  - " + entityName + "Service.java");
            System.out.println("  - " + entityName + "ServiceImpl.java");
            System.out.println("========================================");
        } catch (IOException e) {
            System.err.println("生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成Controller
     */
    private static void generateController(String entityName, String entityCnName, String moduleName) throws IOException {
        String lowerEntityName = toLowerCamelCase(entityName);
        String urlPath = toKebabCase(entityName);
        String content = String.format("""
            package %s.controller;

            import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
            import com.baomidou.mybatisplus.core.metadata.IPage;
            import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
            import %s.common.PageResult;
            import %s.common.Result;
            import %s.entity.%s;
            import %s.service.%sService;
            import io.swagger.v3.oas.annotations.Operation;
            import io.swagger.v3.oas.annotations.Parameter;
            import io.swagger.v3.oas.annotations.tags.Tag;
            import lombok.RequiredArgsConstructor;
            import org.springframework.security.access.prepost.PreAuthorize;
            import org.springframework.validation.annotation.Validated;
            import org.springframework.web.bind.annotation.*;

            import jakarta.validation.Valid;
            import jakarta.validation.constraints.Max;
            import jakarta.validation.constraints.Min;
            import java.util.List;

            /**
             * %s控制器
             *
             * @author %s
             * @since %s
             */
            @RestController
            @RequestMapping("/api/%s")
            @RequiredArgsConstructor
            @Validated
            @Tag(name = "%s", description = "%s相关接口")
            public class %sController {

                private final %sService %sService;

                /**
                 * 分页查询%s列表
                 */
                @GetMapping("/page")
                @Operation(summary = "分页查询%s列表")
                @PreAuthorize("hasAuthority('%s:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<PageResult<%s>> getPage(
                        @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer current,
                        @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {

                    Page<%s> page = new Page<>(current, size);
                    IPage<%s> result = %sService.page(page);

                    PageResult<%s> pageResult = PageResult.of(
                            result.getRecords(),
                            result.getTotal(),
                            result.getCurrent(),
                            result.getSize()
                    );

                    return Result.success(pageResult);
                }

                /**
                 * 根据ID查询%s
                 */
                @GetMapping("/{id}")
                @Operation(summary = "根据ID查询%s")
                @PreAuthorize("hasAuthority('%s:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<%s> getById(@Parameter(description = "%sID") @PathVariable Long id) {
                    %s entity = %sService.getById(id);
                    if (entity == null) {
                        return Result.error("%s不存在");
                    }
                    return Result.success(entity);
                }

                /**
                 * 查询所有%s
                 */
                @GetMapping("/list")
                @Operation(summary = "查询所有%s")
                @PreAuthorize("hasAuthority('%s:view') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<List<%s>> list() {
                    List<%s> list = %sService.list();
                    return Result.success(list);
                }

                /**
                 * 创建%s
                 */
                @PostMapping
                @Operation(summary = "创建%s")
                @PreAuthorize("hasAuthority('%s:create') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> create(@Valid @RequestBody %s entity) {
                    boolean success = %sService.save(entity);
                    if (success) {
                        return Result.success("创建成功");
                    }
                    return Result.error("创建失败");
                }

                /**
                 * 更新%s
                 */
                @PutMapping("/{id}")
                @Operation(summary = "更新%s")
                @PreAuthorize("hasAuthority('%s:update') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> update(
                        @Parameter(description = "%sID") @PathVariable Long id,
                        @Valid @RequestBody %s entity) {
                    entity.setId(id);
                    boolean success = %sService.updateById(entity);
                    if (success) {
                        return Result.success("更新成功");
                    }
                    return Result.error("更新失败");
                }

                /**
                 * 删除%s
                 */
                @DeleteMapping("/{id}")
                @Operation(summary = "删除%s")
                @PreAuthorize("hasAuthority('%s:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> delete(@Parameter(description = "%sID") @PathVariable Long id) {
                    boolean success = %sService.removeById(id);
                    if (success) {
                        return Result.success("删除成功");
                    }
                    return Result.error("删除失败");
                }

                /**
                 * 批量删除%s
                 */
                @DeleteMapping("/batch")
                @Operation(summary = "批量删除%s")
                @PreAuthorize("hasAuthority('%s:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
                    boolean success = %sService.removeByIds(ids);
                    if (success) {
                        return Result.success("批量删除成功");
                    }
                    return Result.error("批量删除失败");
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDateTime(), urlPath, moduleName, moduleName, entityName,
            entityName, lowerEntityName, entityCnName, entityCnName, lowerEntityName,
            entityName, entityName, entityName, lowerEntityName, entityName,
            entityCnName, entityCnName, lowerEntityName, entityName, entityCnName,
            entityName, lowerEntityName, entityCnName,
            entityCnName, entityCnName, lowerEntityName, entityName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityCnName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityCnName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, lowerEntityName
        );

        writeToFile("controller", entityName + "Controller.java", content);
    }

    /**
     * 生成Service接口
     */
    private static void generateService(String entityName, String entityCnName) throws IOException {
        String content = String.format("""
            package %s.service;

            import com.baomidou.mybatisplus.extension.service.IService;
            import %s.entity.%s;

            /**
             * %s服务接口
             *
             * @author %s
             * @since %s
             */
            public interface %sService extends IService<%s> {

                // 在此添加自定义业务方法

            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, entityCnName, AUTHOR, getCurrentDateTime(), entityName, entityName
        );

        writeToFile("service", entityName + "Service.java", content);
    }

    /**
     * 生成ServiceImpl实现类
     */
    private static void generateServiceImpl(String entityName, String entityCnName) throws IOException {
        String content = String.format("""
            package %s.service.impl;

            import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
            import %s.entity.%s;
            import %s.mapper.%sMapper;
            import %s.service.%sService;
            import lombok.RequiredArgsConstructor;
            import org.springframework.stereotype.Service;

            /**
             * %s服务实现类
             *
             * @author %s
             * @since %s
             */
            @Service
            @RequiredArgsConstructor
            public class %sServiceImpl extends ServiceImpl<%sMapper, %s> implements %sService {

                // 在此实现自定义业务方法

            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDateTime(), entityName, entityName, entityName, entityName
        );

        writeToFile("service/impl", entityName + "ServiceImpl.java", content);
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
