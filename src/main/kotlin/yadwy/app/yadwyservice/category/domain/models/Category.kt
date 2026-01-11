package yadwy.app.yadwyservice.category.domain.models

import yadwy.app.yadwyservice.category.domain.events.CategoryCreatedEvent
import yadwy.app.yadwyservice.category.domain.events.CategoryEvent
import yadwy.app.yadwyservice.category.domain.events.CategoryUpdatedEvent
import yadwy.app.yadwyservice.category.domain.exceptions.InvalidSlugException
import yadwy.app.yadwyservice.category.domain.exceptions.ParentCategoryNotFoundException
import yadwy.app.yadwyservice.category.domain.exceptions.SlugAlreadyExistsException
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import yadwy.app.yadwyservice.sharedkernel.domain.models.base.AggregateRoot

class Category internal constructor(
    private val categoryId: CategoryId,
    private var name: Localized,
    private val slug: String,
    private var imageUrl: String?,
    private var description: Localized?,
    private val parentId: Long?
) : AggregateRoot<CategoryId, CategoryEvent>(id = categoryId) {

    init {
        require(slug.matches(SLUG_PATTERN)) { "Invalid slug format: $slug" }
    }

    companion object {
        private val SLUG_PATTERN = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")

        /**
         * Creates a new Category with all domain validations.
         * @param name Localized name (ar/en)
         * @param slug URL-friendly identifier
         * @param imageUrl Optional image URL
         * @param description Optional localized description
         * @param parentId Optional parent category ID
         * @param parentExists Function to check if parent exists (injected from repository)
         * @param slugExists Function to check if slug already exists (injected from repository)
         */
        fun create(
            name: Localized,
            slug: String,
            imageUrl: String? = null,
            description: Localized? = null,
            parentId: Long? = null,
            parentExists: (Long) -> Boolean = { true },
            slugExists: (String) -> Boolean = { false }
        ): Category {
            val normalizedSlug = slug.lowercase()

            // Domain validation: slug format
            if (!normalizedSlug.matches(SLUG_PATTERN)) {
                throw InvalidSlugException(slug)
            }

            // Domain validation: slug uniqueness
            if (slugExists(normalizedSlug)) {
                throw SlugAlreadyExistsException(slug)
            }

            // Domain validation: parent existence
            parentId?.let {
                if (!parentExists(it)) {
                    throw ParentCategoryNotFoundException(it)
                }
            }

            val category = Category(
                categoryId = CategoryId(0),
                name = name,
                slug = normalizedSlug,
                imageUrl = imageUrl,
                description = description,
                parentId = parentId
            )
            category.raiseEvent(
                CategoryCreatedEvent(category.categoryId, name, normalizedSlug, parentId)
            )
            return category
        }

        /**
         * Reconstitutes a Category from persistence (no validation, no events).
         */
        fun reconstitute(
            categoryId: CategoryId,
            name: Localized,
            slug: String,
            imageUrl: String?,
            description: Localized?,
            parentId: Long?
        ): Category = Category(categoryId, name, slug, imageUrl, description, parentId)
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
    fun getSlug() = slug
    fun getImageUrl() = imageUrl
    fun getDescription() = description
    fun getParentId() = parentId
}
