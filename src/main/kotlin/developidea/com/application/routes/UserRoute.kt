package developidea.com.application.routes

import developidea.com.application.request.CreateUserRequest
import developidea.com.application.response.UserResponse
import developidea.com.domain.model.GenericErrorResponse
import developidea.com.domain.model.GenericResponse
import developidea.com.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {

    val userRepository by inject<UserRepository>()

    post {
        val userRequest = call.receive<CreateUserRequest>()
        val userResponse = userRepository.createUser(userRequest.toDbUser()) ?: return@post call.respond(
            HttpStatusCode.BadRequest,
            GenericErrorResponse(message = "Invalid user request payload!")
        )
        return@post call.respond(
            HttpStatusCode.OK,
            GenericResponse<UserResponse>(message = "User created Successfully", data = userResponse)
        )
    }

    get("/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            HttpStatusCode.BadRequest, GenericErrorResponse(message = "Invalid userId in query params")
        )

        val user = userRepository.getUserById(id)
            ?: return@get call.respond(
                HttpStatusCode.BadRequest, message = GenericErrorResponse(message = "User not found, Invalid userId!")
            )

        return@get call.respond(HttpStatusCode.OK, GenericResponse(data = user))
    }
}