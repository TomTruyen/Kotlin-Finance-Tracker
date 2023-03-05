package com.tomtruyen.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)
