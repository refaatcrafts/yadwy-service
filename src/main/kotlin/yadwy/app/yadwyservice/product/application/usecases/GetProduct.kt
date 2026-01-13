package yadwy.app.yadwyservice.product.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.product.application.models.ProductResponse
import yadwy.app.yadwyservice.product.domain.contracts.ProductRepository
import yadwy.app.yadwyservice.product.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class GetProduct(
    private val productRepository: ProductRepository
) : UseCase<Long, ProductResponse>() {

    override fun execute(request: Long): ProductResponse {
        val product = productRepository.findById(request)
            ?: throw ProductNotFoundException(request)

        return product.toResponse()
    }
}
