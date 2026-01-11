package yadwy.app.yadwyservice.category.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.category.infrastructure.database.dbo.CategoryDbo

interface CategoryDao : ListCrudRepository<CategoryDbo, Long> {
    fun findByParentId(parentId: Long?): List<CategoryDbo>
    fun findBySlug(slug: String): CategoryDbo?
}
