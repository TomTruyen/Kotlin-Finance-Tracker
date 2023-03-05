package com.tomtruyen.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val refreshExpiresIn: Long,
    val secret: String
)
