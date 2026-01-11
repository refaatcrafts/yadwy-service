package yadwy.app.yadwyservice.category.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.category.application.models.CategoryResponse
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.category.domain.models.Category
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

data class ListCategoriesRequest(
    val parentId: Long? = null,
    val includeChildren: Boolean = false
)

@Component
class ListCategories(
    private val categoryRepository: CategoryRepository
) : UseCase<ListCategoriesRequest, List<CategoryResponse>>() {

    override fun execute(request: ListCategoriesRequest): List<CategoryResponse> {
        return if (request.includeChildren) {
            buildTree()
        } else {
            // When parentId is null, returns root categories (categories with no parent)
            // When parentId is provided, returns direct children of that category
            categoryRepository.findByParentId(request.parentId).map { it.toResponse() }
        }
    }

    private fun buildTree(): List<CategoryResponse> {
        val allCategories = categoryRepository.findAll()
        val childrenMap = allCategories.groupBy { it.getParentId() }
        return buildTreeRecursive(null, childrenMap)
    }

    private fun buildTreeRecursive(
        parentId: Long?,
        childrenMap: Map<Long?, List<Category>>
    ): List<CategoryResponse> {
        val children = childrenMap[parentId] ?: emptyList()
        return children.map { category ->
            CategoryResponse(
                id = category.getId().id,
                name = category.getName(),
                slug = category.getSlug(),
                imageUrl = category.getImageUrl(),
                description = category.getDescription(),
                parentId = category.getParentId(),
                children = buildTreeRecursive(category.getId().id, childrenMap)
            )
        }
    }
}
