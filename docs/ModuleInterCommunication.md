# Module Inter-Communication Pattern

This document describes how modules communicate with each other in our Spring Modulith architecture while maintaining proper boundaries and loose coupling.

## Overview

In a modular monolith, modules must sometimes query data from other modules. Direct access to another module's repositories or domain models breaks encapsulation and creates tight coupling. Instead, we use the **Gateway Pattern** with **Public Module APIs**.
## Key Concepts

### 1. Public Module API (`api/` package)

The `api/` package at the module root is the **only** public interface for inter-module communication. It contains:

- **API Interface** - Methods other modules can call
- **DTOs** - Data transfer objects (primitives or simple data classes)
- **Module Metadata** - Spring Modulith configuration

```kotlin
// seller/api/SellerAPI.kt
package yadwy.app.yadwyservice.seller.api

interface SellerAPI {
    fun findSellerByAccountId(accountId: Long): SellerDto?
    fun sellerExists(sellerId: Long): Boolean
}
```

```kotlin
// seller/api/SellerDto.kt
package yadwy.app.yadwyservice.seller.api

data class SellerDto(
    val id: Long
)
```

```kotlin
// seller/api/ModuleMetadata.kt
package yadwy.app.yadwyservice.seller.api

import org.springframework.modulith.ApplicationModule
import org.springframework.modulith.ApplicationModule.Type
import org.springframework.modulith.PackageInfo

@ApplicationModule(type = Type.OPEN)
@PackageInfo
class ModuleMetadata
```

### 2. Gateway Contract (`domain/contracts/`)

The consuming module defines what it needs from external modules as a **Gateway interface** in its domain layer. This follows the Dependency Inversion Principle - the domain defines the contract, infrastructure implements it.

```kotlin
// product/domain/contracts/SellerGateway.kt
package yadwy.app.yadwyservice.product.domain.contracts

interface SellerGateway {
    fun findSellerIdByAccountId(accountId: Long): SellerDto?
    fun sellerExists(sellerId: Long): Boolean
}
```

### 3. Gateway Implementation (`infrastructure/gateway/`)

The infrastructure layer implements the gateway by calling the public module API:

```kotlin
// product/infrastructure/gateway/SellerGatewayImpl.kt
package yadwy.app.yadwyservice.product.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.seller.api.SellerAPI
import yadwy.app.yadwyservice.seller.api.SellerDto

@Service
class SellerGatewayImpl(
    private val sellerAPI: SellerAPI
) : SellerGateway {

    override fun findSellerIdByAccountId(accountId: Long): SellerDto? {
        return sellerAPI.findSellerByAccountId(accountId)
    }

    override fun sellerExists(sellerId: Long): Boolean {
        return sellerAPI.sellerExists(sellerId)
    }
}
```

### 4. API Implementation (`infrastructure/api/`)

The providing module implements its public API in the infrastructure layer:

```kotlin
// seller/infrastructure/api/SellerAPIImpl.kt
package yadwy.app.yadwyservice.seller.infrastructure.api

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.seller.api.SellerAPI
import yadwy.app.yadwyservice.seller.api.SellerDto
import yadwy.app.yadwyservice.seller.domain.contracts.SellerRepository

@Service
class SellerAPIImpl(
    private val sellerRepository: SellerRepository
) : SellerAPI {

    override fun findSellerByAccountId(accountId: Long): SellerDto? {
        return sellerRepository.findByAccountId(accountId)?.let {
            SellerDto(id = it.getId().id)
        }
    }

    override fun sellerExists(sellerId: Long): Boolean {
        return sellerRepository.existsById(sellerId)
    }
}
```

## Directory Structure

### Provider Module (e.g., Seller)

