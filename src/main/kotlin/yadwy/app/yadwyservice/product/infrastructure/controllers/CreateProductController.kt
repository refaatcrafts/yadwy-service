package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.api.CreateProductApi
import app.yadwy.model.CreateProductRequestDto
import app.yadwy.model.ProductResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.product.application.models.CreateProductRequest
import yadwy.app.yadwyservice.product.application.usecases.CreateProduct
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.product.domain.exceptions.SellerNotFoundException
import java.math.BigDecimal

@RestController
class CreateProductController(
    private val createProduct: CreateProduct,
    private val sellerGateway: SellerGateway
) : CreateProductApi {

    override fun createProduct(
        createProductRequestDto: CreateProductRequestDto
    ): ResponseEntity<ProductResponseDto> {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        val accountId = jwt.getClaim<Long>("id")
        val seller = sellerGateway.findSellerIdByAccountId(accountId)
            ?: throw SellerNotFoundException(accountId)

        val request = CreateProductRequest(
            sellerId = seller.id,
            name = createProductRequestDto.name.toDomain(),
            description = createProductRequestDto.description?.toDomain(),
            images = createProductRequestDto.images ?: emptyList(),
            price = BigDecimal.valueOf(createProductRequestDto.price),
            compareAtPrice = createProductRequestDto.compareAtPrice?.let { BigDecimal.valueOf(it) },
            categoryId = createProductRequestDto.categoryId,
            stock = createProductRequestDto.stock ?: 0,
            trackInventory = createProductRequestDto.trackInventory ?: true,
            visible = createProductRequestDto.visible ?: false
        )

        val response = createProduct.execute(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response.toDto())
    }
}
