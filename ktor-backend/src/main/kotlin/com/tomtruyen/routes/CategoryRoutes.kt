package com.tomtruyen.routes

import com.tomtruyen.data.model.Category
import com.tomtruyen.data.model.CategoryType
import com.tomtruyen.data.requests.CategoryRequest
import com.tomtruyen.data.responses.ErrorResponse
import com.tomtruyen.data.table.CategoryTable
import com.tomtruyen.repositories.CategoryRepository
import com.tomtruyen.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createCategory() {
    post("categories") {
        val request = call.receiveNullable<CategoryRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if(request.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category name cannot be empty"))
            return@post
        }

        // Check if CategoryType enum contains the type
        val isTypeValid = CategoryType.values().any { it.type == request.type }
        if(!isTypeValid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid category type"))
            return@post
        }

        val userId = call.getUserId()
        val category = Category(
            name = request.name,
            type = request.type,
        )
        CategoryRepository.createCategory(category, userId).let {
            it.resultedValues?.firstOrNull()?.let { row ->
                category.id = row[CategoryTable.id]
            }
        }

        call.respond(HttpStatusCode.OK, category)
    }
}

fun Route.updateCategory() {
    put("categories/{id}") {
        val request = call.receiveNullable<CategoryRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }

        val categoryId = call.parameters["id"]
        if(categoryId.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category id is required"))
            return@put
        }

        if(request.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category name cannot be empty"))
            return@put
        }

        // Check if CategoryType enum contains the type
        val isTypeValid = CategoryType.values().any { it.type == request.type }
        if(!isTypeValid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid category type"))
            return@put
        }

        val category = Category(
            id = UUID.fromString(categoryId),
            name = request.name,
            type = request.type,
        )
        CategoryRepository.updateCategory(category)

        call.respond(HttpStatusCode.OK, category)
    }
}

fun Route.deleteCategory() {
    delete("categories/{id}") {
        val categoryId = call.parameters["id"]

        if(categoryId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category id is required"))
            return@delete
        }

        CategoryRepository.deleteCategory(UUID.fromString(categoryId))

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.getCategories() {
    get("categories") {
        val userId = call.getUserId()
        val type = call.request.queryParameters["type"]

        if(type.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Category type is required"))
            return@get
        }

        val isTypeValid = CategoryType.values().any { it.type == type }
        if(!isTypeValid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid category type"))
            return@get
        }

        val categories = CategoryRepository.findAllByType(
            userId = userId,
            type = type
        )

        call.respond(HttpStatusCode.OK, categories)
    }
}