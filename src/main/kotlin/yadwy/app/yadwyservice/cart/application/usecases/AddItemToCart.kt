package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.AddItemToCartRequest
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.cart.domain.exceptions.ProductNotFoundException
import yadwy.app.yadwyservice.cart.domain.models.Cart
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Component
class AddItemToCart(
    private val cartRepository: CartRepository,
    private val productGateway: ProductGateway
) : UseCase<AddItemToCartRequest, CartResponse>() {

    override fun execute(request: AddItemToCartRequest): CartResponse {
        if (!productGateway.productExists(request.productId)) {
            throw ProductNotFoundException(request.productId)
        }

        val unitPrice = productGateway.getProductPrice(request.productId)
            ?: throw ProductNotFoundException(request.productId)

        val cart = cartRepository.findByAccountId(request.accountId)
            ?: Cart.create(request.accountId)

        cart.addItem(
            productId = request.productId,
            quantity = Quantity.of(request.quantity),
            unitPrice = unitPrice,
            getAvailableStock = { productGateway.getAvailableStock(it) ?: 0 }
        )

        return cartRepository.save(cart).toResponse()
    }
}
