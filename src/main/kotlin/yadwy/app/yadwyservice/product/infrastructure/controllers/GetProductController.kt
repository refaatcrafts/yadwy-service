package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.api.GetProductApi
import app.yadwy.model.ProductResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.product.application.usecases.GetProduct

@RestController
class GetProductController(
    private val getProduct: GetProduct
) : GetProductApi {

    override fun getProduct(id: Long): ResponseEntity<ProductResponseDto> {
        val response = getProduct.execute(id)
        return ResponseEntity.ok(response.toDto())
    }
}
