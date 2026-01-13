package yadwy.app.yadwyservice.product.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.category.api.CategoryAPI
import yadwy.app.yadwyservice.category.domain.contracts.CategoryRepository
import yadwy.app.yadwyservice.product.domain.contracts.CategoryGateway

@Service
class CategoryGatewayImpl(
    private val categoryAPI: CategoryAPI
) : CategoryGateway {

    override fun categoryExists(categoryId: Long): Boolean {
        return categoryAPI.existsById(categoryId)
    }
}
