package com.tomtruyen.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import com.tomtruyen.models.User
import com.tomtruyen.models.response.AuthResponse
import com.tomtruyen.utils.AuthUtils

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Authentication
        post("/login") {
            val user = call.receive<User>()

            // TODO: Check user in database

            val token = AuthUtils.generateToken(user)

            call.respond(AuthResponse(token))
        }

        authenticate("auth-jwt") {
            get("/auth") {
                val principal = call.principal<JWTPrincipal>()

                val email = principal?.payload?.getClaim("email")?.asString()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

                call.respondText("Hello $email! Your token expires in $expiresAt ms")
            }
        }
    }
}
