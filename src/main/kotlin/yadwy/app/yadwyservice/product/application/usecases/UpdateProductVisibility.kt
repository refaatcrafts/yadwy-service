package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.application.models.UpdateProductVisibilityRequest
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotOwnedException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class UpdateProductVisibility(
    private val productRepository: ProductRepository
) : UseCase<UpdateProductVisibilityRequest, ProductResponse>() {

    override fun execute(request: UpdateProductVisibilityRequest): ProductResponse {
        val product = productRepository.findById(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        if (product.getSellerId() != request.sellerId) {
            throw ProductNotOwnedException(request.productId, request.sellerId)
        }

        product.setVisibility(request.visible)

        val saved = productRepository.save(product)
        return saved.toResponse()
    }
}
