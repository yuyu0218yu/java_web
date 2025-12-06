# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack user management system with Spring Boot 3.2.5 backend and Vue 3 frontend. The backend uses Java 17 and Maven with MyBatis-Plus for data persistence. It features a complete JWT-based authentication system, operation logging/auditing functionality, file management module, and user management with RBAC (Role-Based Access Control). The project demonstrates enterprise-grade architecture with security, logging, and file handling capabilities.

**Multi-Module Maven Architecture**: The project is organized as a multi-module Maven project for better separation of concerns and future microservices migration.

## Development Commands

### Backend (Spring Boot)
```bash
# Build the project
mvn clean compile

# Run the application (from zhangjiajie-admin module)
cd zhangjiajie-admin && mvn spring-boot:run
# Or from root directory:
mvn spring-boot:run -pl zhangjiajie-admin

# Run tests
mvn test

# Package the application
mvn package

# Skip tests during build
mvn package -DskipTests

# Run a single test class
mvn test -Dtest=ZhangjiajieAdminApplicationTests -pl zhangjiajie-admin

# Run a single test method
mvn test -Dtest=ZhangjiajieAdminApplicationTests#contextLoads -pl zhangjiajie-admin
```

### Frontend (Vue 3)
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Run development server (http://localhost:3000)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Application Access
- **Backend**: http://localhost:8080
- **Frontend**: http://localhost:3000
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## Architecture

### Multi-Module Structure

```
zhangjiajie-admin-parent/       (Parent POM)
├── zhangjiajie-common/         (Common utilities, exceptions, annotations)
├── zhangjiajie-system/         (System management: User, Role, Menu, Permission, Auth)
├── zhangjiajie-generator/      (Code generator module)
└── zhangjiajie-admin/          (Application startup module)
```

### Technology Stack

#### Backend
- **Framework**: Spring Boot 3.2.5
- **Java**: Java 17
- **Build**: Apache Maven (Multi-Module)
- **Security**: Spring Security with JWT authentication
- **Database**: MyBatis-Plus 3.5.14 (Spring Boot 3 compatible)
- **Database Driver**: MySQL Connector (runtime), H2 (test)
- **JWT**: io.jsonwebtoken (JJWT) 0.12.3 for token handling
- **AOP**: Spring AOP for logging and cross-cutting concerns
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger 2.3.0)
- **Testing**: JUnit 5 with Spring Boot Test
- **Code Generation**: Lombok for reduced boilerplate

#### Frontend
- **Framework**: Vue 3.3.11 (Composition API)
- **UI Library**: Element Plus 2.4.4
- **Routing**: Vue Router 4.2.5
- **State Management**: Pinia 2.1.7
- **HTTP Client**: Axios 1.6.2
- **Build Tool**: Vite 5.0.8
- **Icons**: @element-plus/icons-vue 2.3.1

### Package Structure

#### zhangjiajie-common Module
- `com.zhangjiajie.common.core`: Core classes (Result, PageResult, PageRequest, BaseController)
- `com.zhangjiajie.common.enums`: Enum classes (CodeEnum, ResultCode, EnableStatus, ResultStatus)
- `com.zhangjiajie.common.exception`: Exception classes (BusinessException, GlobalExceptionHandler, ResourceNotFoundException)
- `com.zhangjiajie.common.annotation`: Custom annotations (@AuditLog)
- `com.zhangjiajie.common.util`: Utility classes (JwtUtil, IpUtil, ExcelUtil, ValidationUtil, etc.)
- `com.zhangjiajie.common.config`: Common configuration (MyBatisPlusConfig, AsyncConfig)

#### zhangjiajie-system Module
- `com.zhangjiajie.system.entity`: Entity classes (User, Role, Menu, Permission, FileInfo, OperationLog, etc.)
- `com.zhangjiajie.system.mapper`: MyBatis-Plus mapper interfaces
- `com.zhangjiajie.system.service`: Business logic services
- `com.zhangjiajie.system.controller`: REST controllers (UserController, RoleController, AuthController, etc.)
- `com.zhangjiajie.system.dto`: Data transfer objects
- `com.zhangjiajie.system.security`: JWT authentication components
- `com.zhangjiajie.system.config`: System-specific configuration (SecurityConfig, FileUploadConfig)
- `com.zhangjiajie.system.aspect`: AOP aspects (AuditLogAspect)
- `com.zhangjiajie.system.event`: Application events
- `com.zhangjiajie.system.listener`: Event listeners

#### zhangjiajie-generator Module
- `com.zhangjiajie.generator.controller`: Generator REST controllers
- `com.zhangjiajie.generator.service`: Generator service
- `com.zhangjiajie.generator.config`: Generator configuration and utilities
- `com.zhangjiajie.generator.dto`: Generator DTOs

