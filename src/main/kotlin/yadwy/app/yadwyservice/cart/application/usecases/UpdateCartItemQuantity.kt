package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.application.models.UpdateCartItemQuantityRequest
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.cart.domain.exceptions.CartNotFoundException
import yadwy.app.yadwyservice.cart.domain.exceptions.InsufficientStockException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class UpdateCartItemQuantity(
    private val cartRepository: CartRepository,
    private val productGateway: ProductGateway
) : UseCase<UpdateCartItemQuantityRequest, CartResponse>() {

    override fun execute(request: UpdateCartItemQuantityRequest): CartResponse {
        val cart = cartRepository.findByAccountId(request.accountId)
            ?: throw CartNotFoundException(request.accountId)

        // Validate stock if quantity > 0
        if (request.quantity > 0) {
            val availableStock = productGateway.getAvailableStock(request.productId) ?: 0
            if (request.quantity > availableStock) {
                throw InsufficientStockException(request.productId, request.quantity, availableStock)
            }
        }

        cart.updateItemQuantity(request.productId, request.quantity)
        val saved = cartRepository.save(cart)
        return saved.toResponse()
    }
}
