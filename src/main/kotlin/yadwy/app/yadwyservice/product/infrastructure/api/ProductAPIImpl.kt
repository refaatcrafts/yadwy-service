package yadwy.app.yadwyservice.product.infrastructure.api

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.product.api.ProductAPI
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import java.math.BigDecimal

@Service
class ProductAPIImpl(
    private val productRepository: ProductRepository
) : ProductAPI {

    override fun existsById(productId: Long): Boolean {
        return productRepository.existsById(productId)
    }

    override fun getPrice(productId: Long): BigDecimal? {
        return productRepository.findById(productId)?.getPrice()?.value
    }

    override fun getStock(productId: Long): Int? {
        return productRepository.findById(productId)?.getStock()
    }
}
