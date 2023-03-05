package com.tomtruyen.routes

import com.tomtruyen.data.model.User
import com.tomtruyen.data.requests.AuthRequest
import com.tomtruyen.data.responses.ErrorResponse
import com.tomtruyen.data.responses.AuthResponse
import com.tomtruyen.security.hashing.HashingService
import com.tomtruyen.repositories.UserRepository
import com.tomtruyen.security.hashing.SaltedHash
import com.tomtruyen.security.token.TokenConfig
import com.tomtruyen.security.token.TokenService
import com.tomtruyen.security.token.TokenClaim
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.validator.routines.EmailValidator

fun Route.register(hashingService: HashingService) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isEmailValid = EmailValidator.getInstance().isValid(request.email)
        if(!isEmailValid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid email"))
            return@post
        }

        val isPasswordValid = request.password.length >= 8
        if(!isPasswordValid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Password must be at least 8 characters"))
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            email = request.email,
            password = saltedHash.hash,
            salt = saltedHash.salt,
        )

        UserRepository.addUser(user)

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.login(
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    hashingService: HashingService
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = UserRepository.findUserByEmail(request.email)

        if(user == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Incorrect username or password"))
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if(!isValidPassword) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Incorrect username or password"))
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "id",
                value = user.id.toString()
            )
        )

        call.respond(HttpStatusCode.OK, AuthResponse(token))
    }
}

// Used on startup of client application to check if token is still valid
fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}