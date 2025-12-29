# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

张家界后台管理系统是一个基于 Spring Boot 3 和 Vue 3 的全栈企业级后台管理系统。采用前后端分离架构，后端使用 Maven 多模块管理。

## 技术栈

### 后端
- Spring Boot 3.3.6 (Java 17)
- MyBatis-Plus 3.5.14
- Spring Security + JWT 认证
- MySQL 数据库
- Swagger/OpenAPI 3 文档

### 前端
- Vue 3 (Composition API)
- Element Plus UI 框架
- Pinia 状态管理
- Vue Router 4
- Vite 构建工具

## 项目结构

```
java_web/
├── zhangjiajie-common/      # 公共模块 - 通用工具、注解、配置
├── zhangjiajie-system/      # 系统模块 - 核心业务逻辑
├── zhangjiajie-generator/   # 代码生成器模块
├── zhangjiajie-admin/       # 启动模块 - 应用入口
└── frontend/                # Vue 3 前端项目
```

### 模块职责

**zhangjiajie-common** - 基础设施层
- `annotation/` - 自定义注解（AuditLog 操作日志、DataScope 数据权限、RateLimit 限流）
- `core/` - 核心类（Result 统一响应、PageRequest/PageResult 分页、BaseController 基础控制器）
- `config/` - 通用配置（MyBatis-Plus、异步任务）
- `constant/` - 常量定义
- `enums/` - 枚举类型（ResultCode 响应码）
- `exception/` - 自定义异常
- `security/` - 安全相关基础类
- `service/` - 公共服务
- `util/` - 工具类（JWT、文件上传、加密等）

**zhangjiajie-system** - 业务逻辑层
- `controller/` - REST API 控制器
- `service/` - 业务服务实现
- `mapper/` - MyBatis 数据访问层
- `entity/` - 实体类
- `dto/` - 数据传输对象
- `converter/` - 对象转换器
- `aspect/` - AOP 切面（审计日志、数据权限）
- `security/` - Spring Security 配置和过滤器
- `event/listener/` - 事件驱动组件

**zhangjiajie-generator** - 代码生成器
- 基于 MyBatis-Plus Generator 和 Freemarker 模板
- 支持从数据库表自动生成 Entity、Mapper、Service、Controller

**zhangjiajie-admin** - 应用启动入口
- `ZhangjiajieAdminApplication.java` - Spring Boot 主启动类
- `application.yml` - 应用配置文件
- `mapper/*.xml` - MyBatis XML 映射文件

## 开发命令

### 后端开发

#### 构建项目
```bash
# 在项目根目录执行
mvn clean install
```

#### 运行应用
```bash
# 方式1：通过 Maven 运行
cd zhangjiajie-admin
mvn spring-boot:run

# 方式2：运行打包后的 JAR（需要先 mvn clean package）
cd zhangjiajie-admin/target
java -jar zhangjiajie-admin-1.0.0.jar
```

#### 编译单个模块
```bash
cd zhangjiajie-system
mvn clean compile
```

#### 打包生产版本
```bash
mvn clean package -DskipTests
```

#### 更新依赖
```bash
mvn clean install -U
```

### 前端开发

#### 安装依赖
```bash
cd frontend
npm install
```

#### 启动开发服务器
```bash
cd frontend
npm run dev
# 访问 http://localhost:3000
```

#### 构建生产版本
```bash
cd frontend
npm run build
```

## 配置说明

### 数据库配置
数据库配置位于 `zhangjiajie-admin/src/main/resources/application.yml`:
- 数据库: `java_web`
- 默认用户: `root`
- 默认密码: `123456`
- 端口: `3306`

### JWT 配置
JWT 配置在 `application.yml` 中:
- `jwt.secret` - JWT 签名密钥
- `jwt.expiration` - Token 过期时间（秒，默认 24 小时）
- `jwt.refresh-expiration` - 刷新 Token 过期时间（默认 7 天）

### 文件上传配置
- `file.upload.path` - 上传目录 (默认 `uploads/`)
- `file.upload.max-size` - 最大文件大小 (10MB)
- `file.upload.allowed-types` - 允许的文件类型

