package developidea.com.application.request

import kotlinx.serialization.Serializable

@Serializable
data class LicenseRequest(val userId: String, val expiryTimestamp: String?)