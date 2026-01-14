package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.CreateProductRequest
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.domain.contracts.CategoryGateway
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.product.domain.models.Product
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Component
class CreateProduct(
    private val productRepository: ProductRepository,
    private val sellerGateway: SellerGateway,
    private val categoryGateway: CategoryGateway
) : UseCase<CreateProductRequest, ProductResponse>() {

    override fun execute(request: CreateProductRequest): ProductResponse {
        val product = Product.create(
            sellerId = request.sellerId,
            name = request.name,
            description = request.description,
            images = request.images,
            price = Amount.of(request.price),
            compareAtPrice = request.compareAtPrice?.let { Amount.of(it) },
            categoryId = request.categoryId,
            stock = request.stock,
            trackInventory = request.trackInventory,
            visible = request.visible,
            sellerExists = { sellerGateway.sellerExists(it) },
            categoryExists = { categoryGateway.categoryExists(it) }
        )

        val saved = productRepository.save(product)
        return saved.toResponse()
    }
}
