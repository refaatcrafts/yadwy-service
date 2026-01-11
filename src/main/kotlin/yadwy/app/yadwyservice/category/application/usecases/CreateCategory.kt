package yadwy.app.yadwyservice.category.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.category.application.models.CreateCategoryRequest
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.models.Category
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class CreateCategory(
    private val categoryRepository: CategoryRepository
) : UseCase<CreateCategoryRequest, CategoryResponse>() {

    override fun execute(request: CreateCategoryRequest): CategoryResponse {
        val category = Category.create(
            name = request.name,
            slug = request.slug,
            imageUrl = request.imageUrl,
            description = request.description,
            parentId = request.parentId,
            parentExists = { categoryRepository.existsById(it) },
            slugExists = { categoryRepository.findBySlug(it.lowercase()) != null }
        )
        val saved = categoryRepository.save(category)

        return saved.toResponse()
    }
}
