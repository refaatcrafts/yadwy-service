/**
 * Core domain building blocks following Domain-Driven Design (DDD).
 *
 * This file defines base abstractions used across the domain layer,
 * such as entities, value objects, aggregate roots, and domain events.
 *
 * These types enforce consistency and express domain intent,
 * without containing business logic themselves.
 */
package yadwy.app.yadwyservice.sharedkernel.domain.models.base

/**
 * Marker interface for all domain model types.
 */
interface DomainModel

/**
 * Represents a domain event that occurred within the domain.
 *
 * Domain events are immutable facts describing something
 * that already happened in the business.
 */
interface DomainEvent : DomainModel

/**
 * Represents an immutable value object identified by its properties.
 */
interface ValueObject : DomainModel

/**
 * Base class for entities.
 *
 * Entities are identified by their identity, not by their attributes.
 * Equality is based solely on the entity ID.
 */
open class Entity<out T : ValueObject>(val id: T) : DomainModel {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other.javaClass != javaClass) return false

        other as Entity<*>
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/**
 * Base class for aggregate roots.
 *
 * An aggregate root is the entry point to an aggregate
 * and is responsible for raising domain events.
 */
abstract class AggregateRoot<out T : ValueObject, U : DomainEvent>(
    id: T
) : Entity<T>(id) {

    private val occurredEvents: MutableList<U> = mutableListOf()

    /**
     * Returns and clears all domain events raised by this aggregate.
     */
    fun occurredEvents(): List<U> {
        val events = occurredEvents.toMutableList()
        occurredEvents.clear()
        return events
    }

    /**
     * Raises a new domain event.
     */
    protected fun raiseEvent(event: U) {
        occurredEvents.add(event)
    }
}
