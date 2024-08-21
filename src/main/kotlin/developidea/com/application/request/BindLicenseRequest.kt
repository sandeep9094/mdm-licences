package developidea.com.application.request

import kotlinx.serialization.Serializable

@Serializable
data class BindLicenseRequest(val licenseKey: String, val userId: String)