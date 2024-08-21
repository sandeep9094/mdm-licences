package developidea.com.application.routes

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
        val mdmLicense = licenseRepository.generateLicense(licenseRequest)
        call.respond(HttpStatusCode.OK, message = GenericResponse(data = mdmLicense))
    }

    post("/bind") {
    }
}