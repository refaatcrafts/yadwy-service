package yadwy.app.yadwyservice.category.infrastructure.api

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.api.CategoryAPI
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository

@Component
class CategoryAPIImpl(
    private val categoryRepository: CategoryRepository
) : CategoryAPI {
    override fun existsById(categoryId: Long): Boolean {
        return categoryRepository.existsById(categoryId)
    }
}