#### zhangjiajie-admin Module
- `com.zhangjiajie.ZhangjiajieAdminApplication`: Main startup class
- Application configuration (application.yml)
- SQL initialization scripts

#### Frontend Structure
- `frontend/src/api/`: API service layer with Axios requests
- `frontend/src/components/`: Reusable Vue components
- `frontend/src/router/`: Vue Router configuration with auth guards
- `frontend/src/stores/`: Pinia stores for state management
- `frontend/src/views/`: Page components (Login, Dashboard, User Management, etc.)
- `frontend/src/utils/`: Frontend utilities (auth helpers, formatters)
- `frontend/vite.config.js`: Vite configuration with proxy settings for backend API

### Key Components
- `ZhangjiajieAdminApplication.java`: Main Spring Boot application class with `@MapperScan("com.zhangjiajie.system.mapper")`
- `application.yml`: Database, JWT, and file upload configuration
- `Result.java`: Standard API response wrapper with HTTP status codes
- `PageResult.java`: Pagination utility with navigation helpers
- `@AuditLog`: Custom annotation for operation logging
- `JwtUtil`: JWT token generation and validation utility
- `AuditLogAspect`: AOP aspect for automatic operation logging
- Complete JWT authentication system with role-based access control
- Operation logging and auditing system with async processing
- File management system with upload/download capabilities

### Database Configuration
- **Production**: MySQL database (localhost:3306/java_web)
  - Username: root
  - Password: 123456
  - Encoding: UTF-8MB4 with Asia/Shanghai timezone
  - No SSL configuration
- **Testing**: H2 in-memory database
- **Initialization Script**: `zhangjiajie-admin/src/main/resources/sql/init.sql` contains complete database schema (users, roles, permissions, logs, files)
- **MyBatis-Plus Features**:
  - Automatic camel case mapping (`user_name` → `userName`)
  - Logical delete support (`deleted` field: 1=deleted, 0=active)
  - SQL logging to console
  - Entity scanning: `com.zhangjiajie.system.mapper` package
  - XML mapper location: `classpath*:mapper/*.xml`

### API Documentation
- Swagger/OpenAPI 3 available via SpringDoc
- Default UI URL: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

### Authentication & Security
- **JWT Authentication**: Complete JWT-based auth system with login/logout/token refresh
- **Default Admin User**: Username: `admin`, Password: `123456`
- **Security Configuration**: `/api/auth/login` and `/api/auth/refresh` allow anonymous access
- **CORS Support**: Configured for cross-origin requests
- **Password Encryption**: BCrypt hashing for secure password storage

### Development Notes
- Spring Security with JWT integration for API authentication
- MyBatis-Plus mappers are auto-scanned in `com.zhangjiajie.system.mapper` package
- Database schema includes init scripts for RBAC and logging tables
- Entity classes include logical delete field for soft deletes
- Lombok is configured for code generation
- Async processing for logging to avoid performance impact
- File upload directory `uploads/` is auto-created on startup
- Chinese comments used throughout the codebase

## Core Features & Patterns

### JWT Authentication
```java
// Login request/response
POST /api/auth/login
{
  "username": "admin",
  "password": "123456"
}

// Response includes JWT token and user info
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": { "username": "admin", "email": "admin@example.com" },
    "permissions": ["user:view", "role:view", "*"]
  }
}
```

### Operation Logging with @AuditLog
```java
@AuditLog(operation = "用户登录", module = "认证管理", saveRequestData = false)
@PostMapping("/login")
public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    // Automatic logging handled by AOP aspect
}
```

### File Management
```java
// File upload
POST /api/files/upload
Content-Type: multipart/form-data
// Single or batch file upload with security validation

// File download
GET /api/files/{id}/download
// Returns file with proper headers and increments download count
```

### API Response Format
```java
// Success response
return Result.success(data);
return Result.success("操作成功", data);

// Error response
return Result.error("操作失败");
return Result.error(400, "参数错误");

// Pagination response
return Result.success(PageResult.of(records, total, current, size));
```

## Utility Library

### Core Utilities
- **Result<T>**: Standard API response wrapper with HTTP status codes
- **PageResult<T>**: Pagination utility with navigation helpers
- **PageRequest**: Common pagination request parameter with sort support
- **JwtUtil**: JWT token generation, validation, and refresh functionality
- **UserAgentUtil**: Browser and operating system parsing from user agents

### Extended Utilities (11 utility classes total)
- **QueryWrapperUtil**: MyBatis-Plus query builder with simplified API
- **WebUtil**: HTTP request/response utilities, parameter handling
- **IpUtil**: IP address utilities, validation, network calculations
- **JsonUtil**: JSON serialization/deserialization with Jackson
- **DateUtil**: Comprehensive date/time operations and formatting
- **StringUtil**: String manipulation, validation, format conversion
- **FileSizeUtil**: File size formatting and conversion utilities
- **ValidationUtil**: Common validation methods (email, phone, ID card, password strength, etc.)
- **ExcelUtil**: Excel import/export based on EasyExcel

