package com.tomtruyen.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val name: String,
    val type: String,
)