# 张家界后台管理系统 - 前端

基于 Vue 3 + Element Plus 构建的现代化后台管理前端系统。

## 🚀 技术栈

- **框架**: Vue 3 (Composition API)
- **UI库**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **开发语言**: JavaScript

## 📁 项目结构

```
frontend/
├── src/
│   ├── api/           # API接口定义
│   ├── components/    # 公共组件
│   ├── router/        # 路由配置
│   ├── stores/        # Pinia状态管理
│   ├── utils/         # 工具函数
│   ├── views/         # 页面组件
│   ├── App.vue        # 根组件
│   └── main.js        # 应用入口
├── index.html         # HTML模板
├── package.json       # 依赖配置
└── vite.config.js     # Vite配置
```

## 🛠️ 快速开始

### 环境要求
- Node.js >= 16.0.0
- npm >= 7.0.0

### 安装依赖
```bash
cd frontend
npm install
```

### 启动开发服务器
```bash
npm run dev
```

应用将在 http://localhost:3000 启动

### 构建生产版本
```bash
npm run build
```

## 📋 功能特性

### 🔐 认证系统
- 用户登录/退出
- JWT Token管理
- 路由权限控制
- 自动Token刷新

### 👥 用户管理
- 用户列表查询（分页、搜索、筛选）
- 新增/编辑/删除用户
- 批量操作
- 密码重置
- 用户状态管理

### 🎭 角色管理
- 角色列表管理
- 角色权限配置
- 角色状态控制

### 🔑 权限管理
- 树形权限结构
- 菜单/按钮/接口权限
- 权限层级管理
- 批量权限操作

### 📊 仪表盘
- 系统统计概览
- 用户增长趋势
- 权限分布图表
- 最近活动记录

## 🔧 配置说明

### API代理配置
在 `vite.config.js` 中配置了API代理：
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

### 环境变量
如需配置不同环境，可创建 `.env.development` 和 `.env.production` 文件：
```
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=张家界后台管理系统
```

## 📝 开发指南

### 添加新页面
1. 在 `src/views/` 创建页面组件
2. 在 `src/router/index.js` 添加路由配置
3. 在 `src/App.vue` 侧边栏添加导航项

### API接口调用
```javascript
import { userApi } from '@/api'

// 获取用户列表
const response = await userApi.getUserPage(params)
```

### 状态管理
```javascript
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
await authStore.login(credentials)
```

## 🎨 UI组件

项目使用 Element Plus 组件库，主要组件：
- `el-table` - 数据表格
- `el-form` - 表单
- `el-dialog` - 对话框
- `el-tree` - 树形控件
- `el-pagination` - 分页

## 🚨 注意事项

1. **后端依赖**: 前端需要后端提供认证接口才能正常运行
2. **CORS配置**: 确保后端允许前端域名的跨域请求
3. **JWT Token**: 后端需要实现JWT生成和验证逻辑
4. **权限控制**: 前端权限控制基于后端返回的权限列表

## 📞 技术支持

如有问题，请检查：
1. 后端服务是否正常运行
2. API接口是否可访问
3. 网络连接是否正常
4. 浏览器控制台错误信息
