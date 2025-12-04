# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a comprehensive Spring Boot 3.2.5 enterprise application using Java 21 and Maven with MyBatis-Plus for data persistence. It features a complete JWT-based authentication system, operation logging/auditing functionality, file management module, and user management with RBAC (Role-Based Access Control). The project demonstrates enterprise-grade architecture with security, logging, and file handling capabilities.

## Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Clean build artifacts
./mvnw clean

# Package the application
./mvnw package

# Skip tests during build
./mvnw package -DskipTests

# Run a single test class
./mvnw test -Dtest=DemoApplicationTests

# Run a single test method
./mvnw test -Dtest=DemoApplicationTests#contextLoads
```

### Application Access
- Default port: 8080
- Application URL: `http://localhost:8080`
- Default Spring Security will require authentication for most endpoints

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.2.5
- **Java**: Java 21 (LTS)
- **Build**: Apache Maven (with Maven wrapper)
- **Security**: Spring Security with JWT authentication
- **Database**: MyBatis-Plus 3.5.14 (Spring Boot 3 compatible)
- **Database Driver**: MySQL Connector (runtime), H2 (test)
- **JWT**: io.jsonwebtoken (JJWT) 0.12.3 for token handling
- **AOP**: Spring AOP for logging and cross-cutting concerns
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Testing**: JUnit 5 with Spring Boot Test
- **Code Generation**: Lombok for reduced boilerplate

### Package Structure
- Main package: `com.yushuang.demo`
- Standard Maven project structure
- Layered architecture: Controllers → Services → Mappers
- `com.yushuang.demo.common`: Common response and pagination components
- `com.yushuang.demo.util`: Comprehensive utility library
- `com.yushuang.demo.entity`: Entity classes (User, Role, Permission, UserRole, RolePermission, OperationLog, LoginLog, FileInfo)
- `com.yushuang.demo.mapper`: MyBatis-Plus mapper interfaces
- `com.yushuang.demo.service`: Business logic services
- `com.yushuang.demo.controller`: REST controllers
- `com.yushuang.demo.dto`: Data transfer objects (LoginRequest, LoginResponse, UserInfo)
- `com.yushuang.demo.security`: JWT authentication and security components
- `com.yushuang.demo.annotation`: Custom annotations (@AuditLog)
- `com.yushuang.demo.aspect`: AOP aspects for logging
- `com.yushuang.demo.event`: Application events (LoginEvent)
- `com.yushuang.demo.listener`: Event listeners for logging
- `com.yushuang.demo.config`: Configuration classes

### Key Components
- `DemoApplication.java`: Main Spring Boot application class with `@MapperScan("com.yushuang.demo.mapper")`
- `application.properties`: Database, JWT, and file upload configuration
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
- **MyBatis-Plus Features**:
  - Automatic camel case mapping (`user_name` → `userName`)
  - Logical delete support (`deleted` field: 1=deleted, 0=active)
  - SQL logging to console
  - Entity scanning: `com.yushuang.demo.entity` package
  - XML mapper location: `classpath:mapper/*.xml`

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
- MyBatis-Plus mappers are auto-scanned in `com.yushuang.demo.mapper` package
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
- **JwtUtil**: JWT token generation, validation, and refresh functionality
- **UserAgentUtil**: Browser and operating system parsing from user agents

### Extended Utilities
- **QueryWrapperUtil**: MyBatis-Plus query builder with simplified API
- **WebUtil**: HTTP request/response utilities, parameter handling
- **PasswordUtil**: Password hashing, salt generation, strength validation
- **IpUtil**: IP address utilities, validation, network calculations
- **JsonUtil**: JSON serialization/deserialization with Jackson
- **DateUtil**: Comprehensive date/time operations and formatting
- **StringUtil**: String manipulation, validation, format conversion

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
- ✅ JWT authentication system with role-based permissions
- ✅ Operation logging and auditing with async processing
- ✅ File management system with security controls
- ✅ User management with RBAC structure (User, Role, Permission entities)
- ✅ Database schemas for authentication, logging, and file management
- ✅ Comprehensive utility library (12+ utility classes)
- ✅ Standard API response patterns with Result wrapper
- ✅ Swagger/OpenAPI 3 documentation
- ✅ Async configuration for background processing
- ✅ CORS and security configuration