package developidea.com

import developidea.com.domain.repository.UserRepository
import developidea.com.plugins.*
import developidea.com.service.JwtService
import io.ktor.server.application.*
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureExceptions()
    configureDatabase()
    val userRepository by inject<UserRepository>()
    val jwtService = JwtService(this, userRepository)
    configureSecurity(jwtService)
    configureRouting(jwtService)
    configureSwaggerUI()
}
