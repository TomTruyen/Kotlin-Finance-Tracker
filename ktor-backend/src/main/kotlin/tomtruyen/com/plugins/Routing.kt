package tomtruyen.com.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("auth-bearer") {
            get("/auth") {
                call.respondText("Hello ${call.principal<UserIdPrincipal>()?.name}")
            }
        }
    }
}
