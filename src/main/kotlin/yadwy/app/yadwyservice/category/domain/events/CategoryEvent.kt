package yadwy.app.yadwyservice.category.domain.events

import yadwy.app.yadwyservice.category.domain.models.CategoryId
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.DomainEvent

sealed interface CategoryEvent : DomainEvent

data class CategoryCreatedEvent(
    val categoryId: CategoryId,
    val name: Localized,
    val slug: String,
    val parentId: Long?
) : CategoryEvent

data class CategoryUpdatedEvent(
    val categoryId: CategoryId
) : CategoryEvent

data class CategoryDeletedEvent(
    val categoryId: CategoryId
) : CategoryEvent
