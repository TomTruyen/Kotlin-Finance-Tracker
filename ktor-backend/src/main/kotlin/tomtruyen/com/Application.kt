package tomtruyen.com

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tomtruyen.com.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                if(tokenCredential.token == "123456") {
                    UserIdPrincipal("tomtruyen")
                } else {
                    null
                }
            }
        }
    }

    configureRouting()
}
