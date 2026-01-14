# CLAUDE.md

## What This Is

Kotlin Spring Boot 4.0 microservice for a handmade marketplace. DDD + Spring Modulith. Spring Data JDBC (not JPA - this matters for how you write repositories).

## Commands You'll Actually Use

```bash
./gradlew build                    # Build everything (runs openApiGenerate automatically)
./gradlew bootRun --args='--spring.profiles.active=local'  # Run locally
docker compose up -d               # Start PostgreSQL first
./gradlew test --tests 'TestName'  # Run specific test
```

## The Architecture (Don't Fight It)

```
<module>/
├── api/           # Public module API for inter-module communication (NOT HTTP controllers)
├── application/   # Use cases only - no business logic here
├── domain/        # ALL business logic lives here
└── infrastructure/
    ├── controllers/  # HTTP controllers (implement OpenAPI interfaces)
    ├── gateway/      # Implementations of external module gateways
    ├── repositories/ # Database implementations
    └── database/     # DAOs, DBOs
```

See `docs/ModuleInterCommunication.md` for detailed inter-module communication patterns.

## Critical Rules

**Domain logic stays in domain models. Period.**

Why: We've had bugs from validation scattered across layers. If an aggregate can be invalid, something's wrong.

```kotlin
// ✅ Validation inside aggregate
fun changePassword(current: String, new: String) {
    require(passwordEncoder.matches(current, this.passwordHash)) { "Wrong password" }
    require(new.length >= 8) { "Too short" }
    this.passwordHash = passwordEncoder.encode(new)
    raise(PasswordChangedEvent(this.id))
}

// ❌ Never do this - logic leaked to use case
class ChangePassword : UseCase<...> {
    override fun execute(request: Request) {
        if (request.newPassword.length < 8) throw ValidationException() // NO
        account.passwordHash = encoder.encode(request.newPassword)      // NO - direct mutation
    }
}
```

**Value objects validate themselves:**

```kotlin
@JvmInline
value class PhoneNumber private constructor(val value: String) : ValueObject {
    companion object {
        fun create(value: String): PhoneNumber {
            require(value.matches(Regex("^01[0125][0-9]{8}$"))) { "Invalid Egyptian phone" }
            return PhoneNumber(value)
        }
    }
}
```

## API-First Workflow

1. Edit `src/main/resources/openapi/openapi.yaml`
2. Build regenerates code automatically
3. Implement controller extending generated interface

Why: Generated DTOs have validation annotations. Controllers are just adapters - map DTOs to application models, call use case, map response back.

```kotlin
@RestController
class LoginController(private val login: Login) : LoginApi {
    override fun login(dto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val response = login.execute(LoginRequest(dto.phoneNumber, dto.password))
        return ResponseEntity.ok(LoginResponseDto(response.accessToken, response.refreshToken))
    }
}
```

Never edit `build/generated/` - it gets overwritten.

## RESTful URLs

- `/api/v1/products` not `/api/v1/getProducts`
- `/sellers/{sellerId}/products` for nested resources
- HTTP methods do the verbs: GET/POST/PUT/PATCH/DELETE

Why: Consistency matters when the API grows. We don't want mixed conventions.

## Data Flow

`Generated DTO → Application Request → Domain Model → Application Response → Generated DTO`

Never expose domain models in API responses. Why: Domain models change for business reasons, API contracts change for client reasons. Keep them decoupled.

## Module Boundaries

Spring Modulith enforces these. Modules communicate through:
1. **Public Module APIs** (`api/` package) - For synchronous queries between modules
2. **Domain Events** - For asynchronous reactions to business actions

**Never** access another module's repositories, domain models, or internal types directly.

Current modules:
- `identity/` - Auth, JWT, registration
- `customer/` - Customer profiles
- `seller/` - Seller profiles and store management
- `product/` - Product catalog management
- `category/` - Product categories
- `cart/` - Shopping cart management
- `sharedkernel/` - Base types (Entity, AggregateRoot, ValueObject, UseCase, Amount, Quantity)

## Shared Value Objects (Use These!)

**Always use `Amount` for monetary values - never raw `BigDecimal`:**

```kotlin
// ✅ Correct - use Amount from sharedkernel
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

val price: Amount = Amount.of(99.99)
val total: Amount = price * quantity  // Supports arithmetic operations
val discounted: Amount = price - discount

// ❌ Wrong - don't use raw BigDecimal for money
val price: BigDecimal = BigDecimal("99.99")
```

**Always use `Quantity` for item counts - never raw `Int`:**

```kotlin
// ✅ Correct - use Quantity from sharedkernel
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

val qty: Quantity = Quantity.of(5)
val newQty: Quantity = qty + 3  // Supports arithmetic
val total: Amount = unitPrice * qty  // Works with Amount

// ❌ Wrong - don't use raw Int for quantities in domain
val quantity: Int = 5
```

Why: These value objects enforce validation (no negative amounts/quantities), provide type safety, and support domain-specific operations. Located in `sharedkernel/domain/models/`.

See `docs/ModuleInterCommunication.md` for the Gateway pattern.

## Database

PostgreSQL on port 5434 (Docker Compose). Flyway migrations in `src/main/resources/db/migration/V*.sql` run on startup.

## Security

JWT with HS256. Public: `/api/v1/auth/**`, Swagger UI. Everything else needs valid token.

The JWT key in `application.yml` is for dev only - don't use in prod.

## Naming Conventions

- Use cases: `RegisterCustomer`, `Login` (imperative)
- Events: `AccountCreatedEvent` (past tense)
- Repos: `save`, `findById`, `findByPhoneNumber`

## Testing

HTTP files in `http-test/` for manual API testing. Use IntelliJ HTTP Client.