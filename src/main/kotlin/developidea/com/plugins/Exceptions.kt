package developidea.com.plugins

import developidea.com.domain.model.GenericErrorResponse
import developidea.com.domain.model.ParsingException
import developidea.com.domain.model.ValidationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptions() {
    install(StatusPages) {

        // Un-captured exceptions handling
        exception<Throwable> { call, throwable ->
            when(throwable) {
                is ValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericErrorResponse(status = false, throwable.message)
                    )
                }
                is ParsingException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        GenericErrorResponse(status = false, throwable.message)
                    )
                }
                else -> {
                    call.respond(
                        call.response.status() ?: HttpStatusCode.BadRequest,
                        GenericErrorResponse(message = throwable.message ?: "Something went wrong!")
                    )
                }
            }
        }

        //Handling exceptions via Status Codes
        status(
            // any number of status codes can be mentioned
            HttpStatusCode.InternalServerError,
            HttpStatusCode.BadGateway,
        ) { call, statusCode ->
            when(statusCode) {
                HttpStatusCode.InternalServerError -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        GenericErrorResponse(status = false, message = "Oops! internal server error at our end")
                    )
                }
                HttpStatusCode.BadGateway -> {
                    call.respond(
                        HttpStatusCode.BadGateway,
                        GenericErrorResponse(status = false, message = "Oops! We got a bad gateway. Fixing it. Hold on!")
                    )
                }
            }
        }
    }
}
