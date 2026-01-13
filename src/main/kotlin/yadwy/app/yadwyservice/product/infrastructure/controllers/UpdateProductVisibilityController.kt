package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.api.UpdateProductVisibilityApi
import app.yadwy.model.ProductResponseDto
import app.yadwy.model.UpdateProductVisibilityRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.product.application.models.UpdateProductVisibilityRequest
import yadwy.app.yadwyservice.product.application.usecases.UpdateProductVisibility
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.product.domain.exceptions.SellerNotFoundException

@RestController
class UpdateProductVisibilityController(
    private val updateProductVisibility: UpdateProductVisibility,
    private val sellerGateway: SellerGateway
) : UpdateProductVisibilityApi {

    override fun updateProductVisibility(
        id: Long,
        updateProductVisibilityRequestDto: UpdateProductVisibilityRequestDto
    ): ResponseEntity<ProductResponseDto> {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        val accountId = jwt.getClaim<Long>("id")
        val seller = sellerGateway.findSellerIdByAccountId(accountId)
            ?: throw SellerNotFoundException(accountId)

        val request = UpdateProductVisibilityRequest(
            productId = id,
            sellerId = seller.id,
            visible = updateProductVisibilityRequestDto.visible
        )

        val response = updateProductVisibility.execute(request)

        return ResponseEntity.ok(response.toDto())
    }
}