```
seller/
├── api/                              # PUBLIC - exposed to other modules
│   ├── SellerAPI.kt                  # Interface for inter-module calls
│   ├── SellerDto.kt                  # DTOs (no domain types!)
│   └── ModuleMetadata.kt             # @ApplicationModule(type = OPEN)
│
├── application/
│   └── usecases/                     # Internal use cases
│
├── domain/
│   ├── models/                       # INTERNAL - never expose
│   ├── contracts/                    # INTERNAL - repository interfaces
│   └── events/                       # Domain events (can be public)
│
└── infrastructure/
    ├── api/                          # Implements SellerAPI
    │   └── SellerAPIImpl.kt
    ├── controllers/                  # HTTP endpoints
    ├── repositories/                 # Repository implementations
    └── database/                     # DAOs, DBOs
```

### Consumer Module (e.g., Product)

```
product/
├── application/
│   └── usecases/                     # Inject gateways here
│
├── domain/
│   ├── models/
│   ├── contracts/
│   │   ├── ProductRepository.kt     # Internal repository
│   │   ├── SellerGateway.kt         # External module gateway
│   │   └── CategoryGateway.kt       # External module gateway
│   └── events/
│
└── infrastructure/
    ├── controllers/                  # HTTP endpoints
    ├── gateway/                      # Gateway implementations
    │   ├── SellerGatewayImpl.kt     # Calls SellerAPI
    │   └── CategoryGatewayImpl.kt   # Calls CategoryAPI
    ├── repositories/
    └── database/
```

## Rules

### ✅ DO

- Define public APIs in `api/` package with `@ApplicationModule(type = Type.OPEN)`
- Use DTOs with primitive types or simple data classes in public APIs
- Define gateway interfaces in `domain/contracts/` for external dependencies
- Implement gateways in `infrastructure/gateway/`
- Keep domain models internal to the module

### ❌ DON'T

- Access another module's repository directly
- Import another module's domain models
- Expose domain aggregates through public APIs
- Put HTTP controllers in `api/` package (use `infrastructure/controllers/`)

## When to Use Each Communication Pattern

| Scenario | Pattern | Example |
|----------|---------|---------|
| Need data from another module synchronously | Gateway + Public API | Product needs to check if seller exists |
| React to something that happened in another module | Integration Events | Deduct inventory when order is placed |
| Query complex data across modules | Dedicated Query Service | Dashboard aggregating data from multiple modules |

---

## Integration Events (Async Communication)

For asynchronous reactions to events in other modules, we use **Integration Events**. These are different from domain events:

- **Domain Events** - Internal to a module, contain domain types, never cross module boundaries
- **Integration Events** - Public contracts for cross-module communication, contain only primitives

### Why Not Use Domain Events Directly?

Using domain events for inter-module communication violates DDD principles:
1. Domain events contain domain types (value objects, entity IDs) that shouldn't be exposed
2. Creates tight coupling between modules
3. Changes to internal domain models would break consumers

### Integration Event Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Publisher Module                                │
│  ┌─────────────┐    ┌──────────────┐    ┌─────────────────────────────────┐ │
│  │   Domain    │───▶│  Repository  │───▶│  Event Mapper (api/)            │ │
│  │  Aggregate  │    │    Impl      │    │  DomainEvent → IntegrationEvent │ │
│  └─────────────┘    └──────────────┘    └─────────────────────────────────┘ │
│        │                   │                           │                     │
│        │ raises            │ saves                     │ maps                │
│        ▼                   ▼                           ▼                     │
│  DomainEvent          Database              IntegrationEventPublisher        │
│  (internal)                                          │                       │
│                                                      ▼                       │
│                                          Spring ApplicationEventPublisher    │
└─────────────────────────────────────────────────────────────────────────────┘
                                                       │
                                    Spring Event Bus   │
                                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Consumer Module                                 │
│                     ┌────────────────────────────────┐                      │
│                     │  Consumer (infrastructure/)    │                      │
│                     │  @TransactionalEventListener   │                      │
│                     └────────────────────────────────┘                      │
│                                    │                                        │
│                                    ▼                                        │
│                     ┌────────────────────────────────┐                      │
│                     │  EventHandler (application/)   │                      │
│                     │  extends EventHandler<T, R>    │                      │
│                     └────────────────────────────────┘                      │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Shared Kernel Components

The `sharedkernel` provides the base infrastructure:

