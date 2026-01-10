# Requirements Document

## Introduction

The Seller module is a bounded context within the yadwy-service marketplace platform responsible for managing seller profiles. It reacts to identity events (specifically `AccountCreatedEvent` with SELLER role) to automatically create seller records. The module follows DDD principles and maintains strict boundaries with other modules through event-driven communication.

## Glossary

- **Seller**: An aggregate root representing a seller profile in the marketplace, linked to an Account in the identity module
- **Seller_Module**: The bounded context responsible for seller profile management
- **Account_Created_Event**: A domain event published by the identity module when a new account is registered
- **Event_Handler**: An application layer component that listens for and processes domain events from other modules
- **Seller_Repository**: The contract for persisting and retrieving Seller aggregates

## Requirements

### Requirement 1: Seller Domain Model

**User Story:** As a system architect, I want a Seller aggregate root with proper value objects, so that seller data is modeled following DDD tactical patterns.

#### Acceptance Criteria

1. THE Seller_Module SHALL define a `SellerId` value object using `@JvmInline value class` wrapping a Long
2. THE Seller_Module SHALL define a `StoreName` value object for the seller's store/shop name
3. THE Seller_Module SHALL define a `Seller` aggregate root containing sellerId, accountId (Long reference), and storeName
4. THE Seller aggregate SHALL extend `AggregateRoot<SellerId, SellerEvent>`
5. THE Seller aggregate SHALL provide a factory method `create()` for instantiation with validation
6. WHEN a Seller is created THEN THE Seller aggregate SHALL raise a `SellerCreatedEvent`

### Requirement 2: Seller Domain Events

**User Story:** As a system architect, I want seller-specific domain events, so that other modules can react to seller lifecycle changes.

#### Acceptance Criteria

1. THE Seller_Module SHALL define a `SellerEvent` sealed interface extending `DomainEvent`
2. THE Seller_Module SHALL define a `SellerCreatedEvent` data class containing sellerId and accountId
3. THE SellerCreatedEvent SHALL be raised when a new Seller is successfully created

### Requirement 3: Seller Repository Contract

**User Story:** As a developer, I want a repository interface for Seller persistence, so that the domain layer remains independent of infrastructure concerns.

#### Acceptance Criteria

1. THE Seller_Module SHALL define a `SellerRepository` interface in the domain contracts package
2. THE SellerRepository SHALL provide a `save(seller: Seller): Seller` method
3. THE SellerRepository SHALL provide a `findById(sellerId: Long): Seller?` method
4. THE SellerRepository SHALL provide a `findByAccountId(accountId: Long): Seller?` method

### Requirement 4: Seller Database Infrastructure

**User Story:** As a developer, I want database infrastructure for persisting sellers, so that seller data is stored in PostgreSQL.

#### Acceptance Criteria

1. THE Seller_Module SHALL define a `SellerDbo` data class mapped to the `seller.sellers` table
2. THE SellerDbo SHALL contain id, accountId, and storeName fields
3. THE Seller_Module SHALL define a `SellerDao` interface extending `ListCrudRepository`
4. THE SellerDao SHALL provide a `findByAccountId(accountId: Long): SellerDbo?` method
5. THE Seller_Module SHALL create a Flyway migration `V2__create_sellers_table.sql` with a `seller` schema

### Requirement 5: Seller Repository Implementation

**User Story:** As a developer, I want a repository implementation that maps between domain and database objects, so that persistence is handled transparently.

#### Acceptance Criteria

1. THE Seller_Module SHALL implement `SellerRepositoryImpl` in the infrastructure layer
2. THE SellerRepositoryImpl SHALL map Seller domain models to SellerDbo for persistence
3. THE SellerRepositoryImpl SHALL map SellerDbo back to Seller domain models on retrieval
4. THE SellerRepositoryImpl SHALL publish domain events via EventPublisher after saving

### Requirement 6: Account Created Event Handler

**User Story:** As a system architect, I want the seller module to automatically create a seller when an account with SELLER role is created, so that seller profiles are provisioned without manual intervention.

#### Acceptance Criteria

1. THE Seller_Module SHALL define an `OnAccountCreated` event handler extending `EventHandler`
2. WHEN an `AccountCreatedEvent` is received with SELLER role THEN THE event handler SHALL create a new Seller
3. WHEN an `AccountCreatedEvent` is received without SELLER role THEN THE event handler SHALL ignore the event
4. THE event handler SHALL use Spring's `@EventListener` annotation for event subscription
5. THE event handler SHALL generate a default store name from the accountId (e.g., "Store-{accountId}")
6. IF a Seller already exists for the accountId THEN THE event handler SHALL not create a duplicate

### Requirement 7: Module Boundary Enforcement

**User Story:** As a system architect, I want strict module boundaries, so that the seller module does not leak domain models to other bounded contexts.

#### Acceptance Criteria

1. THE Seller_Module SHALL NOT import domain models from the identity module (except for the event)
2. THE Seller_Module SHALL only reference accountId as a Long primitive, not as AccountId value object
3. THE Seller_Module SHALL communicate with other modules only through domain events
4. THE Seller_Module SHALL follow the package structure defined in AGENTS.md

### Requirement 8: Spring Modulith Verification

**User Story:** As a system architect, I want Spring Modulith to verify module boundaries, so that architectural violations are caught at test time.

#### Acceptance Criteria

1. THE Seller_Module SHALL pass Spring Modulith architecture verification tests
2. THE Seller_Module SHALL NOT have cyclic dependencies with other modules
3. THE Seller_Module SHALL only access exposed APIs from other modules (domain events)
4. WHEN the modulith verification test runs THEN THE Seller_Module SHALL not violate any module boundaries
5. THE project SHALL include a modulith verification test that validates all module boundaries
