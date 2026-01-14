package yadwy.app.yadwyservice.cart.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.models.Cart
import yadwy.app.yadwyservice.cart.domain.models.CartId
import yadwy.app.yadwyservice.cart.domain.models.CartItem
import yadwy.app.yadwyservice.cart.domain.models.CartItemId
import yadwy.app.yadwyservice.cart.infrastructure.database.dao.CartDao
import yadwy.app.yadwyservice.cart.infrastructure.database.dao.CartItemDao
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartDbo
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartItemDbo
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Component
class CartRepositoryImpl(
    private val cartDao: CartDao,
    private val cartItemDao: CartItemDao,
) : CartRepository {

    override fun save(cart: Cart): Cart {
        // Save cart
        val cartDbo = CartDbo(
            id = if (cart.getId().id == 0L) null else cart.getId().id,
            accountId = cart.getAccountId()
        )
        val savedCart = cartDao.save(cartDbo)
        val cartId = savedCart.id!!

        // Delete existing items and save new ones
        cartItemDao.deleteByCartId(cartId)
        val savedItems = cart.getItems().map { item ->
            val itemDbo = CartItemDbo(
                id = null,
                cartId = cartId,
                productId = item.getProductId(),
                quantity = item.getQuantity().value,
                unitPrice = item.getUnitPrice().value
            )
            cartItemDao.save(itemDbo)
        }

        return Cart.reconstitute(
            cartId = CartId(cartId),
            accountId = savedCart.accountId,
            items = savedItems.map { it.toDomain() }.toMutableList()
        )
    }

    override fun findByAccountId(accountId: Long): Cart? {
        val cartDbo = cartDao.findByAccountId(accountId)
        val items = cartItemDao.findByCartId(cartDbo?.id!!)
        return Cart.reconstitute(
            cartId = CartId(cartDbo.id),
            accountId = cartDbo.accountId,
            items = items.map { it.toDomain() }.toMutableList()
        )
    }

    override fun findById(cartId: Long): Cart? {
        val cartDbo = cartDao.findById(cartId).orElse(null) ?: return null
        val items = cartItemDao.findByCartId(cartDbo.id!!)
        return Cart.reconstitute(
            cartId = CartId(cartDbo.id),
            accountId = cartDbo.accountId,
            items = items.map { it.toDomain() }.toMutableList()
        )
    }

    override fun existsByAccountId(accountId: Long): Boolean {
        return cartDao.existsByAccountId(accountId)
    }

    private fun CartItemDbo.toDomain(): CartItem = CartItem.reconstitute(
        cartItemId = CartItemId(id!!),
        productId = productId,
        quantity = Quantity.of(quantity),
        unitPrice = Amount.of(unitPrice)
    )
}
