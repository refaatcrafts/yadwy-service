package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.application.models.UpdateProductRequest
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotOwnedException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Component
class UpdateProduct(
    private val productRepository: ProductRepository
) : UseCase<UpdateProductRequest, ProductResponse>() {

    override fun execute(request: UpdateProductRequest): ProductResponse {
        val product = productRepository.findById(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        if (product.getSellerId() != request.sellerId) {
            throw ProductNotOwnedException(request.productId, request.sellerId)
        }

        // Update details if provided
        if (request.name != null || request.description != null || request.images != null) {
            product.updateDetails(
                name = request.name,
                description = request.description,
                images = request.images
            )
        }

        // Update pricing if provided
        if (request.price != null || request.compareAtPrice != null) {
            product.updatePricing(
                price = request.price?.let { Amount.of(it) },
                compareAtPrice = request.compareAtPrice?.let { Amount.of(it) }
            )
        }

        // Update inventory if provided
        if (request.stock != null || request.trackInventory != null) {
            product.updateInventory(
                stock = request.stock,
                trackInventory = request.trackInventory
            )
        }

        // Update visibility if provided
        request.visible?.let { product.setVisibility(it) }

        val saved = productRepository.save(product)
        return saved.toResponse()
    }
}
