package ${basePackage}.controller;

import com.zhangjiajie.common.core.BaseController;
import ${basePackage}.entity.${className};
import ${basePackage}.service.${className}Service;
<#if enableSwagger>
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>
import org.springframework.web.bind.annotation.*;

/**
 * ${className} 控制器
 * 继承BaseController，自动拥有CRUD接口
 *
 * @author ${author}
 * @date ${date}
 */
@RestController
@RequestMapping("/api/${kebabCaseName}")
<#if enableSwagger>
@Tag(name = "${className}管理")
</#if>
public class ${className}Controller extends BaseController<${className}Service, ${className}> {

    @Override
    protected String getPermissionPrefix() {
        return "${lowerCamelCaseName}";
    }

    // 自动继承以下接口：
    // GET  /api/${kebabCaseName}/page - 分页查询
    // GET  /api/${kebabCaseName}/{id} - ID查询
    // GET  /api/${kebabCaseName}/list - 列表查询
    // POST /api/${kebabCaseName} - 创建
    // PUT  /api/${kebabCaseName}/{id} - 更新
    // DELETE /api/${kebabCaseName}/{id} - 删除
    // DELETE /api/${kebabCaseName}/batch - 批量删除
}
