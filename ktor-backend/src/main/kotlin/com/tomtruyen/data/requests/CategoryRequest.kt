package com.tomtruyen.data.requests

import com.tomtruyen.data.model.CategoryType
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val name: String,
    val type: String,
) {
    fun validate() {
        if(name.isBlank()) {
            throw IllegalArgumentException("Name cannot be blank")
        }

        if(type.isBlank()) {
            throw IllegalArgumentException("Type cannot be blank")
        }

        if(CategoryType.values().none { it.type == type }) {
            throw IllegalArgumentException("Type is invalid. Options: income, expense")
        }
    }
}