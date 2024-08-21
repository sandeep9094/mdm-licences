package developidea.com.plugins

import developidea.com.application.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/api/v1/user") {
            userRoute()
        }
    }
}
