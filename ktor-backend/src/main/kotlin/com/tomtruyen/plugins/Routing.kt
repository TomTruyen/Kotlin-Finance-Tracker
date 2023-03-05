package com.tomtruyen.plugins

import com.tomtruyen.routes.*
import com.tomtruyen.security.hashing.HashingService
import com.tomtruyen.security.token.TokenConfig
import com.tomtruyen.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting(
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    hashingService: HashingService
) {
    routing {
        // Authentication
        login(tokenService, tokenConfig, hashingService)
        register(hashingService)
        refreshToken(tokenService, tokenConfig)

        authenticate {
            // Logout
            logout()

            // Categories
            getCategories()
            createCategory()
            updateCategory()
            deleteCategory()
        }
    }
}
