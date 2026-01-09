# Domain-Driven Design (DDD) Guidelines

This document outlines the DDD tactical patterns used in this codebase and how to implement them using our base classes.

## Overview

We follow [Domain-Driven Design (DDD)](https://martinfowler.com/bliki/DomainDrivenDesign.html) tactical patterns to model our business domain. All base types are defined in `sharedkernel/domain/models/base/DomainModels.kt`.

## Core Tactical Patterns

### 1. Value Objects

**What:** Immutable objects identified by their properties, not by identity. Two value objects with the same properties are considered equal.

**When to use:** For domain concepts without lifecycle or identity (e.g., money, dates, phone numbers, addresses).

**Base interface:** `ValueObject`

**Implementation:**

```kotlin
// Single-property value objects: use @JvmInline value class
@JvmInline
value class AccountId(val id: Long) : ValueObject

// Multi-property value objects: use data class
data class Address(
    val street: String,
    val city: String,
    val zipCode: String
) : ValueObject
```

**Examples in codebase:**
- `identity/domain/models/AccountId.kt` - Single property with `@JvmInline`
- `identity/domain/models/PhoneNumber.kt` - Single property value object
- `identity/domain/models/Name.kt` - Single property value object

**Rules:**
- Must be immutable (use `val`, not `var`)
- Contain validation logic in constructor or factory method
- No identity - equality based on properties
- Prefer `@JvmInline value class` for single-property VOs (zero runtime overhead)

---

### 2. Entities

**What:** Objects with a unique identity that persists throughout their lifecycle. Two entities with the same ID are considered equal, even if their properties differ.

**When to use:** For domain objects that have lifecycle and need to be tracked individually.

**Base class:** `Entity<T : ValueObject>`

**Implementation:**

```kotlin
class Order(
    orderId: OrderId,
    private var status: OrderStatus,
    private var items: List<OrderItem>
) : Entity<OrderId>(orderId) {

    fun updateStatus(newStatus: OrderStatus) {
        this.status = newStatus
    }

    fun getStatus() = status
    fun getItems() = items.toList()
}
```

**Rules:**
- Identity (ID) is a value object passed to the base class
- Equality based solely on ID (handled by `Entity` base class)
- Can contain mutable state (use `private var` with public getters)
- Expose behavior through methods, not raw property access

---

### 3. Aggregate Roots

**What:** The entry point to an aggregate - a cluster of entities and value objects treated as a single unit for data changes. Only aggregate roots can be referenced from outside.

**When to use:** For domain objects that are the top of an aggregate boundary and need to raise domain events.

**Base class:** `AggregateRoot<T : ValueObject, U : DomainEvent>`

**Implementation:**

```kotlin
class Account(
    private val accountId: AccountId,
    private val name: Name,
    private val phoneNumber: PhoneNumber,
    private val passwordHash: String,
    private val roles: MutableSet<Role>
) : AggregateRoot<AccountId, IdentityEvent>(id = accountId) {

    companion object {
        fun register(
            name: String,
            phone: String,
            rawPassword: String,
            roles: MutableSet<Role>,
            encryptionService: EncryptionService
        ): Account {
            // Validation
            if (phone.isBlank()) throw InvalidPhoneNumberException(phone)

            val account = Account(...)

            // Raise domain event
            account.raiseEvent(AccountCreatedEvent(account.accountId, roles.toSet()))

            return account
        }
    }

    fun login(rawPassword: String, ...): Pair<String, String> {
        // Business logic
    }
}
```

**Examples in codebase:**
- `identity/domain/models/Account.kt` - Main aggregate in identity module

**Rules:**
- Use factory methods (companion object) for creation to ensure invariants
- Raise domain events using `raiseEvent(event)` for significant business actions
- Protect internal state with `private` fields and expose through methods
- All external access to an aggregate goes through the root
- Use `occurredEvents()` to retrieve and clear domain events (typically in use cases)

---

### 4. Domain Events

**What:** Immutable facts describing something significant that happened in the business domain.

**When to use:** To communicate that something important occurred, enabling loose coupling between modules.

**Base interface:** `DomainEvent`

**Implementation:**

```kotlin
// Define module-specific event interface
sealed interface IdentityEvent : DomainEvent

// Define concrete events as data classes
data class AccountCreatedEvent(
    val accountId: AccountId,
    val roles: Set<Role>
) : IdentityEvent

data class AccountDeletedEvent(
    val accountId: AccountId,
    val deletedAt: LocalDateTime
) : IdentityEvent
```

**Examples in codebase:**
- `identity/domain/events/IdentityEvent.kt` - Event definitions

**Rules:**
- Use `sealed interface` for module-specific events (enables exhaustive `when`)
- Use `data class` for concrete events (immutability)
- Name in past tense (what happened, not what should happen)
- Include relevant data for event consumers
- Raised by aggregate roots, not by use cases

---

## Implementation Workflow

### Creating a New Aggregate

1. **Identify the aggregate boundary** - What entities/VOs belong together?
2. **Create value objects** for IDs and domain concepts
3. **Define domain events** using `sealed interface` + `data class`
4. **Create aggregate root** extending `AggregateRoot<ID, EventType>`
5. **Implement factory methods** with validation and event raising
6. **Add business methods** that maintain invariants
7. **Define repository interface** in `domain/contracts/`
8. **Implement repository** in `infrastructure/repositories/`

### Example Structure

```
identity/
├── domain/
│   ├── models/
│   │   ├── Account.kt              # Aggregate Root
│   │   ├── AccountId.kt            # Value Object (ID)
│   │   ├── Name.kt                 # Value Object
│   │   └── PhoneNumber.kt          # Value Object
│   ├── events/
│   │   └── IdentityEvent.kt        # Domain Events
│   ├── contracts/
│   │   └── AccountRepository.kt    # Repository interface
│   └── exceptions/
│       └── Exception.kt            # Domain exceptions
└── infrastructure/
    └── repositories/
        └── AccountRepositoryImpl.kt # Repository implementation
```

---

## Best Practices

### Value Objects
- ✅ Use `@JvmInline value class` for single-property VOs
- ✅ Validate in constructor/factory method
- ✅ Make immutable (`val` only)
- ❌ Don't use mutable collections (`MutableList`, `MutableSet`)

### Entities & Aggregates
- ✅ Factory methods in `companion object` for creation
- ✅ Private mutable state with public getters
- ✅ Raise events for significant business actions
- ✅ Keep aggregates small and focused
- ❌ Don't expose domain models directly in API responses
- ❌ Don't access aggregates through child entities

### Domain Events
- ✅ Past tense naming (`AccountCreatedEvent`, not `CreateAccountEvent`)
- ✅ Immutable data classes
- ✅ Use `sealed interface` for type safety
- ✅ Include necessary context for consumers
- ❌ Don't use events for command/request patterns

### Repository Pattern
- ✅ Define interface in `domain/contracts/`
- ✅ Implement in `infrastructure/repositories/`
- ✅ Return domain models, not database objects
- ✅ Use standard method names (`save`, `findById`, `findBy*`)
- ❌ Don't leak infrastructure concerns to domain layer

---

## Using Base Classes

All base types are in `sharedkernel/domain/models/base/DomainModels.kt`:

```kotlin
interface DomainModel                           // Marker for all domain types
interface ValueObject : DomainModel             // Immutable, identified by properties
interface DomainEvent : DomainModel             // Immutable business facts
open class Entity<T : ValueObject>(val id: T)  // Objects with identity
abstract class AggregateRoot<T, U>(id: T)       // Aggregate entry point with events
```

### Methods on AggregateRoot

```kotlin
protected fun raiseEvent(event: U)              // Raise domain event
fun occurredEvents(): List<U>                   // Get and clear events
```

---

## Further Reading

- [Domain-Driven Design - Martin Fowler](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [DDD Aggregate Pattern](https://martinfowler.com/bliki/DDD_Aggregate.html)
- [Value Object Pattern](https://martinfowler.com/bliki/ValueObject.html)
- [Domain Events - Martin Fowler](https://martinfowler.com/eaaDev/DomainEvent.html)
- [Kotlin Inline Value Classes](https://kotlinlang.org/docs/inline-classes.html)
