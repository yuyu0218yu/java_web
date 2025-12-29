# 张家界旅游系统 - 启动指南

## 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        后端服务                              │
│                   localhost:8080                            │
│         (zhangjiajie-admin + portal + system)               │
└─────────────────────────────────────────────────────────────┘
                    ▲                    ▲
                    │                    │
         ┌─────────┴────────┐  ┌────────┴─────────┐
         │   管理后台前端    │  │    用户端前端     │
         │  localhost:3000  │  │  localhost:3001  │
         │    (frontend)    │  │ (frontend-portal)│
         └──────────────────┘  └──────────────────┘
```

## 启动顺序

### 1. 启动数据库

确保 MySQL 已启动，数据库配置：
- 数据库名：`java_web`
- 用户名：`root`
- 密码：`123456`
- 端口：`3306`

### 2. 启动后端服务

```bash
# 方式一：IDE 启动
# 运行 ZhangjiajieAdminApplication.java

# 方式二：Maven 命令
cd C:\Users\Administrator\Desktop\DD\java_web\zhangjiajie-admin
mvn spring-boot:run

# 方式三：JAR 包启动（需要先打包）
cd C:\Users\Administrator\Desktop\DD\java_web
mvn clean package -DskipTests
java -jar zhangjiajie-admin/target/zhangjiajie-admin-1.0.0.jar
```

启动成功标志：
```
Started ZhangjiajieAdminApplication in x.xxx seconds
```

### 3. 启动管理后台前端

```bash
cd C:\Users\Administrator\Desktop\DD\java_web\frontend
npm install    # 首次启动需要
npm run dev
```

访问地址：**http://localhost:3000**

### 4. 启动用户端前端

```bash
cd C:\Users\Administrator\Desktop\DD\java_web\frontend-portal
npm install    # 首次启动需要
npm run dev
```

访问地址：**http://localhost:3001**

## 快速启动（Windows 批处理）

可以创建 `start-all.bat` 一键启动：

```bat
@echo off
echo 启动张家界旅游系统...

:: 启动后端
start "后端服务" cmd /k "cd /d C:\Users\Administrator\Desktop\DD\java_web\zhangjiajie-admin && mvn spring-boot:run"

:: 等待后端启动
timeout /t 30

:: 启动管理后台前端
start "管理后台" cmd /k "cd /d C:\Users\Administrator\Desktop\DD\java_web\frontend && npm run dev"

:: 启动用户端前端
start "用户端" cmd /k "cd /d C:\Users\Administrator\Desktop\DD\java_web\frontend-portal && npm run dev"

echo 启动完成！
echo 管理后台: http://localhost:3000
echo 用户端:   http://localhost:3001
pause
```

## 访问地址汇总

| 服务 | 地址 | 说明 |
|------|------|------|
| 后端 API | http://localhost:8080 | REST API 服务 |
| Swagger 文档 | http://localhost:8080/swagger-ui.html | API 文档 |
| 管理后台 | http://localhost:3000 | 管理员登录 |
| 用户端 | http://localhost:3001 | 游客/用户访问 |

## 默认账号

### 管理后台
- 用户名：`admin`
- 密码：`123456`

### 用户端
- 需要注册新账号，或使用已有用户账号登录

## 常见问题

### Q: 后端启动失败，提示数据库连接错误
检查 MySQL 是否启动，数据库 `java_web` 是否存在。

### Q: 前端启动失败，提示依赖错误
```bash
# 删除 node_modules 重新安装
rm -rf node_modules
npm install
```

### Q: 前端页面空白或接口报错
确保后端已启动并监听 8080 端口。

### Q: 端口被占用
```bash
# Windows 查看端口占用
netstat -ano | findstr :8080
netstat -ano | findstr :3000
netstat -ano | findstr :3001

# 结束占用进程
taskkill /PID <进程ID> /F
```

## 开发模式 vs 生产模式

| 环境 | 后端 | 前端 |
|------|------|------|
| 开发 | `mvn spring-boot:run` | `npm run dev` |
| 生产 | `java -jar xxx.jar` | `npm run build` 后部署静态文件 |