## Enterprise Features

### Security Architecture
- JWT-based stateless authentication
- Role-based access control (RBAC)
- CORS configuration for cross-origin requests
- Password encryption with BCrypt
- Sensitive data masking in logs

### Auditing & Logging
- Automatic operation logging with @AuditLog annotation
- Login success/failure tracking with IP and browser info
- Async logging to prevent performance impact
- Sensitive field masking (passwords, tokens)
- Comprehensive log statistics and reporting

### File Management
- Secure file upload with type and size validation
- File deduplication using SHA256 hashing
- Download tracking and statistics
- File preview and download endpoints
- Configurable storage paths and allowed file types

## Project State
The codebase includes a complete enterprise-grade foundation:

### Backend (Fully Implemented)
- ✅ JWT authentication system with login/register/refresh endpoints
- ✅ Spring Security configuration with role-based access control
- ✅ Operation logging and auditing with @AuditLog annotation and AOP
- ✅ File management system with upload/download/security controls
- ✅ User management with RBAC structure (User, Role, Permission entities)
- ✅ Database schemas with init scripts for all features
- ✅ Comprehensive utility library (9 utility classes)
- ✅ Standard API response patterns with Result<T> wrapper
- ✅ Swagger/OpenAPI 3 documentation
- ✅ Async configuration for background processing
- ✅ CORS and security configuration

### Frontend (Fully Implemented)
- ✅ Vue 3 with Composition API
- ✅ Element Plus UI components
- ✅ Pinia state management
- ✅ Vue Router with navigation guards
- ✅ Axios HTTP client with interceptors
- ✅ Login/Register pages
- ✅ Dashboard with statistics
- ✅ User/Role/Permission management pages
- ✅ Dark mode theme support

### Getting Started
1. **Initialize Database**: Run `src/main/resources/sql/init.sql` on MySQL
2. **Start Backend**: `mvn spring-boot:run`
3. **Start Frontend**: `cd frontend && npm install && npm run dev`
4. **Login**: Use default admin credentials (username: `admin`, password: `123456`)

## Common Features (13 Enterprise-Grade Utilities)

The project includes 13 pre-built common features that accelerate development:

### 1. Unified Response Wrapper - `Result<T>`
Standard API response format with code, message, data, and timestamp.
```java
return Result.success(data);
return Result.error("操作失败");
return Result.unauthorized("未登录");
```

### 2. Global Exception Handler - `GlobalExceptionHandler`
Centralized exception handling for business exceptions, validation errors, authentication failures, etc.

### 3. Pagination Utilities
- **PageResult<T>**: Pagination result wrapper with navigation helpers
- **PageRequest**: Common pagination request parameters with sorting support
```java
@GetMapping("/page")
public Result<PageResult<User>> page(@Valid PageRequest request) {
    Page<User> page = request.toMpPageWithSort();
    IPage<User> result = userService.page(page);
    return Result.success(PageResult.of(result.getRecords(), result.getTotal(), ...));
}
```

### 4. Base CRUD Controller - `BaseController<S, T>`
Abstract base class providing standard CRUD endpoints. Inherit to get 7 REST endpoints automatically:
```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController<ProductService, Product> {
    @Override
    protected String getPermissionPrefix() {
        return "product";
    }
    // GET /api/products/page - Pagination query
    // GET /api/products/{id} - Get by ID
    // GET /api/products/list - Get all
    // POST /api/products - Create
    // PUT /api/products/{id} - Update
    // DELETE /api/products/{id} - Delete
    // DELETE /api/products/batch - Batch delete
}
```

### 5. Operation Logging - `@AuditLog` + `AuditLogAspect`
Automatic async operation logging with sensitive data masking:
```java
@AuditLog(operation = "创建用户", module = "用户管理",
          saveRequestData = true, sensitiveFields = {"password"})
@PostMapping
public Result<Void> create(@RequestBody CreateUserRequest request) {
    // Logging handled automatically
}
```

### 6. Permission Control - Spring Security `@PreAuthorize`
Expression-based permission control integrated throughout controllers.

### 7. Data Validation
- JSR-303 validation with `@Valid`/`@Validated`
- Custom `ValidationUtil` with 30+ validation methods (email, phone, ID card, password strength, etc.)

### 8. Auto-fill Audit Fields - `MyBatisPlusConfig`
Automatic population of `createTime`, `updateTime`, `deleted`, `status` fields on insert/update.

### 9. Excel Import/Export - `ExcelUtil`
Based on EasyExcel, supporting single/multi-sheet export and import:
```java
@GetMapping("/export")
public void export(HttpServletResponse response) {
    List<User> users = userService.list();
    ExcelUtil.export(response, users, UserExportDTO.class, "用户列表");
}
```

