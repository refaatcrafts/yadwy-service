package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.DeleteProductRequest
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotOwnedException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class DeleteProduct(
    private val productRepository: ProductRepository
) : UseCase<DeleteProductRequest, Unit>() {

    override fun execute(request: DeleteProductRequest) {
        val product = productRepository.findById(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        if (product.getSellerId() != request.sellerId) {
            throw ProductNotOwnedException(request.productId, request.sellerId)
        }

        productRepository.delete(request.productId)
    }
}
