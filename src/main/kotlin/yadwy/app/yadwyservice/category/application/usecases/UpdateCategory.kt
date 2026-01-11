package yadwy.app.yadwyservice.category.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.category.application.models.UpdateCategoryRequest
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.exceptions.CategoryNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class UpdateCategory(
    private val categoryRepository: CategoryRepository
) : UseCase<UpdateCategoryRequest, CategoryResponse>() {

    override fun execute(request: UpdateCategoryRequest): CategoryResponse {
        val category = categoryRepository.findById(request.categoryId)
            ?: throw CategoryNotFoundException(request.categoryId)

        category.update(
            name = request.name,
            imageUrl = request.imageUrl,
            description = request.description
        )
        val saved = categoryRepository.save(category)

        return saved.toResponse()
    }
}
