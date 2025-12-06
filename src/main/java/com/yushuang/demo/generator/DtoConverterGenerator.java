package com.yushuang.demo.generator;

import java.io.IOException;

import static com.yushuang.demo.generator.GeneratorHelper.*;

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

            printSuccess("DTO转换器代码生成", entityName,
                entityName + "Converter.java",
                "Create" + entityName + "Request.java",
                "Update" + entityName + "Request.java",
                entityName + "Response.java");
        } catch (IOException e) {
            printError(e.getMessage(), e);
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

            import java.beans.BeanInfo;
            import java.beans.Introspector;
            import java.beans.PropertyDescriptor;
            import java.lang.reflect.Method;
            import java.util.ArrayList;
            import java.util.Arrays;
            import java.util.List;
            import java.util.stream.Collectors;

            /**
             * %s转换器
             * Entity与DTO之间的转换工具类
             *
             * @author %s
             * @since %s
             */
            public final class %sConverter {

                private %sConverter() {
                }

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
                    BeanUtils.copyProperties(request, entity, getNullPropertyNames(request));
                }

                /**
                 * 获取对象中值为null的属性名
                 */
                private static String[] getNullPropertyNames(Object source) {
                    try {
                        BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
                        return Arrays.stream(beanInfo.getPropertyDescriptors())
                                .filter(pd -> {
                                    Method readMethod = pd.getReadMethod();
                                    if (readMethod == null) return false;
                                    try {
                                        return readMethod.invoke(source) == null;
                                    } catch (Exception e) {
                                        return false;
                                    }
                                })
                                .map(PropertyDescriptor::getName)
                                .toArray(String[]::new);
                    } catch (Exception e) {
                        return new String[0];
                    }
                }
            }
            """,
            BASE_PACKAGE, BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            BASE_PACKAGE, entityName, BASE_PACKAGE, entityName,
            entityCnName, AUTHOR, getCurrentDate(), entityName, entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName, entityName,
            entityName, entityName, entityName,
            entityName, entityName
        );

        writeToFile(getMainJavaPath(), "converter", entityName + "Converter.java", content);
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

            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDate(), entityCnName, entityName
        );
        writeToFile(getMainJavaPath(), "dto", "Create" + entityName + "Request.java", createContent);

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

            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDate(), entityCnName, entityName,
            entityCnName, entityCnName
        );
        writeToFile(getMainJavaPath(), "dto", "Update" + entityName + "Request.java", updateContent);
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

                @Schema(description = "创建时间")
                private LocalDateTime createTime;

                @Schema(description = "更新时间")
                private LocalDateTime updateTime;
            }
            """,
            BASE_PACKAGE, entityCnName, AUTHOR, getCurrentDate(), entityCnName, entityName, entityCnName
        );

        writeToFile(getMainJavaPath(), "dto", entityName + "Response.java", content);
    }
}
