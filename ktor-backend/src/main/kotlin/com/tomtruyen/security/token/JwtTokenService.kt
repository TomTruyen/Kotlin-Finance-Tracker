package com.tomtruyen.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tomtruyen.repositories.TokenRepository
import com.tomtruyen.security.hashing.SHA256HashingService
import java.util.*

class JwtTokenService: TokenService {
    override suspend fun generate(config: TokenConfig, userId: UUID, isUpdate: Boolean): TokenPair {
        val accessToken = JWT.create()
            .withSubject(userId.toString())
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
            .sign(Algorithm.HMAC256(config.secret))

        val refreshToken = SHA256HashingService.generateRandomHash()

        val tokenPair = TokenPair(accessToken, refreshToken)

        if(!isUpdate) {
            TokenRepository.insertToken(
                tokenPair = tokenPair,
                userId = userId,
                expiresAt = System.currentTimeMillis() + config.refreshExpiresIn
            )
        }

        return tokenPair
    }
}