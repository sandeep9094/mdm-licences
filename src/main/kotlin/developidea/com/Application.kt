package developidea.com

import developidea.com.plugins.*
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureExceptions()
    configureDatabase()
    configureRouting()
    configureSwaggerUI()
}
