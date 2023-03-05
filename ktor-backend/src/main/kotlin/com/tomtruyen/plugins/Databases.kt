package com.tomtruyen.plugins

import com.tomtruyen.DatabaseFactory
import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDatabases() {
    DatabaseFactory.init(environment)
}
