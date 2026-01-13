package yadwy.app.yadwyservice.category.api

interface CategoryAPI {
    fun existsById(categoryId: Long): Boolean
}