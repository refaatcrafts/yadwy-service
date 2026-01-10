# Implementation Plan: Seller Module

## Overview

This implementation plan creates the seller module following DDD patterns and Spring Modulith conventions. Tasks are ordered to build incrementally: domain layer first, then infrastructure, then application layer (event handler), and finally tests.

## Tasks

- [x] 1. Create domain layer value objects and events
  - [x] 1.1 Create SellerId value object
    - Create `seller/domain/models/SellerId.kt`
    - Use `@JvmInline value class` wrapping Long
    - Implement `ValueObject` interface
    - _Requirements: 1.1_

  - [x] 1.2 Create StoreName value object with validation
    - Create `seller/domain/models/StoreName.kt`
    - Use `@JvmInline value class` wrapping String
    - Add validation: not blank, max 100 characters
    - _Requirements: 1.2_

  - [x] 1.3 Create SellerEvent sealed interface and SellerCreatedEvent
    - Create `seller/domain/events/SellerEvent.kt`
    - Define `SellerEvent` sealed interface extending `DomainEvent`
    - Define `SellerCreatedEvent` data class with sellerId and accountId
    - _Requirements: 2.1, 2.2, 2.3_

- [x] 2. Create Seller aggregate root
  - [x] 2.1 Implement Seller aggregate root
    - Create `seller/domain/models/Seller.kt`
    - Extend `AggregateRoot<SellerId, SellerEvent>`
    - Include sellerId, accountId (Long), storeName fields
    - Implement `create()` factory method that raises SellerCreatedEvent
    - Add getter methods
    - _Requirements: 1.3, 1.4, 1.5, 1.6_

  - [x] 2.2 Write property tests for Seller aggregate
    - **Property 2: Seller Factory Creates Valid Instances**
    - **Property 3: Seller Creation Raises Event**
    - **Validates: Requirements 1.5, 1.6**

  - [x] 2.3 Write property test for StoreName validation
    - **Property 1: StoreName Validation**
    - **Validates: Requirements 1.2**

- [x] 3. Create repository contract
  - [x] 3.1 Define SellerRepository interface
    - Create `seller/domain/contracts/SellerRepository.kt`
    - Define `save(seller: Seller): Seller` method
    - Define `findById(sellerId: Long): Seller?` method
    - Define `findByAccountId(accountId: Long): Seller?` method
    - _Requirements: 3.1, 3.2, 3.3, 3.4_

- [x] 4. Create database infrastructure
  - [x] 4.1 Create Flyway migration for sellers table
    - Create `V2__create_sellers_table.sql`
    - Create `seller` schema
    - Create `sellers` table with id, account_id, store_name
    - Add UNIQUE constraint on account_id
    - Add index on account_id
    - _Requirements: 4.5_

  - [x] 4.2 Create SellerDbo database object
    - Create `seller/infrastructure/database/dbo/SellerDbo.kt`
    - Map to `seller.sellers` table
    - Include id, accountId, storeName fields
    - _Requirements: 4.1, 4.2_

  - [x] 4.3 Create SellerDao interface
    - Create `seller/infrastructure/database/dao/SellerDao.kt`
    - Extend `ListCrudRepository<SellerDbo, Long>`
    - Add `findByAccountId(accountId: Long): SellerDbo?` method
    - _Requirements: 4.3, 4.4_

- [x] 5. Implement repository
  - [x] 5.1 Implement SellerRepositoryImpl
    - Create `seller/infrastructure/repositories/SellerRepositoryImpl.kt`
    - Inject SellerDao and EventPublisher
    - Implement save() with domain-to-DBO mapping and event publishing
    - Implement findById() with DBO-to-domain mapping
    - Implement findByAccountId() with DBO-to-domain mapping
    - _Requirements: 5.1, 5.2, 5.3, 5.4_

  - [x] 5.2 Write integration test for repository round-trip
    - **Property 4: Repository Round-Trip Consistency**
    - **Validates: Requirements 5.2, 5.3**

- [x] 6. Checkpoint - Verify domain and infrastructure layers
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Create event handler and consumer
  - [x] 7.1 Implement HandleAccountCreated use case
    - Create `seller/application/usecases/HandleAccountCreated.kt`
    - Extend `EventHandler<AccountCreatedEvent, Unit>`
    - Add `@Component` annotation
    - Check for SELLER role before creating seller
    - Check for existing seller (idempotency)
    - Generate default store name as "Store-{accountId}"
    - Save seller via repository
    - _Requirements: 6.1, 6.2, 6.3, 6.5, 6.6_

  - [x] 7.2 Implement AccountCreatedConsumer infrastructure component
    - Create `seller/infrastructure/consumers/AccountCreatedConsumer.kt`
    - Add `@Component` and `@EventListener` annotations
    - Inject HandleAccountCreated use case
    - Delegate event handling to the use case
    - _Requirements: 6.4_

  - [x] 7.3 Write property tests for event handler
    - **Property 5: Event Handler Role-Based Creation**
    - **Property 6: Default Store Name Generation**
    - **Property 7: Event Handler Idempotency**
    - **Validates: Requirements 6.2, 6.3, 6.5, 6.6**

- [x] 8. Add Spring Modulith verification
  - [x] 8.1 Create modulith architecture verification test
    - Create `ModulithArchitectureTest.kt` in test package
    - Use `ApplicationModules.of().verify()` to validate boundaries
    - Ensure seller module passes verification
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

- [x] 9. Final checkpoint - Verify complete implementation
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- All tasks including tests are required for comprehensive coverage
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- Property tests validate universal correctness properties
- Unit tests validate specific examples and edge cases
- The seller module only references accountId as Long primitive to maintain module boundaries
