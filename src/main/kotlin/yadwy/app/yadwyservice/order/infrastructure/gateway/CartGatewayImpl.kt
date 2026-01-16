package yadwy.app.yadwyservice.order.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.cart.api.CartAPI
import yadwy.app.yadwyservice.order.domain.contracts.CartGateway
import yadwy.app.yadwyservice.order.domain.contracts.ProductGateway
import yadwy.app.yadwyservice.order.domain.models.CartItem
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Service
class CartGatewayImpl(
    private val cartAPI: CartAPI,
    private val productGateway: ProductGateway
) : CartGateway {

    override fun getCartItems(accountId: Long): List<CartItem> {
        val cart = cartAPI.getCartByAccountId(accountId) ?: return emptyList()
        
        return cart.items.mapNotNull { cartItemDto ->
            val productDetails = productGateway.getProductDetails(cartItemDto.productId)
                ?: return@mapNotNull null
            
            CartItem(
                productId = productDetails.productId,
                sellerId = productDetails.sellerId,
                productName = productDetails.name,
                unitPrice = productDetails.price,
                quantity = Quantity.of(cartItemDto.quantity),
                stock = productDetails.stock
            )
        }
    }

    override fun clearCart(accountId: Long) {
        cartAPI.clearCart(accountId)
    }
}
