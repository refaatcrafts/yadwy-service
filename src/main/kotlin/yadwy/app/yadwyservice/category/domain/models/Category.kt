package yadwy.app.yadwyservice.category.domain.models

import yadwy.app.yadwyservice.category.domain.events.CategoryCreatedEvent
import yadwy.app.yadwyservice.category.domain.events.CategoryEvent
import yadwy.app.yadwyservice.category.domain.events.CategoryUpdatedEvent
import yadwy.app.yadwyservice.category.domain.exceptions.ParentCategoryNotFoundException
import yadwy.app.yadwyservice.category.domain.exceptions.SlugAlreadyExistsException
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot

class Category internal constructor(
    private val categoryId: CategoryId,
    private var name: Localized,
    private val slug: Slug,
    private var imageUrl: String?,
    private var description: Localized?,
    private val parentId: Long?
) : AggregateRoot<CategoryId, CategoryEvent>(id = categoryId) {

    companion object {
        fun create(
            name: Localized,
            slug: String,
            imageUrl: String? = null,
            description: Localized? = null,
            parentId: Long? = null,
            parentExists: (Long) -> Boolean = { true },
            slugExists: (String) -> Boolean = { false }
        ): Category {
            val validatedSlug = Slug.of(slug)

            if (slugExists(validatedSlug.value)) throw SlugAlreadyExistsException(slug)

            parentId?.let {
                if (!parentExists(it)) throw ParentCategoryNotFoundException(it)
            }

            val category = Category(
                categoryId = CategoryId(0),
                name = name,
                slug = validatedSlug,
                imageUrl = imageUrl,
                description = description,
                parentId = parentId
            )
            category.raiseEvent(
                CategoryCreatedEvent(category.categoryId, name, validatedSlug.value, parentId)
            )
            return category
        }

        fun reconstitute(
            categoryId: CategoryId,
            name: Localized,
            slug: String,
            imageUrl: String?,
            description: Localized?,
            parentId: Long?
        ): Category = Category(
            categoryId = categoryId,
            name = name,
            slug = Slug.of(slug),
            imageUrl = imageUrl,
            description = description,
            parentId = parentId
        )
    }

    fun update(
        name: Localized? = null,
        imageUrl: String? = null,
        description: Localized? = null
    ) {
        name?.let { this.name = it }
        imageUrl?.let { this.imageUrl = it }
        description?.let { this.description = it }
        raiseEvent(CategoryUpdatedEvent(categoryId))
    }

    fun getId() = categoryId
    fun getName() = name
    fun getSlug() = slug.value
    fun getImageUrl() = imageUrl
    fun getDescription() = description
    fun getParentId() = parentId
}
