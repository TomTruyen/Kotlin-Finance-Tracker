package com.tomtruyen.security.token

data class TokenClaim(
    val name: String,
    val value: String
)

const val USER_ID_CLAIM = "userId"
