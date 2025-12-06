<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackage}.mapper.${className}Mapper">

    <!-- 通用结果映射 -->
    <resultMap id="BaseResultMap" type="${basePackage}.entity.${className}">
<#list columns as column>
        <#if column.isPrimaryKey>
        <id column="${column.columnName}" property="${column.fieldName}" />
        <#else>
        <result column="${column.columnName}" property="${column.fieldName}" />
        </#if>
</#list>
    </resultMap>

    <!-- 通用列列表 -->
    <sql id="Base_Column_List">
        ${columnList}
    </sql>

    <!-- 自定义查询示例（可根据业务扩展） -->
    <!--
    <select id="selectByCondition" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List" />
        FROM ${tableName}
        WHERE deleted = 0
        <if test="keyword != null and keyword != ''">
            AND name LIKE CONCAT('%', ${r"#{keyword}"}, '%')
        </if>
        ORDER BY create_time DESC
    </select>
    -->

</mapper>
