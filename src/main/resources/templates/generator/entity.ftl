package ${basePackage}.entity;

import com.baomidou.mybatisplus.annotation.*;
<#if enableLombok>
import lombok.Data;
</#if>
<#if enableSwagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * ${className} 实体类
 * 对应表：${tableName}
 *
 * @author ${author}
 * @date ${date}
 */
<#if enableLombok>
@Data
</#if>
@TableName("${tableName}")
<#if enableSwagger>
@Schema(description = "${className}实体")
</#if>
public class ${className} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list columns as column>
    <#if column.comment?? && column.comment != "">
    /** ${column.comment} */
    </#if>
    <#if enableSwagger && column.comment??>
    @Schema(description = "${column.comment}")
    </#if>
    <#if column.isPrimaryKey>
    @TableId(<#if column.isAutoIncrement>type = IdType.AUTO<#else>type = IdType.ASSIGN_ID</#if>)
    </#if>
    <#if column.fieldName == "deleted">
    @TableLogic
    </#if>
    <#if column.fieldName == "createTime">
    @TableField(fill = FieldFill.INSERT)
    </#if>
    <#if column.fieldName == "updateTime">
    @TableField(fill = FieldFill.INSERT_UPDATE)
    </#if>
    private ${column.javaType} ${column.fieldName};

</#list>
}
