# AGENTS.md

This file provides guidance to AI agents and coding assistants when working with code in this repository.

## Project Overview

**yadwy-service** is a Kotlin-based Spring Boot 4.0 microservice for a handmade marketplace platform. The project follows Domain-Driven Design (DDD) principles and uses Spring Modulith for modular monolith architecture.

**Tech Stack:**
- Kotlin 2.2.21 with Java 24 toolchain
- Spring Boot 4.0.0
- Spring Modulith 2.0.0
- Spring Data JDBC (not JPA)
- PostgreSQL with Flyway migrations
- Spring Security with OAuth2 JWT Resource Server
- OpenAPI 3.0 with code generation

## Common Commands

### Build and Run
```bash
# Build the project
./gradlew build

# Run the application (requires PostgreSQL)
./gradlew bootRun

# Start PostgreSQL via Docker Compose
docker compose up -d

# Run with local profile (connects to Docker Compose PostgreSQL on port 5434)
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Testing
```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests 'yadwy.app.yadwyservice.ApplicationTests'
```

### Code Generation
```bash
# Generate OpenAPI models and API interfaces
./gradlew openApiGenerate

# Note: compileKotlin depends on openApiGenerate, so it runs automatically during build
```

### Database
```bash
# Flyway migrations run automatically on application startup
# Migration files: src/main/resources/db/migration/V*.sql
```

## Architecture

### Spring Modulith Structure
The codebase is organized into bounded contexts (modules) following Spring Modulith conventions:

```
src/main/kotlin/yadwy/app/yadwyservice/
├── identity/          # Authentication & authorization module
├── customer/          # Customer domain module (placeholder)
├── seller/            # Seller domain module (placeholder)
└── sharedkernel/      # Shared domain primitives
```

Each module is a top-level package under `yadwyservice` and represents a bounded context. Spring Modulith enforces module boundaries and enables event-driven communication between modules.

### Module Internal Structure (DDD Layering)
Each module follows hexagonal/clean architecture with DDD tactical patterns:

```
<module>/
├── api/                      # API Controllers (implements generated OpenAPI interfaces)
├── application/              # Application layer
│   ├── models/              # DTOs for use cases
│   └── usecases/            # Use case implementations (extends UseCase<T, R>)
├── domain/                   # Domain layer (pure business logic)
│   ├── contracts/           # Repository and service interfaces
│   ├── events/              # Domain events
│   ├── exceptions/          # Domain-specific exceptions
│   └── models/              # Aggregates, entities, value objects
└── infrastructure/           # Infrastructure layer
    ├── database/            # DAO and DBO classes
    ├── repositories/        # Repository implementations
    ├── security/            # Security configuration
    └── service/             # Infrastructure service implementations
