package yadwy.app.yadwyservice.product.infrastructure.gateway

import org.springframework.stereotype.Service
import yadwy.app.yadwyservice.product.domain.contracts.SellerGateway
import yadwy.app.yadwyservice.seller.api.SellerAPI
import yadwy.app.yadwyservice.seller.api.SellerDto

@Service
class SellerGatewayImpl(
    private val sellerAPI: SellerAPI
) : SellerGateway {

    override fun findSellerIdByAccountId(accountId: Long): SellerDto? {
        return sellerAPI.findSellerByAccountId(accountId)
    }

    override fun sellerExists(sellerId: Long): Boolean {
        return sellerAPI.sellerExists(sellerId)
    }
}
