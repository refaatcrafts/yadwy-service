package yadwy.app.yadwyservice.category.application.models

import yadwy.app.yadwyservice.sharedkernel.domain.models.Localized

data class UpdateCategoryRequest(
    val categoryId: Long,
    val name: Localized? = null,
    val imageUrl: String? = null,
    val description: Localized? = null
)
