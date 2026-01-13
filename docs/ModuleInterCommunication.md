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
| React to something that happened in another module | Domain Events | Create seller profile when account is created |
| Query complex data across modules | Dedicated Query Service | Dashboard aggregating data from multiple modules |

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
