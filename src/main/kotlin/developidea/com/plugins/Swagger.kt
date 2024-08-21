package developidea.com.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureSwaggerUI() {
    routing {
        swaggerUI(path = "swagger-ui", swaggerFile = "openapi/documentation.yaml") {
            version = "1.0.0"
        }
    }
}
