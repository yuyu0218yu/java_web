# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.8 web application using Java 21 and Maven. It follows standard Spring Boot conventions and provides a foundation for secure REST API development.

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
- **Testing**: JUnit 5 with Spring Boot Test

### Package Structure
- Main package: `com.yushuang.demo`
- Standard Maven project structure
- Ready for layered architecture: Controllers → Services → Repositories

### Key Components
- `DemoApplication.java`: Main Spring Boot application class
- `application.properties`: Spring Boot configuration (currently minimal)
- `DemoApplicationTests.java`: JUnit 5 test infrastructure

### Development Notes
- Spring Security is included but uses default configuration
- No database dependency yet - repositories can be added as needed
- Static resources and templates directories are empty and ready for web assets
- IDE null analysis is enabled in VSCode configuration

## Project State
The codebase is a clean Spring Boot foundation ready for:
- REST API controller implementation
- Business logic service layer
- Data access repository layer
- Custom security configuration
- Database integration

All infrastructure is in place for rapid development of secure web applications.