package yadwy.app.yadwyservice.category.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

data class CreateCategoryRequest(
    val name: Localized,
    val slug: String,
    val imageUrl: String? = null,
    val description: Localized? = null,
    val parentId: Long? = null
)
