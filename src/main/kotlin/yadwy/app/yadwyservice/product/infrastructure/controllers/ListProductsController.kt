package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.api.ListProductsApi
import app.yadwy.model.ProductResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.product.application.models.ListProductsRequest
import yadwy.app.yadwyservice.product.application.usecases.ListProducts

@RestController
class ListProductsController(
    private val listProducts: ListProducts
) : ListProductsApi {

    override fun listProducts(
        sellerId: Long?,
        categoryId: Long?,
        visible: Boolean?
    ): ResponseEntity<List<ProductResponseDto>> {
        val request = ListProductsRequest(
            sellerId = sellerId,
            categoryId = categoryId,
            visibleOnly = visible ?: false
        )

        val response = listProducts.execute(request)

        return ResponseEntity.ok(response.map { it.toDto() })
    }
}
