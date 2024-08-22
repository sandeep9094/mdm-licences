package developidea.com.application.request

import developidea.com.domain.model.Device
import kotlinx.serialization.Serializable

@Serializable
data class BindLicenseRequest(
    val licenseId: String,
    val licenseKey: String,
    val userId: String,
    val device: Device
)