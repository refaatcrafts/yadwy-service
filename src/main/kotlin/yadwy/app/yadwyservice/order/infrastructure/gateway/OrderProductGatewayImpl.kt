package yadwy.app.yadwyservice.order.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.order.domain.contracts.ProductDetailsDto
import yadwy.app.yadwyservice.order.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.product.api.ProductAPI

@Service
class OrderProductGatewayImpl(
    private val productAPI: ProductAPI
) : ProductGateway {

    override fun getProductDetails(productId: Long): ProductDetailsDto? {
        val dto = productAPI.getProductDetails(productId) ?: return null
        return ProductDetailsDto(
            productId = dto.productId,
            sellerId = dto.sellerId,
            name = dto.name,
            price = dto.price,
            stock = dto.stock
        )
    }

    override fun decrementStock(productId: Long, quantity: Int) {
        productAPI.decrementStock(productId, quantity)
    }

    override fun existsById(productId: Long): Boolean {
        return productAPI.existsById(productId)
    }
}
