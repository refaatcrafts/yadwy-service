package yadwy.app.yadwyservice.cart.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.cart.domain.contracts.CartRepository
import yadwy.app.yadwyservice.cart.domain.models.Cart
import yadwy.app.yadwyservice.cart.domain.models.CartId
import yadwy.app.yadwyservice.cart.domain.models.CartItem
import yadwy.app.yadwyservice.cart.domain.models.CartItemId
import yadwy.app.yadwyservice.cart.infrastructure.database.dao.CartDao
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartDbo
import yadwy.app.yadwyservice.cart.infrastructure.database.dbo.CartItemDbo
import yadwy.app.yadwyservice.sharedkernel.domain.models.Amount
import yadwy.app.yadwyservice.sharedkernel.domain.models.Quantity

@Component
class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun save(cart: Cart): Cart {
        val cartDbo = CartDbo(
            id = if (cart.getId().id == 0L) null else cart.getId().id,
            accountId = cart.getAccountId(),
            items = cart.getItems().map { item ->
                CartItemDbo(
                    id = if (item.getId().id == 0L) null else item.getId().id,
                    productId = item.getProductId(),
                    quantity = item.getQuantity().value,
                    unitPrice = item.getUnitPrice().value
                )
            }.toSet()
        )
        val saved = cartDao.save(cartDbo)
        return saved.toDomain()
    }

    override fun findByAccountId(accountId: Long): Cart? {
        return cartDao.findByAccountId(accountId)?.toDomain()
    }

    override fun findById(cartId: Long): Cart? {
        return cartDao.findById(cartId).orElse(null)?.toDomain()
    }

    override fun existsByAccountId(accountId: Long): Boolean {
        return cartDao.existsByAccountId(accountId)
    }

    private fun CartDbo.toDomain(): Cart = Cart.reconstitute(
        cartId = CartId(id!!),
        accountId = accountId,
        items = items.map { it.toDomain() }.toMutableList()
    )

    private fun CartItemDbo.toDomain(): CartItem = CartItem.reconstitute(
        cartItemId = CartItemId(id!!),
        productId = productId,
        quantity = Quantity.of(quantity),
        unitPrice = Amount.of(unitPrice)
    )
}
