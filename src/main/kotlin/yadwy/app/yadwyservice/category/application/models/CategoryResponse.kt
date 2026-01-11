package yadwy.app.yadwyservice.category.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

data class CategoryResponse(
    val id: Long,
    val name: Localized,
    val slug: String,
    val imageUrl: String?,
    val description: Localized?,
    val parentId: Long?,
    val children: List<CategoryResponse>? = null
)
