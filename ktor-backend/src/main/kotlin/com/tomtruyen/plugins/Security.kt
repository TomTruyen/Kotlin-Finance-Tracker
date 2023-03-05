package com.tomtruyen.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tomtruyen.security.token.TokenConfig
import io.ktor.server.application.*

fun Application.configureSecurity(config: TokenConfig) {
    
    authentication {
            jwt {
                verifier(
                    JWT.require(Algorithm.HMAC256(config.secret))
                        .withAudience(config.audience)
                        .withIssuer(config.issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(config.audience) && credential.payload.expiresAt.time > System.currentTimeMillis()) {
                        JWTPrincipal(credential.payload)
                    } else null
                }
            }
        }
}
