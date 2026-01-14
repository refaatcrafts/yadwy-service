package yadwy.app.yadwyservice.cart.infrastructure.api

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.cart.api.CartAPI
import yadwy.app.yadwyservice.cart.api.CartDto
import yadwy.app.yadwyservice.cart.api.CartItemDto
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository

@Service
class CartAPIImpl(
    private val cartRepository: CartRepository
) : CartAPI {

    override fun getCartByAccountId(accountId: Long): CartDto? {
        val cart = cartRepository.findByAccountId(accountId) ?: return null
        return CartDto(
            id = cart.getId().id,
            accountId = cart.getAccountId(),
            items = cart.getItems().map { item ->
                CartItemDto(
                    id = item.getId().id,
                    productId = item.getProductId(),
                    quantity = item.getQuantity(),
                    unitPrice = item.getUnitPrice(),
                    subtotal = item.getSubtotal()
                )
            },
            total = cart.calculateTotal()
        )
    }

    override fun clearCart(accountId: Long) {
        val cart = cartRepository.findByAccountId(accountId) ?: return
        cart.clear()
        cartRepository.save(cart)
    }
}
