# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

---

## Commands

```bash
# Build
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=UserControllerTest

# Run linter (check format)
./mvnw spotless:check

# Run linter with autofix
./mvnw spotless:apply

# Package as JAR
./mvnw clean package
```

---

## Stack

- **Java 17**, **Spring Boot 4.0.4**, **Spring Framework 7.0.6**
- **Spring MVC** for REST endpoints
- **Spring Data JPA** + **Hibernate 7** for persistence
- **MySQL** (production) / **H2 in-memory** (tests)
- **JUnit 5** + **MockMvc** for controller tests
- **Spotless** + **Google Java Format** for code style
- **SpringDoc OpenAPI** (Swagger UI at `/swagger-ui/index.html`)
- **Maven** (wrapper included via `./mvnw`)

---

## Project structure

```
src/
└── main/
    └── java/
        └── com/az_qa/backend/
            ├── controller/          # HTTP layer — @RestController only, no business logic
            ├── service/             # Business logic interfaces
            │   └── impl/            # Service implementations (@Service, @Transactional)
            ├── repository/          # JPA repositories — extend JpaRepository
            ├── entity/              # JPA entities — map to DB tables, never sent to client
            ├── dto/
            │   ├── request/         # What the client sends (with @Valid constraints)
            │   └── response/        # What we send back to the client
            ├── mapper/              # Converts between Entity ↔ DTO (@Component)
            └── exception/           # Custom exceptions + GlobalExceptionHandler
```

### One class per responsibility

| File | Annotation | Purpose |
|---|---|---|
| `*Controller.java` | `@RestController` | Receives HTTP, delegates to Service |
| `*Service.java` | interface | Contract — document here with Javadoc |
| `*ServiceImpl.java` | `@Service` | Business logic implementation |
| `*Repository.java` | `@Repository` | Extends `JpaRepository<Entity, Long>` |
| `*Entity.java` | `@Entity` | DB table mapping — never expose directly |
| `*Request.java` | — | Input DTO with Jakarta validation constraints |
| `*Response.java` | — | Output DTO — controls what the client sees |
| `*UpdateRequest.java` | — | Separate DTO for PUT — all fields optional |
| `*Mapper.java` | `@Component` | `toEntity()`, `toResponse()`, `updateFromRequest()` |
| `ResourceNotFoundException.java` | — | Extends `RuntimeException` — throws 404 |
| `GlobalExceptionHandler.java` | `@ControllerAdvice` | Maps exceptions to HTTP responses |

---

## Architecture rules

### Layer flow
```
HTTP Request
    → Controller        (receives, validates with @Valid, delegates)
    → ServiceImpl       (business logic, @Transactional)
    → Repository        (DB access via JpaRepository)
    → Mapper            (Entity → Response before returning)
HTTP Response
```

### DTOs vs Entity
- **Never** return an `@Entity` directly from a controller — always convert to a `*Response` DTO via the Mapper.
- **Never** use an `@Entity` as a `@RequestBody` parameter — always use a `*Request` DTO.
- Use `*Request` for POST (all fields required with `@NotBlank`).
- Use `*UpdateRequest` for PUT (all fields optional, no `@NotBlank`, only `@Size`).

### Validation
- `@Valid` on `@RequestBody` triggers `MethodArgumentNotValidException` → handled as `400`.
- `@Positive` on `@PathVariable` requires `@Validated` on the controller class → triggers `HandlerMethodValidationException` → handled as `400`.
- **Do not use `@Validated` and `@Valid @RequestBody` together on the same controller in Spring Boot 4** — they conflict. Keep `@Validated` only if you need `@PathVariable` constraints, and be aware the request body validation still works via `@Valid`.
- Hibernate Validator must be on the classpath (`spring-boot-starter-validation`) — without it, all validation annotations are silently ignored.

### Exception handling
All exceptions are handled in `GlobalExceptionHandler`. The current handlers are:

| Exception | HTTP Status | Trigger |
|---|---|---|
| `ResourceNotFoundException` | 404 | `orElseThrow()` in ServiceImpl |
| `MethodArgumentNotValidException` | 400 | `@Valid` fails on `@RequestBody` |
| `HandlerMethodValidationException` | 400 | `@Positive` fails on `@PathVariable` (Spring Boot 4) |
| `ConstraintViolationException` | 400 | Constraint fails in Service layer |
| `MethodArgumentTypeMismatchException` | 400 | Wrong type in `@PathVariable` (e.g. "abc" for Long) |
| `HttpMessageNotReadableException` | 400 | Malformed JSON or wrong type in body |
| `HttpMediaTypeNotSupportedException` | 415 | Missing or wrong `Content-Type` header |
| `Exception` | 500 | Fallback — prevents internal details leaking |

To add a new error scenario: create a new exception class in `exception/`, throw it in `ServiceImpl`, add a handler in `GlobalExceptionHandler`.

---

## Javadoc guidelines

Document where it adds value. Do not document the obvious.

### Always document
- **Service interfaces** (`*Service.java`) — use `@param`, `@return`, `@throws` for every method.
- **`GlobalExceptionHandler`** — each handler method with `@param` and what status it returns.
- **`@Query` methods in repositories** — when the method name alone doesn't explain the query.

### Do not document
- `*ServiceImpl.java` — the interface already documents the contract.
- Getters and setters.
- Methods where the name is self-explanatory (`getUserById`, `deleteUser`).
- `ResourceNotFoundException` and similar trivial exception classes.

### Example — Service interface method
```java
/**
 * Retrieves a user by their ID.
 *
 * @param id the user's ID
 * @return the matching user
 * @throws ResourceNotFoundException if no user exists with the given ID
 */
UserResponse getUserById(long id);
```

Only add `@throws` when the exception is actually thrown by that method.

---

## Testing

Tests live in `src/test/java/com/az_qa/backend/`.

### Controller tests (`@WebMvcTest`)
- Use `@WebMvcTest(controllers = {XController.class, GlobalExceptionHandler.class})`.
- Include `GlobalExceptionHandler.class` explicitly — otherwise error handlers are not loaded and 4xx tests fail.
- Mock the service with `@MockitoBean` (not `@MockBean` — deprecated in Spring Boot 4).
- Use `@Nested` + `@DisplayName` to group tests by endpoint.
- Use `ObjectMapper` (autowired) to serialize request bodies.

### What to test per endpoint
For each endpoint, cover at minimum:
1. Happy path — correct input returns expected status and body.
2. Not found — service throws `ResourceNotFoundException` → 404.
3. Invalid input — blank/short field → 400.
4. Type mismatch — non-numeric id → 400.

---

## Jackson configuration

```properties
# Reject unknown fields in request bodies
spring.jackson.deserialization.fail-on-unknown-properties=true
```

This causes `HttpMessageNotReadableException` when the client sends fields not declared in the DTO.

---

## Known Spring Boot 4 specifics

- `@MockBean` is deprecated — use `@MockitoBean` from `org.springframework.test.context.bean.override.mockito`.
- `HandlerMethodValidationException` replaces `ConstraintViolationException` for `@PathVariable` validation in Spring MVC controllers.
- `MethodValidationException` is an internal base class — do not use it as an `@ExceptionHandler` target in MVC.