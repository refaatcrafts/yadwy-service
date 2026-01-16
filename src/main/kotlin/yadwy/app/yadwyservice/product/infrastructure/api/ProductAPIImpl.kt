package yadwy.app.yadwyservice.product.infrastructure.api

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yadwy.app.yadwyservice.product.api.ProductAPI
import yadwy.app.yadwyservice.product.api.ProductDetailsDto
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

    override fun getProductDetails(productId: Long): ProductDetailsDto? {
        val product = productRepository.findById(productId) ?: return null
        return ProductDetailsDto(
            productId = product.getId().id,
            sellerId = product.getSellerId(),
            name = product.getName(),
            price = product.getPrice(),
            stock = product.getStock()
        )
    }

    @Transactional
    override fun decrementStock(productId: Long, quantity: Int) {
        val product = productRepository.findById(productId)
            ?: throw IllegalArgumentException("Product not found: $productId")
        product.decrementStock(quantity)
        productRepository.save(product)
    }
}
