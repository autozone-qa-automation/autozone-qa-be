# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=BackendApplicationTests

# Package as JAR
./mvnw clean package
```

## Stack

- **Java 17**, **Spring Boot 4.0.4**
- **Spring MVC** for REST endpoints
- **Spring Data JPA** with **MySQL** for persistence
- **Maven** (wrapper included via `mvnw`)

## Architecture

This project is an early-stage scaffold. The main package is `com.az_qa.backend` (note: Maven converts the group ID `com.az-qa` to underscores for Java package naming).

Expected layered structure as it grows:
- `controller/` — REST controllers (`@RestController`)
- `service/` — Business logic (`@Service`)
- `repository/` — JPA repositories (`@Repository`)
- `model/` or `entity/` — JPA entities

## Configuration

Database and other settings go in `src/main/resources/application.properties`. Currently only `spring.application.name=backend` is set — MySQL connection properties (`spring.datasource.*`) need to be added before JPA entities can be used.
