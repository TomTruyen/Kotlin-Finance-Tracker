package com.tomtruyen

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tomtruyen.models.response.ErrorResponse
import com.tomtruyen.plugins.configureRouting
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import com.tomtruyen.plugins.*
import com.tomtruyen.utils.AuthUtils
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import java.util.logging.Logger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to protected endpoints"

            // Setup the verifier
            verifier(
                JWT.require(AuthUtils.secret).build()
            )

            // Make sure the token has the claim
            validate { credential ->
                if(credential.payload.getClaim("email").asString().isNotBlank()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            // Setup the challenge for unauthorized requests
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is invalid or has expired")
            }
        }
    }

    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            // TODO: Find a way to get the inner exception like the SerializationException so we can get a more detailed error message
            

            call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message))
        }
    }

    configureRouting()
}
