package developidea.com.domain.model

import developidea.com.application.response.LicenseResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class MdmLicense(
    @BsonId
    val id: ObjectId,
    val key: String,
    val userId: String,
    val expiryTimestamp: String?,
    val device: Device?,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) {
    constructor(key: String, userId: String, expiryTimestamp: String?) : this(
        id = ObjectId(), key = key, userId = userId, expiryTimestamp = expiryTimestamp, device = null
    )

    fun toResponse(): LicenseResponse {
        return LicenseResponse(
            id = id.toString(),
            key = key,
            expiryTimestamp = expiryTimestamp,
            device = device
        )
    }
}