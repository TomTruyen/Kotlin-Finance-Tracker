package com.tomtruyen.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tomtruyen.models.User

object AuthUtils {
    val secret = Algorithm.HMAC256("secret")

    const val expirationTime = 86400000L // 1 day

    fun generateToken(user: User): String {
        return JWT.create().withClaim("email", user.email).sign(secret)
    }
}