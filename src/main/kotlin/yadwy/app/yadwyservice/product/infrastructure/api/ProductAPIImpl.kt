package yadwy.app.yadwyservice.product.infrastructure.api

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.product.api.ProductAPI
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount

@Service
class ProductAPIImpl(
    private val productRepository: ProductRepository
) : ProductAPI {

    override fun existsById(productId: Long): Boolean {
        return productRepository.existsById(productId)
    }

    override fun getPrice(productId: Long): Amount? {
        return productRepository.findById(productId)?.getPrice()
    }

    override fun getStock(productId: Long): Int? {
        return productRepository.findById(productId)?.getStock()
    }
}
