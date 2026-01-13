package yadwy.app.yadwyservice.category.api

import app.yadwy.api.GetCategoryApi
import app.yadwy.model.CategoryResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import yadwy.app.yadwyservice.category.application.usecases.GetCategory

@RestController
class GetCategoryController(
    private val getCategory: GetCategory
) : GetCategoryApi {

    override fun getCategory(id: Long): ResponseEntity<CategoryResponseDto> {
        val response = getCategory.execute(id)
        return ResponseEntity.ok(response.toDto())
    }
}
