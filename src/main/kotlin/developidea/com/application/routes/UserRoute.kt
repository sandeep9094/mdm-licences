package developidea.com.application.routes

import developidea.com.application.request.AuthUserRequest
import developidea.com.application.request.CreateUserRequest
import developidea.com.application.response.AuthUserResponse
import developidea.com.application.response.UserResponse
import developidea.com.domain.model.local.GenericErrorResponse
import developidea.com.domain.model.local.GenericResponse
import developidea.com.domain.repository.UserRepository
import developidea.com.service.JwtService
import developidea.com.utils.encryptPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute(
    jwtService: JwtService
) {

    val userRepository by inject<UserRepository>()

    post("/register") {
        val userRequest = call.receive<CreateUserRequest>()
        val userResponse = userRepository.createUser(userRequest.toDbUser()) ?: return@post call.respond(
            HttpStatusCode.BadRequest, GenericErrorResponse(message = "Invalid user request payload!")
        )
        return@post call.respond(
            HttpStatusCode.OK, GenericResponse<UserResponse>(message = "User created Successfully", data = userResponse)
        )
    }

    post("/authenticate") {
        val authUserRequest = call.receive<AuthUserRequest>()
        val userResponse =
            userRepository.authenticateUser(authUserRequest.email, authUserRequest.password.encryptPassword())
                ?: return@post call.respond(
                    HttpStatusCode.BadRequest, GenericErrorResponse(message = "Email or password is invalid!")
                )
        val jwtToken = jwtService.createAccessToken(userResponse.id)
        val authResponse = AuthUserResponse(jwtToken, userResponse)
        return@post call.respond(HttpStatusCode.OK, GenericResponse(data = authResponse))
    }

    authenticate {
        get("/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, GenericErrorResponse(message = "Invalid userId in query params")
            )

            val user = userRepository.getUserById(id) ?: return@get call.respond(
                HttpStatusCode.BadRequest, message = GenericErrorResponse(message = "User not found, Invalid userId!")
            )
            return@get call.respond(HttpStatusCode.OK, GenericResponse(data = user))
        }

    }


}