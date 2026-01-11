package yadwy.app.yadwyservice.category.domain.contracts

import yadwy.app.yadwyservice.category.domain.models.Category

interface CategoryRepository {
    fun save(category: Category): Category
    fun findById(categoryId: Long): Category?
    fun findBySlug(slug: String): Category?
    fun findAll(): List<Category>
    fun findByParentId(parentId: Long?): List<Category>
    fun delete(categoryId: Long)
    fun existsById(categoryId: Long): Boolean
}
