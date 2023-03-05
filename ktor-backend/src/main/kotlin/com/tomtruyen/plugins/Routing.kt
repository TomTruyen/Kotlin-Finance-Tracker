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
        authenticate()

        // TODO: Add refresh tokens functionality (on an Unauthorized response, try a call to /refresh to get a new token), if failed --> redirect to login
        // TODO: Clear tokens of user on logout

        authenticate {
            // Categories
            getCategories()
            createCategory()
            updateCategory()
            deleteCategory()
        }
    }
}
