package com.yushuang.demo.generator;

import java.io.IOException;

import static com.yushuang.demo.generator.GeneratorHelper.*;

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

            printSuccess("CRUD代码生成", entityName, 
                entityName + "Controller.java",
                entityName + "Service.java",
                entityName + "ServiceImpl.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
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
                    return Result.success(PageResult.of(result));
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
                        return Result.notFound("%s不存在");
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
                    return Result.success(%sService.list());
                }

                /**
                 * 创建%s
                 */
                @PostMapping
                @Operation(summary = "创建%s")
                @PreAuthorize("hasAuthority('%s:create') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> create(@Valid @RequestBody %s entity) {
                    return %sService.save(entity) ? Result.success("创建成功") : Result.error("创建失败");
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
                    return %sService.updateById(entity) ? Result.success("更新成功") : Result.error("更新失败");
                }

                /**
                 * 删除%s
                 */
                @DeleteMapping("/{id}")
                @Operation(summary = "删除%s")
                @PreAuthorize("hasAuthority('%s:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> delete(@Parameter(description = "%sID") @PathVariable Long id) {
                    return %sService.removeById(id) ? Result.success("删除成功") : Result.error("删除失败");
                }

                /**
                 * 批量删除%s
                 */
                @DeleteMapping("/batch")
                @Operation(summary = "批量删除%s")
                @PreAuthorize("hasAuthority('%s:delete') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
                public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
                    return %sService.removeByIds(ids) ? Result.success("批量删除成功") : Result.error("批量删除失败");
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDate(), urlPath, moduleName, moduleName, entityName,
            entityName, lowerEntityName, entityCnName, entityCnName, lowerEntityName,
            entityName, entityName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityName, entityCnName,
            entityName, lowerEntityName, entityCnName,
            entityCnName, entityCnName, lowerEntityName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityCnName, entityName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, entityCnName, lowerEntityName,
            entityCnName, entityCnName, lowerEntityName, lowerEntityName
        );

        writeToFile(getMainJavaPath(), "controller", entityName + "Controller.java", content);
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
            BASE_PACKAGE, BASE_PACKAGE, entityName, entityCnName, AUTHOR, getCurrentDate(), entityName, entityName
        );

        writeToFile(getMainJavaPath(), "service", entityName + "Service.java", content);
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
            entityCnName, AUTHOR, getCurrentDate(), entityName, entityName, entityName, entityName
        );

        writeToFile(getMainJavaPath(), "service/impl", entityName + "ServiceImpl.java", content);
    }
}
