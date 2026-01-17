package yadwy.app.yadwyservice.sharedkernel.domain.models.base

/**
 * Marker interface for integration events used in cross-module communication.
 *
 * Integration events differ from domain events:
 * - Domain events are internal to a module
 * - Integration events cross module boundaries
 * - Integration events contain only primitive types (Long, String, Int)
 *
 * This interface does NOT extend DomainEvent to maintain clear separation
 * between internal domain concerns and external module communication.
 */
interface IntegrationEvent
