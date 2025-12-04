# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.2.5 web application using Java 21 and Maven with MyBatis-Plus for data persistence. It follows standard Spring Boot conventions and provides a comprehensive foundation for secure REST API development with database integration and a rich utility library. The project includes user management functionality with roles, permissions, and password security features.

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
- **Security**: Spring Security (included but not configured)
- **Database**: MyBatis-Plus 3.5.14 (Spring Boot 3 compatible)
- **Database Driver**: MySQL Connector (runtime), H2 (test)
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Testing**: JUnit 5 with Spring Boot Test
- **Code Generation**: Lombok for reduced boilerplate

### Package Structure
- Main package: `com.yushuang.demo`
- Standard Maven project structure
- Layered architecture: Controllers → Services → Mappers
- `com.yushuang.demo.common`: Common response and pagination components
- `com.yushuang.demo.util`: Comprehensive utility library
- `com.yushuang.demo.entity`: Entity classes (User, Role, Permission, UserRole, RolePermission)
- `com.yushuang.demo.mapper`: MyBatis-Plus mapper interfaces
- `com.yushuang.demo.service`: Business logic services
- `com.yushuang.demo.controller`: REST controllers

### Key Components
- `DemoApplication.java`: Main Spring Boot application class with `@MapperScan("com.yushuang.demo.mapper")`
- `application.properties`: Database and MyBatis-Plus configuration
- `DemoApplicationTests.java`: JUnit 5 test infrastructure
- `Result.java`: Standard API response wrapper with HTTP status codes
- `PageResult.java`: Pagination utility with navigation helpers
- User management system with RBAC (Role-Based Access Control) structure

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

### Development Notes
- Spring Security is included but uses default configuration
- MyBatis-Plus mappers are auto-scanned in `com.yushuang.demo.mapper` package
- Database schema needs to be created manually before first run
- Entity classes should include logical delete field if using that feature
- Lombok is configured for code generation
- IDE null analysis is enabled in VSCode configuration
- Chinese comments used throughout the codebase

## Common Patterns

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

### Password Security
```java
// Password encryption
String encryptedPassword = PasswordUtil.encryptPassword("password");

// Password verification
boolean isValid = PasswordUtil.verifyPassword("password", encryptedPassword);

// Random password generation
String randomPassword = PasswordUtil.generateRandomPassword(12);
```

### Database Queries
```java
// Build queries with utility
QueryWrapper<User> wrapper = QueryWrapperUtil.create()
    .eq("status", 1)
    .like("name", keyword)
    .orderByDesc("create_time");
```

## Utility Library

### Common Components
- **Result<T>**: Standard API response wrapper with HTTP status codes
- **PageResult<T>**: Pagination utility with navigation helpers

### Utility Classes
- **QueryWrapperUtil**: MyBatis-Plus query builder with simplified API
- **WebUtil**: HTTP request/response utilities, parameter handling
- **PasswordUtil**: Password hashing, salt generation, strength validation
- **IpUtil**: IP address utilities, validation, network calculations
- **JsonUtil**: JSON serialization/deserialization with Jackson
- **DateUtil**: Comprehensive date/time operations and formatting
- **StringUtil**: String manipulation, validation, format conversion

## Project State
The codebase includes:
- Complete Spring Boot foundation with MyBatis-Plus integration
- Database connection configuration (MySQL java_web)
- Comprehensive utility library (9 major utility classes)
- User management entities and RBAC structure (User, Role, Permission, UserRole, RolePermission)
- Standard API response patterns established with Result wrapper
- Code quality standards with Lombok integration
- Test database setup with H2
- Swagger/OpenAPI 3 documentation ready

Ready for implementation of:
- Service layer business logic for user management
- REST API controllers using Result wrapper
- Custom security configuration
- Database schema creation and migrations
- Role-based access control implementation
- API endpoint development with proper validation