package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.application.models.UpdateCartItemQuantityRequest
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.cart.domain.exceptions.CartNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Component
class UpdateCartItemQuantity(
    private val cartRepository: CartRepository,
    private val productGateway: ProductGateway
) : UseCase<UpdateCartItemQuantityRequest, CartResponse>() {

    override fun execute(request: UpdateCartItemQuantityRequest): CartResponse {
        val cart = cartRepository.findByAccountId(request.accountId)
            ?: throw CartNotFoundException(request.accountId)

        cart.updateItemQuantity(
            productId = request.productId,
            quantity = Quantity.of(request.quantity),
            getAvailableStock = { productGateway.getAvailableStock(it) ?: 0 }
        )

        return cartRepository.save(cart).toResponse()
    }
}
