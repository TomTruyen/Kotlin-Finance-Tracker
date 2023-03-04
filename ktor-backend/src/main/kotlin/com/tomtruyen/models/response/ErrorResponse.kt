package com.tomtruyen.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String?
)
