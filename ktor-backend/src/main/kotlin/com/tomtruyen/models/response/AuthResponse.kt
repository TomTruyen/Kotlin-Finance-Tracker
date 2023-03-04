package com.tomtruyen.models.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
