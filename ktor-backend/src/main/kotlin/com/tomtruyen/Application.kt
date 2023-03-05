package com.tomtruyen

import io.ktor.server.application.*
import com.tomtruyen.plugins.*
import com.tomtruyen.security.hashing.SHA256HashingService
import com.tomtruyen.security.token.JwtTokenService
import com.tomtruyen.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val tokenService = JwtTokenService()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 86400000L, // 1 day
        secret = environment.config.property("jwt.secret").getString(),
    )

    val hashingService = SHA256HashingService()

    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(tokenService, tokenConfig, hashingService)
}
