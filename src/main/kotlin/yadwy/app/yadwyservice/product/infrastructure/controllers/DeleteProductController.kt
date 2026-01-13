package yadwy.app.yadwyservice.product.infrastructure.controllers

import app.yadwy.api.DeleteProductApi
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.product.application.models.DeleteProductRequest
import yadwy.app.yadwyservice.product.application.usecases.DeleteProduct
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.product.domain.exceptions.SellerNotFoundException

@RestController
class DeleteProductController(
    private val deleteProduct: DeleteProduct,
    private val sellerGateway: SellerGateway
) : DeleteProductApi {

    override fun deleteProduct(id: Long): ResponseEntity<Unit> {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
        val accountId = jwt.getClaim<Long>("id")
        val seller = sellerGateway.findSellerIdByAccountId(accountId)
            ?: throw SellerNotFoundException(accountId)

        val request = DeleteProductRequest(
            productId = id,
            sellerId = seller.id
        )

        deleteProduct.execute(request)

        return ResponseEntity.noContent().build()
    }
}
