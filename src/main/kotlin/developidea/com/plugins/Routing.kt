package developidea.com.plugins

import developidea.com.application.routes.licenseRoute
import developidea.com.application.routes.userRoute
import developidea.com.service.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(
) {
    val jwtService by inject<JwtService>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/api/v1/user") {
            userRoute(jwtService)
        }

        authenticate {
            route("/api/v1/license") {
                licenseRoute()
            }
        }

    }
}
