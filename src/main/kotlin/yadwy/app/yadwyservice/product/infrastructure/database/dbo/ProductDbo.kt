package yadwy.app.yadwyservice.product.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Sequence
import org.springframework.data.relational.core.mapping.Table
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized
import java.math.BigDecimal

@Table("products", schema = "product")
data class ProductDbo(
    @Id
    @Sequence("products_id_seq", schema = "product")
    val id: Long? = null,
    val sellerId: Long,
    val name: Localized,
    val description: Localized?,
    val images: Array<String>,
    val price: BigDecimal,
    val compareAtPrice: BigDecimal?,
    val categoryId: Long,
    val stock: Int,
    val trackInventory: Boolean,
    val visible: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductDbo

        if (id != other.id) return false
        if (sellerId != other.sellerId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (!images.contentEquals(other.images)) return false
        if (price != other.price) return false
        if (compareAtPrice != other.compareAtPrice) return false
        if (categoryId != other.categoryId) return false
        if (stock != other.stock) return false
        if (trackInventory != other.trackInventory) return false
        if (visible != other.visible) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + sellerId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + images.contentHashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + (compareAtPrice?.hashCode() ?: 0)
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + stock
        result = 31 * result + trackInventory.hashCode()
        result = 31 * result + visible.hashCode()
        return result
    }
}
