package com.tomtruyen.utils

import com.tomtruyen.security.token.USER_ID_CLAIM
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.UUID

fun ApplicationCall.getUserId(): UUID {
    principal<JWTPrincipal>()!!.getClaim(USER_ID_CLAIM, String::class)!!.let {
        return UUID.fromString(it)
    }
}