```

### Key Design Patterns

**Domain Models (in `sharedkernel/domain/models/base/`):**
- `DomainModel`: Marker interface for all domain types
- `ValueObject`: Immutable objects identified by properties (use `@JvmInline value class` for single-property VOs)
- `Entity<T>`: Objects with identity, equality based on ID
- `AggregateRoot<T, U>`: Entry point to aggregates, raises domain events
- `DomainEvent`: Immutable facts about business events

**Application Layer:**
- `UseCase<T, R>`: Base class for use cases with request/response
- `UseCaseWithoutRequest<R>`: Use cases without request parameters
- `EventHandler<T, R>`: Handlers for domain events

**Example Pattern (Identity module):**
1. Controller implements generated OpenAPI interface (`CustomerRegistrationApi`)
2. Controller delegates to use case (`RegisterCustomer`)
3. Use case orchestrates domain logic and repositories
4. Domain aggregate (`Account`) contains business rules and raises events
5. Repository implementation (`AccountRepositoryImpl`) maps between domain models and database objects

### API-First Approach

**This project follows an API-first development workflow:**

1. **Define the contract** in `src/main/resources/openapi/openapi.yaml`
2. **Generate code** using `./gradlew openApiGenerate` (happens automatically during build)
3. **Implement controllers** that extend the generated API interfaces

**Generated Code:**
- Location: `build/generated/src/main/kotlin/`
- Packages: `app.yadwy.api` (API interfaces), `app.yadwy.model` (request/response DTOs)
- Generated interfaces include Spring MVC annotations (`@RequestMapping`, `@PostMapping`, etc.) and bean validation

**Example - Login Endpoint:**

**Step 1: Define contract in `openapi.yaml`**
```yaml
paths:
  /api/v1/auth/login:
    post:
      operationId: login
      tags:
        - Login
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LoginRequestDto'
      responses:
        '200':
          description: Login successful
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LoginResponseDto'
```

**Step 2: Code generation creates** `LoginApi` interface in `app.yadwy.api` package with method signature and Spring annotations

**Step 3: Implement the controller:**
```kotlin
@RestController
class LoginController(
    private val login: Login  // Use case
) : LoginApi {

    override fun login(loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        // Map generated DTO → application request model
        val request = LoginRequest(
            phoneNumber = loginRequestDto.phoneNumber,
            password = loginRequestDto.password
        )

        // Execute use case
        val response = login.execute(request)

        // Map application response → generated DTO
        return ResponseEntity.ok(
            LoginResponseDto(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        )
    }
}
```

**Key Points:**
- Controllers are lightweight adapters that delegate to use cases
- Mapping happens at controller level: Generated DTOs ↔ Application models
- Never manually edit generated code in `build/generated/`
- To add/modify endpoints: update `openapi.yaml`, regenerate, then implement/update controller

## Module Descriptions

### identity
Authentication and authorization module providing:
- Customer and seller registration with JWT token generation
- Login with phone number and password
- Access token refresh using refresh tokens
- Role-based authentication (CUSTOMER, SELLER roles)

**Key Domain Model:** `Account` aggregate with value objects `Name`, `PhoneNumber`, `AccountId`, and `Role` enum

**Security:**
- JWT-based stateless authentication
- HS256 algorithm with symmetric key (configured in `application.yml`)
- Access tokens expire in 24 hours, refresh tokens in 30 days
- Public endpoints: `/api/v1/auth/**`, Swagger UI
- All other endpoints require valid JWT

### sharedkernel
Contains shared domain primitives and base classes used across all modules:
- DDD base types (Entity, AggregateRoot, ValueObject, DomainEvent)
- Base use case classes
- Cross-cutting domain concepts

## Configuration

**Environment Profiles:**
- `application.yml`: Base configuration (JWT key, Flyway settings)
- `application-local.yml`: Local development (PostgreSQL on localhost:5434)
- `application-prod.yml`: Production configuration

**Database:**
- PostgreSQL database via Docker Compose: `yadwy_db` on port 5434
- Credentials: `yadwy_user` / `yadwy_pass`
- Flyway manages schema migrations automatically

## Testing APIs
HTTP test files located in `http-test/`:
- `Auth.http`: Test authentication endpoints (registration, login, refresh)
- Use IntelliJ HTTP Client or similar tools

## Important Conventions

**Naming:**
- Use case classes: Imperative verbs (e.g., `RegisterCustomer`, `Login`)
- Domain events: Past tense (e.g., `AccountCreatedEvent`)
- Repository methods: Standard patterns (`save`, `findById`, `findByPhoneNumber`)

**Data Flow:**
- API DTOs (generated) → Application Request Models → Domain Models → Application Response Models → API DTOs (generated)
- Never expose domain models directly in API responses
- Repository implementations handle mapping between domain models and database objects (DBO)

**Security:**
- Never commit sensitive data (the current JWT key in `application.yml` should be externalized in production)
- Spring Security configured in `SecurityConfig` with JWT resource server

**API Development:**
- Follow the API-first workflow: define contract in `openapi.yaml` → generate code → implement controller
- Controllers are lightweight adapters that only map DTOs and delegate to use cases
- All business logic belongs in use cases and domain models, never in controllers