```kotlin
// sharedkernel/domain/models/base/IntegrationEvent.kt
interface IntegrationEvent

// sharedkernel/domain/contracts/IntegrationEventPublisher.kt
interface IntegrationEventPublisher {
    fun publish(event: IntegrationEvent)
    fun publishAll(events: Collection<IntegrationEvent>)
}

// sharedkernel/infrastructure/publishers/SpringIntegrationEventPublisher.kt
@Component
class SpringIntegrationEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : IntegrationEventPublisher {
    override fun publish(event: IntegrationEvent) {
        applicationEventPublisher.publishEvent(event)
    }
    override fun publishAll(events: Collection<IntegrationEvent>) {
        events.forEach { publish(it) }
    }
}
```

### Publisher Module Structure

Integration events and their mappers live in the `api/` package:

```
identity/
├── api/                                    # PUBLIC
│   ├── IdentityIntegrationEvent.kt         # Sealed interface + event classes
│   ├── AccountEventMapper.kt               # Domain → Integration mapping
│   └── ModuleMetadata.kt
│
├── domain/
│   └── events/
│       └── IdentityEvent.kt                # INTERNAL domain events
│
└── infrastructure/
    └── repositories/
        └── AccountRepositoryImpl.kt        # Publishes integration events
```

### Defining Integration Events

Group all integration events for a module in a single file with a sealed interface:

```kotlin
// identity/api/IdentityIntegrationEvent.kt
package yadwy.app.yadwyservice.identity.api

import yadwy.app.yadwyservice.sharedkernel.domain.models.base.IntegrationEvent

sealed interface IdentityIntegrationEvent : IntegrationEvent

data class SellerAccountCreatedIntegrationEvent(
    val accountId: Long,
    val name: String
) : IdentityIntegrationEvent

data class CustomerAccountCreatedIntegrationEvent(
    val accountId: Long,
    val name: String
) : IdentityIntegrationEvent
```

**Rules for Integration Events:**
- Use only primitive types (Long, String, Int, Boolean)
- No domain types (Amount, Quantity, value objects)
- Sealed interface enables exhaustive pattern matching
- Named with `IntegrationEvent` suffix

### Event Mapper

Extension functions map domain events to integration events:

```kotlin
// identity/api/AccountEventMapper.kt
package yadwy.app.yadwyservice.identity.api

fun IdentityEvent.toIntegrationEvent(persistedAccountId: AccountId): IntegrationEvent? {
    return when (this) {
        is AccountCreatedEvent -> this.toIntegrationEvent(persistedAccountId)
    }
}

private fun AccountCreatedEvent.toIntegrationEvent(persistedAccountId: AccountId): IntegrationEvent? {
    return when {
        roles.contains(Role.SELLER) -> SellerAccountCreatedIntegrationEvent(
            accountId = persistedAccountId.id,
            name = name
        )
        roles.contains(Role.CUSTOMER) -> CustomerAccountCreatedIntegrationEvent(
            accountId = persistedAccountId.id,
            name = name
        )
        else -> null
    }
}
```

### Publishing from Repository

The repository publishes integration events after saving:

```kotlin
// identity/infrastructure/repositories/AccountRepositoryImpl.kt
@Component
class AccountRepositoryImpl(
    private val accountDao: AccountDao,
    private val integrationEventPublisher: IntegrationEventPublisher
) : AccountRepository {

    override fun save(account: Account): Account {
        val savedAccount = accountDao.save(account.toDbo())
        val persistedAccountId = AccountId(savedAccount.id!!)

        // Map domain events to integration events and publish
        val integrationEvents = account.occurredEvents()
            .mapNotNull { it.toIntegrationEvent(persistedAccountId) }
        integrationEventPublisher.publishAll(integrationEvents)

        return savedAccount.toDomain()
    }
}
```

### Consumer Module Structure

```
product/
├── application/
│   └── usecases/
│       └── HandleOrderPlaced.kt            # EventHandler
│
└── infrastructure/
    └── consumers/
        └── OrderPlacedConsumer.kt          # @TransactionalEventListener
```

### Event Consumer

The consumer receives typed integration events:

