package com.yushuang.demo.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO转换器生成器
 * 生成Entity与DTO之间的转换工具类
 *
 * 使用方法：
 * DtoConverterGenerator.generate("User", "用户");
 *
 * @author yushuang
 */
public class DtoConverterGenerator {

    private static final String BASE_PACKAGE = "com.yushuang.demo";
    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/java/com/yushuang/demo";
    private static final String AUTHOR = "yushuang";

    public static void main(String[] args) {
        // 示例：生成User实体的DTO转换器
        generate("User", "用户");
    }

    /**
     * 生成DTO转换器代码
     *
     * @param entityName 实体类名（如：User, Product）
     * @param entityCnName 实体中文名（如：用户、产品）
     */
    public static void generate(String entityName, String entityCnName) {
        try {
            generateConverter(entityName, entityCnName);
            generateRequestDTO(entityName, entityCnName);
            generateResponseDTO(entityName, entityCnName);

            System.out.println("========================================");
            System.out.println("DTO转换器代码生成完成！");
            System.out.println("实体: " + entityName);
            System.out.println("生成文件:");
            System.out.println("  - " + entityName + "Converter.java");
            System.out.println("  - Create" + entityName + "Request.java");
            System.out.println("  - Update" + entityName + "Request.java");
            System.out.println("  - " + entityName + "Response.java");
            System.out.println("========================================");
        } catch (IOException e) {
            System.err.println("生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成转换器工具类
     */
    private static void generateConverter(String entityName, String entityCnName) throws IOException {
        String content = String.format("""
            package %s.converter;

            import %s.dto.Create%sRequest;
            import %s.dto.Update%sRequest;
            import %s.dto.%sResponse;
            import %s.entity.%s;
            import org.springframework.beans.BeanUtils;

            import java.util.ArrayList;
            import java.util.List;
            import java.util.stream.Collectors;

            /**
             * %s转换器
             * Entity与DTO之间的转换工具类
             *
             * @author %s
             * @since %s
             */
            public class %sConverter {

                /**
                 * CreateRequest转Entity
                 */
                public static %s toEntity(Create%sRequest request) {
                    if (request == null) {
                        return null;
                    }
                    %s entity = new %s();
                    BeanUtils.copyProperties(request, entity);
                    return entity;
                }

                /**
                 * UpdateRequest转Entity
                 */
                public static %s toEntity(Update%sRequest request) {
                    if (request == null) {
                        return null;
                    }
                    %s entity = new %s();
                    BeanUtils.copyProperties(request, entity);
                    return entity;
                }

                /**
                 * Entity转Response
                 */
                public static %sResponse toResponse(%s entity) {
                    if (entity == null) {
                        return null;
                    }
                    %sResponse response = new %sResponse();
                    BeanUtils.copyProperties(entity, response);
                    return response;
                }

                /**
                 * Entity列表转Response列表
                 */
                public static List<%sResponse> toResponseList(List<%s> entities) {
                    if (entities == null) {
                        return new ArrayList<>();
                    }
                    return entities.stream()
                            .map(%sConverter::toResponse)
                            .collect(Collectors.toList());
                }

                /**
                 * 更新Entity（将UpdateRequest的非空字段复制到Entity）
                 */
                public static void updateEntity(%s entity, Update%sRequest request) {
                    if (entity == null || request == null) {
                        return;
                    }
                    // 使用BeanUtils只复制非空属性
                    BeanUtils.copyProperties(request, entity, getNullPropertyNames(request));
                }

                /**
                 * 获取对象中值为null的属性名
                 */
                private static String[] getNullPropertyNames(Object source) {
                    final java.beans.BeanInfo beanInfo;
                    try {
                        beanInfo = java.beans.Introspector.getBeanInfo(source.getClass());
                    } catch (java.beans.IntrospectionException e) {
                        return new String[0];
                    }

                    return java.util.Arrays.stream(beanInfo.getPropertyDescriptors())
                            .filter(pd -> {
                                try {
                                    java.lang.reflect.Method readMethod = pd.getReadMethod();
                                    return readMethod != null && readMethod.invoke(source) == null;
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .map(java.beans.PropertyDescriptor::getName)
                            .toArray(String[]::new);
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDateTime(), entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName,
            entityName, entityName
        );

        writeToFile("converter", entityName + "Converter.java", content);
    }

    /**
     * 生成CreateRequest DTO
     */
    private static void generateRequestDTO(String entityName, String entityCnName) throws IOException {
        // Create Request
        String createContent = String.format("""
            package %s.dto;

            import io.swagger.v3.oas.annotations.media.Schema;
            import lombok.Data;

            import jakarta.validation.constraints.NotBlank;
            import jakarta.validation.constraints.NotNull;
            import java.io.Serializable;

            /**
             * 创建%s请求DTO
             *
             * @author %s
             * @since %s
             */
            @Data
            @Schema(description = "创建%s请求")
            public class Create%sRequest implements Serializable {

                private static final long serialVersionUID = 1L;

                // TODO: 根据实际业务需求添加字段
                // 示例字段：
                // @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
                // @NotBlank(message = "名称不能为空")
                // private String name;

            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDateTime(), entityCnName, entityName
        );
        writeToFile("dto", "Create" + entityName + "Request.java", createContent);

        // Update Request
        String updateContent = String.format("""
            package %s.dto;

            import io.swagger.v3.oas.annotations.media.Schema;
            import lombok.Data;

            import jakarta.validation.constraints.NotNull;
            import java.io.Serializable;

            /**
             * 更新%s请求DTO
             *
             * @author %s
             * @since %s
             */
            @Data
            @Schema(description = "更新%s请求")
            public class Update%sRequest implements Serializable {

                private static final long serialVersionUID = 1L;

                @Schema(description = "%sID", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "%sID不能为空")
                private Long id;

                // TODO: 根据实际业务需求添加字段
                // 示例字段：
                // @Schema(description = "名称")
                // private String name;

            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDateTime(), entityCnName, entityName,
            entityCnName, entityCnName
        );
        writeToFile("dto", "Update" + entityName + "Request.java", updateContent);
    }

    /**
     * 生成Response DTO
     */
    private static void generateResponseDTO(String entityName, String entityCnName) throws IOException {
        String content = String.format("""
            package %s.dto;

            import io.swagger.v3.oas.annotations.media.Schema;
            import lombok.Data;

            import java.io.Serializable;
            import java.time.LocalDateTime;

            /**
             * %s响应DTO
             *
             * @author %s
             * @since %s
             */
            @Data
            @Schema(description = "%s响应")
            public class %sResponse implements Serializable {

                private static final long serialVersionUID = 1L;

                @Schema(description = "%sID")
                private Long id;

                // TODO: 根据实际业务需求添加字段
                // 示例字段：
                // @Schema(description = "名称")
                // private String name;

                @Schema(description = "创建时间")
                private LocalDateTime createTime;

                @Schema(description = "更新时间")
                private LocalDateTime updateTime;
            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDateTime(), entityCnName, entityName, entityCnName
        );

        writeToFile("dto", entityName + "Response.java", content);
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
     * 获取当前日期时间
     */
    private static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
