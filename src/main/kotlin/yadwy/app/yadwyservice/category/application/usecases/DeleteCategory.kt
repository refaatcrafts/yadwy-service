package yadwy.app.yadwyservice.category.application.usecases

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.events.CategoryDeletedEvent
import yadwy.app.yadwyservice.category.domain.exceptions.CategoryHasChildrenException
import yadwy.app.yadwyservice.category.domain.exceptions.CategoryNotFoundException
import yadwy.app.yadwyservice.category.domain.models.CategoryId
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class DeleteCategory(
    private val categoryRepository: CategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) : UseCase<Long, Unit>() {

    override fun execute(request: Long) {
        if (!categoryRepository.existsById(request)) {
            throw CategoryNotFoundException(request)
        }

        val children = categoryRepository.findByParentId(request)
        if (children.isNotEmpty()) {
            throw CategoryHasChildrenException(request)
        }

        categoryRepository.delete(request)
        eventPublisher.publishEvent(CategoryDeletedEvent(CategoryId(request)))
    }
}
