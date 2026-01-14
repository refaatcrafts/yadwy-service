package yadwy.app.yadwyservice.cart.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.cart.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.product.api.ProductAPI
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Service
class ProductGatewayImpl(
    private val productAPI: ProductAPI
) : ProductGateway {

    override fun productExists(productId: Long): Boolean {
        return productAPI.existsById(productId)
    }

    override fun getProductPrice(productId: Long): Amount? {
        return productAPI.getPrice(productId)
    }

    override fun getAvailableStock(productId: Long): Int? {
        return productAPI.getStock(productId)
    }
}
