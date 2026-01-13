package yadwy.app.yadwyservice.category.infrastructure.controllers

import app.yadwy.api.ListCategoriesApi
import app.yadwy.model.CategoryResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.category.application.usecases.ListCategories
import yadwy.app.yadwyservice.category.application.usecases.ListCategoriesRequest

@RestController
class ListCategoriesController(
    private val listCategories: ListCategories
) : ListCategoriesApi {

    override fun listCategories(parentId: Long?, include: String?): ResponseEntity<List<CategoryResponseDto>> {
        val request = ListCategoriesRequest(
            parentId = parentId,
            includeChildren = include == "children"
        )
        val response = listCategories.execute(request)
        return ResponseEntity.ok(response.map { it.toDto() })
    }
}
