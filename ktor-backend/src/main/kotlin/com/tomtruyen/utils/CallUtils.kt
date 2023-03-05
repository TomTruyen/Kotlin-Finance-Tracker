package com.tomtruyen.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.UUID

fun ApplicationCall.getUserId(): UUID {
    principal<JWTPrincipal>()!!.subject!!.let {
        return UUID.fromString(it)
    }
}