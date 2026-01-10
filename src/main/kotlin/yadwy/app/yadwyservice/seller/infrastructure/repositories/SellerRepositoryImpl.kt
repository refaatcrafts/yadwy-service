package yadwy.app.yadwyservice.seller.infrastructure.repositories

import org.springframework.stereotype.Component
import yadwy.app.yadwyservice.seller.domain.contracts.SellerRepository
import yadwy.app.yadwyservice.seller.domain.models.Seller
import yadwy.app.yadwyservice.seller.domain.models.SellerId
import yadwy.app.yadwyservice.seller.domain.models.StoreName
import yadwy.app.yadwyservice.seller.infrastructure.database.dao.SellerDao
import yadwy.app.yadwyservice.seller.infrastructure.database.dbo.SellerDbo

@Component
class SellerRepositoryImpl(
    private val sellerDao: SellerDao,
) : SellerRepository {

    override fun save(seller: Seller): Seller {
        val savedDbo = sellerDao.save(
            SellerDbo(
                accountId = seller.getAccountId(),
                storeName = seller.getStoreName().value
            )
        )
        return savedDbo.toDomain()
    }

    override fun findById(sellerId: Long): Seller? {
        return sellerDao.findById(sellerId).orElse(null)?.toDomain()
    }

    override fun findByAccountId(accountId: Long): Seller? {
        return sellerDao.findByAccountId(accountId)?.toDomain()
    }

    private fun SellerDbo.toDomain(): Seller = Seller(
        sellerId = SellerId(id!!),
        accountId = accountId,
        storeName = StoreName(storeName)
    )
}
