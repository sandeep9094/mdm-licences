package developidea.com.application.routes

import developidea.com.application.request.BindLicenseRequest
import developidea.com.application.request.LicenseRequest
import developidea.com.domain.model.local.GenericErrorResponse
import developidea.com.domain.model.local.GenericResponse
import developidea.com.domain.repository.LicenseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.licenseRoute() {

    val licenseRepository by inject<LicenseRepository>()

    post("/generate") {
        val licenseRequest = call.receive<LicenseRequest>()
        val license = licenseRepository.generateLicense(licenseRequest)
        call.respond(HttpStatusCode.OK, message = GenericResponse(data = license))
    }

    post("/bind") {
        val bindLicenseRequest = call.receive<BindLicenseRequest>()
        val license = licenseRepository.bindLicense(bindLicenseRequest) ?: return@post call.respond(
            HttpStatusCode.BadRequest,
            GenericErrorResponse(message = "Invalid request payload")
        )
        call.respond(HttpStatusCode.OK, message = GenericResponse(data = license))
    }
}