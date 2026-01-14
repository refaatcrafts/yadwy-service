package yadwy.app.yadwyservice.cart.application.usecases

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.application.models.CartResponse
import yadwy.app.yadwyservice.cart.application.models.RemoveItemFromCartRequest
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.exceptions.CartNotFoundException
import yadwy.app.yadwyservice.sharedkernel.application.UseCase

@Component
class RemoveItemFromCart(
    private val cartRepository: CartRepository
) : UseCase<RemoveItemFromCartRequest, CartResponse>() {

    override fun execute(request: RemoveItemFromCartRequest): CartResponse {
        val cart = cartRepository.findByAccountId(request.accountId)
            ?: throw CartNotFoundException(request.accountId)

        cart.removeItem(request.productId)
        val saved = cartRepository.save(cart)
        return saved.toResponse()
    }
}
