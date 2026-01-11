package yadwy.app.yadwyservice.category.infrastructure.repositories

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.models.Category
import yadwy.app.yadwyservice.category.domain.models.CategoryId
import yadwy.app.yadwyservice.category.infrastructure.database.dao.CategoryDao
import yadwy.app.yadwyservice.category.infrastructure.database.dbo.CategoryDbo

@Component
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun save(category: Category): Category {
        val dbo = CategoryDbo(
            id = if (category.getId().id == 0L) null else category.getId().id,
            name = category.getName(),
            slug = category.getSlug(),
            imageUrl = category.getImageUrl(),
            description = category.getDescription(),
            parentId = category.getParentId()
        )
        val saved = categoryDao.save(dbo)

        return saved.toDomain()
    }

    override fun findById(categoryId: Long): Category? {
        return categoryDao.findById(categoryId).orElse(null)?.toDomain()
    }

    override fun findBySlug(slug: String): Category? {
        return categoryDao.findBySlug(slug)?.toDomain()
    }

    override fun findAll(): List<Category> {
        return categoryDao.findAll().map { it.toDomain() }
    }

    override fun findByParentId(parentId: Long?): List<Category> {
        return categoryDao.findByParentId(parentId).map { it.toDomain() }
    }

    override fun delete(categoryId: Long) {
        categoryDao.deleteById(categoryId)
    }

    override fun existsById(categoryId: Long): Boolean {
        return categoryDao.existsById(categoryId)
    }

    private fun CategoryDbo.toDomain(): Category = Category.reconstitute(
        categoryId = CategoryId(id!!),
        name = name,
        slug = slug,
        imageUrl = imageUrl,
        description = description,
        parentId = parentId
    )
}