### 10-13. Additional Utilities
- **MyBatis-Plus BaseMapper**: Zero-SQL CRUD operations
- **Custom Business Exceptions**: `BusinessException`, `ResourceNotFoundException`
- **JWT Utilities**: Token generation, validation, refresh in `JwtUtil`
- **File Management**: Secure upload/download with deduplication

## Code Generators (4 Automation Tools)

The project includes 4 code generators in `zhangjiajie-generator/src/main/java/com/zhangjiajie/generator/config/`:

### 1. MyBatisPlusCodeGenerator
Generates complete Entity, Mapper, Service, Controller from database tables.
```java
// Generate code for single table
MyBatisPlusCodeGenerator.generateCode("product");

// Generate code for multiple tables
MyBatisPlusCodeGenerator.generateCode("user", "role", "permission");

// Generate by table prefix
MyBatisPlusCodeGenerator.generateByPrefix("sys_");
```

**Configuration**: Update database connection settings in the class before running.

### 2. CrudTemplateGenerator
Generates standard CRUD Controller, Service, ServiceImpl with complete REST endpoints.
```java
CrudTemplateGenerator.generate("Product", "产品", "产品管理");
```

**Generated files**:
- `ProductController.java` - REST controller with 7 endpoints
- `ProductService.java` - Service interface
- `ProductServiceImpl.java` - Service implementation

### 3. UnitTestGenerator
Generates JUnit 5 test classes for Controller and Service.
```java
UnitTestGenerator.generate("User", "用户");
```

**Generated files**:
- `UserControllerTest.java` - MockMvc tests for all endpoints
- `UserServiceTest.java` - Service layer unit tests

### 4. DtoConverterGenerator
Generates DTO classes and converter utilities.
```java
DtoConverterGenerator.generate("User", "用户");
```

**Generated files**:
- `UserConverter.java` - Conversion utilities between Entity and DTOs
- `CreateUserRequest.java` - Create request DTO
- `UpdateUserRequest.java` - Update request DTO
- `UserResponse.java` - Response DTO

### Code Generator Workflow
Typical workflow for creating a new module:
```java
// 1. Generate base code from database table
MyBatisPlusCodeGenerator.generateCode("product");

// 2. Generate DTOs and converters
DtoConverterGenerator.generate("Product", "产品");

// 3. Generate unit tests
UnitTestGenerator.generate("Product", "产品");

// 4. Manually customize business logic in Service layer
```

**Efficiency gain**: Reduces CRUD module development time by 80% (from 3-4 hours to 10 minutes).

## Development Patterns

### Creating a New CRUD Module
Use BaseController for rapid development:
```java
// 1. Entity with auto-fill annotations
@Data
@TableName("product")
public class Product {
    private Long id;
    private String name;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

// 2. Mapper (no methods needed)
@Mapper
public interface ProductMapper extends BaseMapper<Product> {}

// 3. Service interface
public interface ProductService extends IService<Product> {}

// 4. Service implementation
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService {}

// 5. Controller (inherits all CRUD endpoints)
@RestController
@RequestMapping("/api/products")
@Tag(name = "产品管理")
public class ProductController extends BaseController<ProductService, Product> {
    @Override
    protected String getPermissionPrefix() {
        return "product";
    }
}
```

### Using Code Generators
When starting from a database table, use generators to bootstrap the module:
```bash
# Run generator main methods directly or create a utility script
java -cp target/classes com.zhangjiajie.generator.MyBatisPlusCodeGenerator
```

### Adding Custom Business Logic
Extend generated Service implementations with custom methods:
```java
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService {

    @Override
    public List<Product> findByCategory(String category) {
        return this.list(new LambdaQueryWrapper<Product>()
            .eq(Product::getCategory, category));
    }
}
```

### Excel Export Example
Add export endpoint to any controller:
```java
@GetMapping("/export")
@Operation(summary = "导出产品列表")
public void export(HttpServletResponse response) {
    List<Product> products = productService.list();
    ExcelUtil.export(response, products, ProductExportDTO.class, "产品列表");
}
```

## Important Notes

### Database Initialization
The `init.sql` script must be run before first startup. It creates:
- User, Role, Permission tables with relationships
- Operation logging and login logging tables
- File management tables
- Sample data including admin user

### Security Configuration
- JWT tokens expire after configured time (check `application.properties`)
- All endpoints except `/api/auth/**` require authentication
- Permission checks use format: `{resource}:{action}` (e.g., `user:view`, `role:create`)
- Super admin has wildcard permission `*`

### Async Processing
Operation logging uses async processing via `AsyncConfig`. Ensure thread pool is properly configured for production use.

### File Upload
- Default upload directory: `uploads/` (auto-created)
- File types and size limits configured in `FileUploadConfig`
- Files are deduplicated using SHA256 hashing