package yadwy.app.yadwyservice.seller.infrastructure.database.dao

import org.springframework.data.repository.ListCrudRepository
import yadwy.app.yadwyservice.seller.infrastructure.database.dbo.SellerDbo

interface SellerDao : ListCrudRepository<SellerDbo, Long> {
    fun findByAccountId(accountId: Long): SellerDbo?
}