```kotlin
// product/infrastructure/consumers/OrderPlacedConsumer.kt
@Component
class OrderPlacedConsumer(
    private val handleOrderPlaced: HandleOrderPlaced
) {
    private val logger = LoggerFactory.getLogger(OrderPlacedConsumer::class.java)

    @TransactionalEventListener
    fun onOrderPlaced(event: OrderPlacedIntegrationEvent) {
        logger.info("Processing OrderPlacedIntegrationEvent for orderId: {}", event.orderId)
        handleOrderPlaced.handle(event)
    }
}
```

### Event Handler

The handler extends `EventHandler<T, R>` from sharedkernel:

```kotlin
// product/application/usecases/HandleOrderPlaced.kt
@Component
class HandleOrderPlaced(
    private val productRepository: ProductRepository
) : EventHandler<OrderPlacedIntegrationEvent, Unit>() {

    override fun handle(event: OrderPlacedIntegrationEvent) {
        event.lineItems.forEach { lineItem ->
            try {
                val product = productRepository.findById(lineItem.productId)
                if (product == null) {
                    logger.warn("Product not found: {}", lineItem.productId)
                    return@forEach
                }
                product.decrementStock(lineItem.quantity)
                productRepository.save(product)
            } catch (e: Exception) {
                logger.error("Failed to deduct inventory for productId: {}", lineItem.productId)
            }
        }
    }
}
```

### Can Application Layer Depend on Integration Events?

**Yes.** Integration events in `api/` are public contracts, similar to API interfaces and DTOs. They contain only primitives, so there's no coupling to another module's domain.

The dependency direction is correct:
```
product/application → order/api (public contract) ✅
product/application → order/domain (internal) ❌
```

### Integration Events vs Gateway Pattern

| Aspect | Gateway Pattern | Integration Events |
|--------|-----------------|-------------------|
| Communication | Synchronous | Asynchronous |
| Coupling | Request/Response | Fire and forget |
| Use case | Query data | React to events |
| Example | Check if seller exists | Deduct inventory on order |

### Summary

1. **Domain events stay internal** - Never expose domain events across modules
2. **Integration events are public contracts** - Live in `api/` package
3. **Use primitives only** - No domain types in integration events
4. **Sealed interfaces** - Enable exhaustive pattern matching
5. **Mapper in api/** - Maps domain events to integration events
6. **Repository publishes** - After saving, map and publish integration events
7. **Consumer in infrastructure/** - Receives typed events via `@TransactionalEventListener`
8. **Handler in application/** - Extends `EventHandler<T, R>`, contains business logic

## Benefits

1. **True Module Isolation** - Modules don't know about each other's internals
2. **Explicit Contracts** - Public APIs are the only integration points
3. **Dependency Inversion** - Consumer defines what it needs, not what provider offers
4. **Testability** - Mock gateways in tests without needing other modules
5. **Refactoring Safety** - Internal changes don't break other modules
6. **Spring Modulith Compliance** - Architecture tests pass

## Example: Product Module Using Seller and Category

```kotlin
// product/application/usecases/CreateProduct.kt
@Service
class CreateProduct(
    private val productRepository: ProductRepository,
    private val sellerGateway: SellerGateway,      // Gateway, not SellerRepository!
    private val categoryGateway: CategoryGateway   // Gateway, not CategoryRepository!
) : UseCase<CreateProductRequest, ProductResponse> {

    override fun execute(request: CreateProductRequest): ProductResponse {
        // Use gateway to check seller exists
        val seller = sellerGateway.findSellerIdByAccountId(request.accountId)
            ?: throw SellerNotFoundException(request.accountId)

        // Use gateway to check category exists
        if (!categoryGateway.categoryExists(request.categoryId)) {
            throw CategoryNotFoundException(request.categoryId)
        }

        val product = Product.create(
            sellerId = seller.id,
            categoryId = request.categoryId,
            // ... other fields
        )

        return productRepository.save(product).toResponse()
    }
}
```

## Related Documentation

- `docs/DDD.md` - Domain-Driven Design patterns
- `AGENTS.md` - Project architecture overview
