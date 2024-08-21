package developidea.com.domain.repository

import developidea.com.application.request.LicenseRequest
import developidea.com.application.response.LicenseResponse
import developidea.com.domain.model.MdmLicense

interface LicenseRepository {

    suspend fun generateLicense(request: LicenseRequest): LicenseResponse

    suspend fun bindLicense(): LicenseResponse?

}