# 后端开发需求清单

为了让前端Vue应用正常运行，后端Spring Boot应用需要实现以下功能：

## 🔐 认证接口 (必需)

### 1. 登录接口
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

**响应格式:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "phone": "13800138000"
    },
    "permissions": ["user:view", "user:create", "role:view", "*"]
  }
}
```

### 2. 获取用户信息接口
```http
GET /api/auth/userinfo
Authorization: Bearer {token}
```

### 3. 刷新Token接口
```http
POST /api/auth/refresh
Authorization: Bearer {token}
```

## 🔧 Spring Security配置

### 需要允许匿名访问的接口:
- `/api/auth/login` - 登录接口
- `/api/auth/refresh` - 刷新Token接口

### 需要认证的接口:
- 所有其他 `/api/**` 接口

### 配置示例:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

## 📝 JWT工具类

需要实现JWT生成、解析和验证功能:

```java
@Component
public class JwtUtil {
    
    public String generateToken(UserDetails userDetails) {
        // 生成JWT token
    }
    
    public String getUsernameFromToken(String token) {
        // 从token中获取用户名
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        // 验证token有效性
    }
}
```

## 🎯 建议的Controller结构

### AuthController
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // 登录逻辑
    }
    
    @GetMapping("/userinfo")
    public Result<UserInfo> getUserInfo() {
        // 获取当前用户信息
    }
    
    @PostMapping("/refresh")
    public Result<String> refreshToken() {
        // 刷新token
    }
}
```

## 📋 响应格式统一

所有API接口应使用统一的响应格式:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
```

## 🚀 实现优先级

### 高优先级 (必需)
1. ✅ 创建 `AuthController` 和登录接口
2. ✅ 配置Spring Security允许登录接口匿名访问
3. ✅ 实现JWT工具类
4. ✅ 添加JWT认证过滤器

### 中优先级 (建议)
1. 🔄 完善用户管理接口的分页响应格式
2. 🔄 添加角色和权限管理接口
3. 🔄 实现密码加密验证

### 低优先级 (可选)
1. 📝 添加接口文档 (Swagger)
2. 📝 实现日志记录
3. 📝 添加异常处理

## 🔍 测试验证

实现完成后，可以通过以下方式测试:

1. **登录测试**: 调用 `/api/auth/login` 获取token
2. **接口测试**: 使用返回的token调用其他API接口
3. **前端测试**: 启动Vue应用，尝试登录和访问各个页面

## 📞 注意事项

1. **CORS配置**: 确保允许 `http://localhost:3000` 跨域访问
2. **密码加密**: 使用BCrypt加密用户密码
3. **Token过期**: 设置合理的JWT过期时间
4. **错误处理**: 统一异常处理和错误响应格式

---

**实现完成后，前端Vue应用将能够正常登录并使用所有管理功能！**
