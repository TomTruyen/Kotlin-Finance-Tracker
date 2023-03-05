package com.tomtruyen.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService: TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        val token = JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))

        claims.forEach { claim ->
            token.withClaim(claim.name, claim.value)
        }

        return token.sign(Algorithm.HMAC256(config.secret))
    }
}