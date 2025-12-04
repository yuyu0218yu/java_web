# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.8 web application using Java 21 and Maven with MyBatis-Plus for data persistence. It follows standard Spring Boot conventions and provides a foundation for secure REST API development with database integration.

## Development Commands

### Build and Run
```bash
# Build the project
./mvnw clean build

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
```

### Application Access
- Default port: 8080
- Application URL: `http://localhost:8080`
- Default Spring Security will require authentication for most endpoints

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.5.8
- **Java**: Java 21 (LTS)
- **Build**: Apache Maven (with Maven wrapper)
- **Security**: Spring Security (included but not configured)
- **Database**: MyBatis-Plus 3.5.8
- **Database Driver**: MySQL Connector (runtime), H2 (test)
- **Testing**: JUnit 5 with Spring Boot Test

### Package Structure
- Main package: `com.yushuang.demo`
- Standard Maven project structure
- Layered architecture: Controllers → Services → Mappers
- `com.yushuang.demo.entity`: Entity classes
- `com.yushuang.demo.mapper`: MyBatis-Plus mapper interfaces
- `com.yushuang.demo.service`: Business logic services
- `com.yushuang.demo.controller`: REST controllers

### Key Components
- `DemoApplication.java`: Main Spring Boot application class with `@MapperScan`
- `application.properties`: Database and MyBatis-Plus configuration
- `DemoApplicationTests.java`: JUnit 5 test infrastructure

### Database Configuration
- **Production**: MySQL database (localhost:3306/demo)
- **Testing**: H2 in-memory database
- **MyBatis-Plus Features**:
  - Automatic camel case mapping (`user_name` → `userName`)
  - Logical delete support (`deleted` field)
  - SQL logging to console
  - XML mapper location: `classpath:mapper/*.xml`

### Development Notes
- Spring Security is included but uses default configuration
- MyBatis-Plus mappers are auto-scanned in `com.yushuang.demo.mapper` package
- Database schema needs to be created manually before first run
- Entity classes should include logical delete field if using that feature
- IDE null analysis is enabled in VSCode configuration

## Project State
The codebase includes:
- Complete Spring Boot foundation with MyBatis-Plus integration
- Database connection configuration (MySQL)
- Ready architecture for layered development
- Test database setup with H2

Ready for implementation of:
- REST API controllers
- Business logic services
- MyBatis-Plus entity and mapper classes
- Custom security configuration
- Database schema and migrations