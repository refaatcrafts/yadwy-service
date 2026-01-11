package yadwy.app.yadwyservice.category.infrastructure.database.dbo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Sequence
import org.springframework.data.relational.core.mapping.Table
import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

@Table("categories", schema = "category")
data class CategoryDbo(
    @Id
    @Sequence("categories_id_seq", schema = "category")
    val id: Long? = null,
    val name: Localized,
    val slug: String,
    val imageUrl: String?,
    val description: Localized?,
    val parentId: Long?
)
