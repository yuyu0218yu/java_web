# 用户权限管理系统

一个基于 Spring Boot 3.2.5 + Vue 3 构建的现代化用户权限管理全栈应用。

## 🏗️ 系统架构

```
┌─────────────────┐    HTTP/REST API    ┌─────────────────┐
│   Vue 3 前端    │ ◄─────────────────► │ Spring Boot 后端 │
│                 │                     │                 │
│ • Element Plus  │                     │ • MyBatis-Plus  │
│ • Pinia         │                     │ • Spring Security│
│ • Vue Router    │                     │ • JWT 认证      │
│ • Axios         │                     │ • MySQL 数据库  │
└─────────────────┘                     └─────────────────┘
```

## 🚀 技术栈

### 前端技术

- **框架**: Vue 3 (Composition API)
- **UI库**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **构建工具**: Vite

### 后端技术

- **框架**: Spring Boot 3.2.5
- **持久层**: MyBatis-Plus 3.5.14
- **安全框架**: Spring Security + JWT
- **数据库**: MySQL
- **构建工具**: Maven

## 📁 项目结构

```text
demo/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/yushuang/demo/
│   │       ├── controller/     # REST API 控制器
│   │       ├── entity/         # 实体类
│   │       ├── mapper/         # MyBatis Mapper
│   │       ├── service/        # 业务逻辑层
│   │       └── util/           # 工具类
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── api/               # API 接口定义
│   │   ├── components/        # 公共组件
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # Pinia 状态管理
│   │   ├── utils/             # 工具函数
│   │   └── views/             # 页面组件
│   ├── package.json
│   └── vite.config.js
├── BACKEND_TODO.md            # 后端开发需求
└── README.md                  # 项目说明
```

## 🎯 核心功能

### 🔐 认证与授权

- [x] 前端登录页面和认证流程
- [x] JWT Token 管理
- [x] 路由权限控制
- [ ] 后端认证接口实现

### 👥 用户管理

- [x] 用户列表（分页、搜索、筛选）
- [x] 新增/编辑/删除用户
- [x] 批量操作
- [x] 密码重置
- [x] 用户状态管理

### 🎭 角色管理

- [x] 角色列表管理
- [x] 角色权限配置
- [x] 角色状态控制

### 🔑 权限管理

- [x] 树形权限结构
- [x] 菜单/按钮/接口权限
- [x] 权限层级管理

### 📊 仪表盘

- [x] 系统统计概览
- [x] 用户增长趋势
- [x] 权限分布图表
- [x] 最近活动记录

## 🚀 快速开始

### 环境要求
- JDK 21+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 1. 启动后端服务

```bash
cd backend
mvnw clean compile
mvnw spring-boot:run
```

后端服务将在 <http://localhost:8080> 启动

### 2. 启动前端服务

```bash
cd frontend
npm install
npm run dev
```

前端应用将在 <http://localhost:3000> 启动

### 3. 访问应用

打开浏览器访问 <http://localhost:3000>

## ⚠️ 重要提示

### 当前状态
- ✅ **前端**: 完全实现，可独立运行
- ✅ **后端**: 基础架构完成，服务正常启动
- ❌ **认证**: 后端缺少认证接口，前端无法登录

### 必需的后端实现
在能够正常使用系统之前，后端需要实现以下功能：

1. **认证接口** (`/api/auth/login`)
2. **JWT 工具类** (生成和验证Token)
3. **Spring Security 配置** (允许登录接口匿名访问)

详细实现指南请查看：[BACKEND_TODO.md](./BACKEND_TODO.md)

## 📋 开发指南

### 前端开发

```bash
cd frontend
npm run dev          # 开发模式
npm run build        # 生产构建
```

### 后端开发

```bash
cd backend
mvnw clean compile   # 编译项目
mvnw spring-boot:run # 启动应用
mvnw test           # 运行测试
```

### API 文档

启动后端服务后，访问：<http://localhost:8080/swagger-ui.html>

## 🎨 界面预览

### 登录页面
- 现代化登录界面
- 支持记住登录状态
- 响应式设计

### 管理后台
- 侧边栏导航
- 面包屑导航
- 用户信息下拉菜单
- 响应式布局

### 功能页面
- 数据表格（分页、排序、筛选）
- 表单对话框
- 批量操作
- 状态管理

## 🔧 配置说明

### 数据库配置

```properties
# backend/src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo
spring.datasource.username=root
spring.datasource.password=your_password
```

### 前端代理配置

```javascript
// frontend/vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## 🛠️ 开发工具

### 推荐IDE

- **后端**: IntelliJ IDEA
- **前端**: VS Code

### 推荐插件

- Vue Language Features (Volar)
- Element Plus Snippets
- Spring Boot Helper

## 📞 技术支持

### 常见问题

1. **前端无法访问后端API**: 检查后端服务是否启动，端口是否正确
2. **登录失败**: 确认后端认证接口已实现
3. **数据库连接失败**: 检查数据库配置和MySQL服务状态

### 调试指南

- 前端：浏览器开发者工具
- 后端：Spring Boot 日志和IDE调试器
- 网络：Postman 测试API接口

## 📄 许可证

MIT License

---

**🎉 恭喜！您现在拥有一个完整的用户权限管理系统基础架构。**

**下一步：** 请根据 [BACKEND_TODO.md](./BACKEND_TODO.md) 完成后端认证接口实现，即可开始使用完整的系统功能！