### 前端 API 代理
前端开发时，API 请求通过 Vite 代理到后端:
```javascript
// vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

## 架构关键点

### 认证流程
1. JWT 认证基于 Spring Security 实现
2. `JwtAuthenticationFilter` (zhangjiajie-system) 拦截所有请求，从 `Authorization: Bearer <token>` 提取 JWT
3. `JwtUtil` (zhangjiajie-common) 负责 Token 生成、验证和解析
4. `UserDetailsServiceImpl` 加载用户详情和权限信息
5. 前端通过 Pinia `authStore` 管理登录状态和 Token

### 统一响应格式
所有 API 返回统一的 `Result<T>` 结构：
```java
{
  "code": 200,        // 响应码
  "message": "成功",  // 响应消息
  "data": {...},      // 响应数据
  "timestamp": 1234567890
}
```

使用示例：
```java
return Result.success(data);           // 成功响应
return Result.error("错误信息");       // 失败响应
return Result.unauthorized();          // 未授权
```

### 分页查询
统一使用 `PageRequest` 和 `PageResult` 进行分页：
```java
// Controller 接收分页参数
public Result<PageResult<User>> getUsers(PageRequest request) {
    PageResult<User> result = userService.getUserPage(request);
    return Result.success(result);
}
```

### 数据权限控制
使用 `@DataScope` 注解实现数据权限过滤：
- 支持全部数据、本部门、本部门及子部门、仅本人等权限范围
- 通过 AOP 切面 (`DataScopeAspect`) 动态修改 SQL 添加数据权限条件

### 操作日志
使用 `@AuditLog` 注解记录用户操作：
```java
@AuditLog(operation = "新增", module = "用户管理", description = "新增用户")
public Result<Void> addUser(@RequestBody User user) {
    // ...
}
```
- `AuditLogAspect` 通过 AOP 自动记录操作日志
- 支持记录请求参数、响应数据，自动过滤敏感字段

### MyBatis-Plus 配置
- 自动填充: `createTime`, `updateTime`, `createBy`, `updateBy`
- 逻辑删除: `deleted` 字段 (0=未删除, 1=已删除)
- 驼峰转换: 数据库下划线自动转为 Java 驼峰命名
- Mapper 扫描: `com.zhangjiajie.system.mapper` 和 `com.zhangjiajie.generator.mapper`

### 前端路由守卫
- `router/index.js` 实现了路由守卫，未登录用户自动跳转到登录页
- 通过 `authStore.isAuthenticated` 检查登录状态
- Token 存储在 `localStorage` 中，页面刷新时自动恢复

### 前端状态管理
- `stores/auth.js` - 认证状态（登录、退出、用户信息）
- API 调用统一通过 `axios` 实例，自动添加 JWT Token
- 响应拦截器处理 401 未授权错误，自动跳转登录页

## 代码生成器使用

代码生成器配置在 `application.yml`:
```yaml
generator:
  base-package: com.zhangjiajie.system
  target-module: zhangjiajie-system
  author: zhangjiajie
  enable-swagger: true
  enable-lombok: true
  table-prefix: sys_
```

生成代码后需要：
1. 在对应的 Controller 添加 `@RestController` 和路由映射
2. 在前端 `src/views/` 添加对应的 Vue 页面
3. 在 `src/router/index.js` 添加路由配置
4. 在 `src/api/` 添加 API 接口定义

## 常见开发任务

### 添加新的业务模块
1. 在数据库创建表（推荐以 `sys_` 为前缀）
2. 使用代码生成器生成 Entity、Mapper、Service、Controller
3. 根据需要添加自定义业务逻辑
4. 在 Controller 方法上添加 `@AuditLog` 记录操作
5. 对于需要数据权限的查询添加 `@DataScope` 注解
6. 前端创建对应的 Vue 页面和 API 接口

### 修改认证逻辑
核心文件:
- `SecurityConfig.java` - Spring Security 配置
- `JwtAuthenticationFilter.java` - JWT 过滤器
- `JwtUtil.java` - JWT 工具类
- `AuthService.java` - 认证服务

### 修改响应格式
修改 `zhangjiajie-common/src/main/java/com/zhangjiajie/common/core/Result.java`

### 前端添加新页面
1. 在 `frontend/src/views/` 创建 `.vue` 文件
2. 在 `frontend/src/router/index.js` 注册路由
3. 在 `frontend/src/api/` 添加对应的 API 接口函数
4. 在主布局 `App.vue` 的侧边栏添加菜单项

## 启动顺序

1. 确保 MySQL 数据库已启动并创建 `java_web` 数据库
2. 启动后端服务 (端口 8080)
3. 启动前端开发服务器 (端口 3000)
4. 访问 http://localhost:3000

## API 文档

项目集成了 Swagger/OpenAPI 3，后端启动后访问:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
