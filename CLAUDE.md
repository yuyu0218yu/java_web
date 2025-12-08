# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Full-stack enterprise admin system: Spring Boot 3.2.5 backend + Vue 3 frontend. Features JWT authentication, RBAC with data-level permissions, operation auditing, and file management.

**Multi-Module Maven Structure**:
```
zhangjiajie-admin-parent/       (Parent POM)
├── zhangjiajie-common/         (Utilities, exceptions, annotations)
├── zhangjiajie-system/         (User, Role, Menu, Permission, Auth)
├── zhangjiajie-generator/      (Code generators)
└── zhangjiajie-admin/          (Application startup, configs)
```

## Development Commands

### Backend
```bash
mvn clean compile                                    # Build
mvn spring-boot:run -pl zhangjiajie-admin           # Run from root
mvn test                                             # All tests
mvn test -Dtest=ClassName -pl zhangjiajie-admin     # Single test class
mvn test -Dtest=ClassName#methodName -pl zhangjiajie-admin  # Single method
mvn package -DskipTests                              # Package without tests
```

### Frontend
```bash
cd frontend && npm install    # Install dependencies
npm run dev                   # Dev server (http://localhost:3000)
npm run build                 # Production build
```

### Access Points
- Backend: http://localhost:8080
- Frontend: http://localhost:3000
- Swagger UI: http://localhost:8080/swagger-ui.html
- Default login: `admin` / `123456`

## Architecture

### Technology Stack
- **Backend**: Java 17, Spring Boot 3.2.5, MyBatis-Plus 3.5.14, Spring Security + JWT (JJWT 0.12.3), SpringDoc OpenAPI 3
- **Frontend**: Vue 3 (Composition API), Element Plus, Pinia, Vue Router 4, Axios, Vite
- **Database**: MySQL (prod), H2 (test), logical delete support (`deleted` field)

### Key Package Locations
- **Entities**: `com.zhangjiajie.system.entity`
- **Mappers**: `com.zhangjiajie.system.mapper` (auto-scanned)
- **Services**: `com.zhangjiajie.system.service`
- **Controllers**: `com.zhangjiajie.system.controller`
- **Security**: `com.zhangjiajie.system.security`
- **Utilities**: `com.zhangjiajie.common.util`
- **Generators**: `com.zhangjiajie.generator.config`

### Database
- **Init script**: `zhangjiajie-admin/src/main/resources/sql/init.sql`
- **Connection**: localhost:3306/java_web (root/123456)
- **MyBatis-Plus**: Auto camel case mapping, logical delete, SQL logging enabled

## API Patterns

### Response Wrapper
```java
return Result.success(data);
return Result.error("操作失败");
return Result.success(PageResult.of(records, total, current, size));
```

### CRUD via BaseController
Extend `BaseController<S, T>` to get 7 REST endpoints automatically:
```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController<ProductService, Product> {
    @Override
    protected String getPermissionPrefix() { return "product"; }
}
// Provides: GET /page, GET /{id}, GET /list, POST, PUT /{id}, DELETE /{id}, DELETE /batch
```

### Audit Logging
```java
@AuditLog(operation = "创建用户", module = "用户管理", sensitiveFields = {"password"})
@PostMapping
public Result<Void> create(@RequestBody CreateUserRequest request) { ... }
```

### Excel Export
```java
@GetMapping("/export")
public void export(HttpServletResponse response) {
    ExcelUtil.export(response, userService.list(), UserExportDTO.class, "用户列表");
}
```

## Code Generators

Located in `zhangjiajie-generator/src/main/java/com/zhangjiajie/generator/config/`:

1. **MyBatisPlusCodeGenerator** - Entity, Mapper, Service, Controller from DB tables
2. **CrudTemplateGenerator** - Standard CRUD Controller/Service/ServiceImpl
3. **UnitTestGenerator** - JUnit 5 test classes
4. **DtoConverterGenerator** - DTOs and conversion utilities

```java
// Typical workflow
MyBatisPlusCodeGenerator.generateCode("product");
DtoConverterGenerator.generate("Product", "产品");
UnitTestGenerator.generate("Product", "产品");
```

## Key Utilities

- **Result<T>** / **PageResult<T>** / **PageRequest**: API response and pagination
- **JwtUtil**: Token generation, validation, refresh
- **ValidationUtil**: 30+ validators (email, phone, ID card, password strength)
- **ExcelUtil**: EasyExcel-based import/export
- **QueryWrapperUtil**: Simplified MyBatis-Plus queries
- **GlobalExceptionHandler**: Centralized exception handling
- **MyBatisPlusConfig**: Auto-fill `createTime`, `updateTime`, `deleted`, `status`

## Security

- JWT-based stateless auth with BCrypt password hashing
- Permission format: `{resource}:{action}` (e.g., `user:view`, `role:create`)
- Super admin wildcard: `*`
- Anonymous endpoints: `/api/auth/login`, `/api/auth/refresh`

## Notes

- Chinese comments used throughout the codebase
- Async processing for operation logging via `AsyncConfig`
- File uploads go to `uploads/` directory (auto-created), SHA256 deduplication
- Mappers use XML in `classpath*:mapper/*.xml`
