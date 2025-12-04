# 项目扩展功能 TODO 清单

## 功能1：操作日志/审计功能

### 数据库设计

- [ ] 创建操作日志表 `sys_operation_log`

```sql
CREATE TABLE sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(100) COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT COMMENT '执行时长(毫秒)',
    ip VARCHAR(50) COMMENT '操作IP',
    user_agent TEXT COMMENT '用户代理',
    status TINYINT COMMENT '操作状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

- [ ] 创建登录日志表 `sys_login_log`

```sql
CREATE TABLE sys_login_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) COMMENT '登录用户名',
    ip VARCHAR(50) COMMENT '登录IP',
    user_agent TEXT COMMENT '用户代理',
    status TINYINT COMMENT '登录状态：0-失败，1-成功',
    error_msg VARCHAR(255) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 后端实现

- [ ] 创建日志实体类 `OperationLog.java` 和 `LoginLog.java`
- [ ] 创建日志Mapper接口 `OperationLogMapper.java` 和 `LoginLogMapper.java`
- [ ] 创建自定义注解 `@AuditLog`

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    String operation() default "";
    String module() default "";
}
```

- [ ] 实现AOP切面 `AuditLogAspect.java`
  - 使用 `@Around` 注解拦截方法调用
  - 异步记录日志信息
  - 处理异常情况
- [ ] 创建登录成功/失败事件监听器

---

## 功能2：文件上传/下载模块

### 数据库设计

- [ ] 创建文件信息表 `sys_file_info`

```sql
CREATE TABLE sys_file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) COMMENT '文件名',
    original_name VARCHAR(255) COMMENT '原始文件名',
    file_path VARCHAR(500) COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小(字节)',
    file_type VARCHAR(50) COMMENT '文件类型',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    upload_user_id BIGINT COMMENT '上传用户ID',
    upload_username VARCHAR(50) COMMENT '上传用户名',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 后端实现

- [ ] 创建文件实体类 `FileInfo.java`
- [ ] 创建文件Mapper接口 `FileInfoMapper.java`
- [ ] 实现文件服务 `FileService.java`
  - 文件上传处理
  - 文件下载处理
  - 文件删除（物理删除+逻辑删除）
  - 文件列表查询
  - 文件权限验证
- [ ] 创建文件控制器 `FileController.java`
  - `POST /api/files/upload` - 文件上传接口
  - `GET /api/files/{id}/download` - 文件下载接口
  - `GET /api/files/{id}/preview` - 文件预览接口
  - `DELETE /api/files/{id}` - 文件删除接口
  - `GET /api/files/list` - 文件列表接口
- [ ] 配置文件存储路径

```properties
# 文件上传配置
file.upload.path=uploads/
file.upload.max-size=10MB
file.upload.allowed-types=jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx
```

- [ ] 实现文件安全检查
  - 文件类型验证
  - 文件大小限制
  - 病毒扫描（可选）
  - 路径遍历攻击防护

### 🎨 前端实现
- [ ] 创建文件管理页面 `src/views/Files/FileManagement.vue`
  - 文件上传组件（拖拽上传）
  - 文件列表展示（表格形式）
  - 文件预览功能
  - 文件下载按钮
  - 文件删除确认
  - 文件搜索和筛选
- [ ] 创建文件上传组件 `src/components/FileUpload.vue`
  - 支持拖拽上传
  - 上传进度显示
  - 文件类型验证
  - 批量上传支持
- [ ] 创建文件预览组件 `src/components/FilePreview.vue`
  - 图片预览
  - PDF预览
  - 文档预览（可选）
- [ ] 添加文件管理路由配置
- [ ] 在侧边栏菜单添加"文件管理"

---

## 🎯 开发优先级和时间安排

### 第一阶段：操作日志功能（预计3-4天）

1. **Day 1**: 数据库表设计 + 实体类创建
2. **Day 2**: AOP切面实现 + 日志服务开发
3. **Day 3**: 控制器接口 + 前端页面开发
4. **Day 4**: 功能测试 + 优化调整

### 第二阶段：文件管理功能（预计4-5天）

1. **Day 1**: 数据库表设计 + 文件服务基础架构
2. **Day 2**: 文件上传下载接口实现
3. **Day 3**: 前端文件管理页面开发
4. **Day 4**: 文件预览和安全功能
5. **Day 5**: 功能测试 + 性能优化

---

## 🔧 技术要点和注意事项

### 操作日志功能
- [ ] 使用Spring AOP实现无侵入式日志记录
- [ ] 异步处理避免影响主业务性能
- [ ] 敏感信息脱敏处理（密码、密钥等）
- [ ] 日志数据定期清理策略

### 文件管理功能
- [ ] 文件存储路径安全配置
- [ ] 大文件分片上传支持
- [ ] 文件访问权限控制
- [ ] 存储空间监控和预警

### 通用要求
- [ ] 所有新功能集成到现有权限系统
- [ ] 遵循RESTful API设计规范
- [ ] 添加完整的异常处理和日志记录
- [ ] 前端界面保持与现有风格一致
- [ ] 添加单元测试和集成测试

---

## 📋 测试计划

### 功能测试
- [ ] 操作日志记录准确性测试
- [ ] 文件上传下载功能测试
- [ ] 权限控制有效性测试
- [ ] 异常情况处理测试

### 性能测试
- [ ] 大量日志数据查询性能测试
- [ ] 大文件上传下载性能测试
- [ ] 并发访问压力测试

### 安全测试
- [ ] 文件上传安全漏洞测试
- [ ] SQL注入和XSS攻击测试
- [ ] 权限绕过测试

---

**🎯 目标：完成这两个功能后，系统将具备企业级应用的核心特性，非常适合作为学生项目展示！**
