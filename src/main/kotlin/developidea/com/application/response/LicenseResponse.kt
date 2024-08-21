package developidea.com.application.response

import kotlinx.serialization.Serializable

@Serializable
data class LicenseResponse(
    val id: String,
    val key: String,
    val expiryTimestamp: String?,
)