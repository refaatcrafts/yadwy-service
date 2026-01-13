package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.ListProductsRequest
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class ListProducts(
    private val productRepository: ProductRepository
) : UseCase<ListProductsRequest, List<ProductResponse>>() {

    override fun execute(request: ListProductsRequest): List<ProductResponse> {
        val products = when {
            request.sellerId != null -> productRepository.findBySellerId(request.sellerId)
            request.categoryId != null && request.visibleOnly -> 
                productRepository.findVisibleByCategoryId(request.categoryId)
            request.categoryId != null -> productRepository.findByCategoryId(request.categoryId)
            request.visibleOnly -> productRepository.findVisibleProducts()
            else -> productRepository.findVisibleProducts()
        }

        return products.map { it.toResponse() }
    }
}
