package yadwy.app.yadwyservice.category.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.exceptions.CategoryNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class GetCategory(
    private val categoryRepository: CategoryRepository
) : UseCase<Long, CategoryResponse>() {

    override fun execute(request: Long): CategoryResponse {
        val category = categoryRepository.findById(request)
            ?: throw CategoryNotFoundException(request)
        return category.toResponse()
    }
}
