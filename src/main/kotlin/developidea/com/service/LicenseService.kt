package developidea.com.service

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import developidea.com.application.request.LicenseRequest
import developidea.com.application.response.LicenseResponse
import developidea.com.domain.model.DbUser
import developidea.com.domain.model.License
import developidea.com.domain.model.MdmLicense
import developidea.com.domain.model.local.InvalidExpiryTimestampException
import developidea.com.domain.model.local.ValidationException
import developidea.com.domain.repository.LicenseRepository
import developidea.com.plugins.DB_COLLECTION_LICENSES
import developidea.com.plugins.DB_COLLECTION_USERS
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.Instant
import java.time.format.DateTimeParseException
import kotlin.random.Random

class LicenseService(
    mongoDatabase: MongoDatabase
) : LicenseRepository {

    private val userCollection = mongoDatabase.getCollection<DbUser>(DB_COLLECTION_USERS)
    private val licenseCollection = mongoDatabase.getCollection<MdmLicense>(
        DB_COLLECTION_LICENSES
    )

    private suspend fun createNewLicense(userId: String, expiryTimestamp: String?): License? {
        val user = userCollection.find(Filters.eq("_id", ObjectId(userId))).firstOrNull() ?: return null
        val timestamp = Instant.now().toEpochMilli()
        val randomDigits = Random.nextInt(10000)
        val licenseKey = "LICENSE_$randomDigits${user.id}$timestamp"

        val validTill: String? = expiryTimestamp?.let {
            try {
                Instant.parse(it).toString()
            } catch (e: DateTimeParseException) {
                null // Don't throw exception here, just return null for invalid timestamp
            }
        }

        if (validTill == null && expiryTimestamp != null) {
            // Only throw exception if expiryTimestamp was provided but invalid
            throw InvalidExpiryTimestampException("Invalid expiry timestamp: $expiryTimestamp")
        }

        return License(key = licenseKey, validTill = validTill)
    }

    override suspend fun generateLicense(request: LicenseRequest): LicenseResponse {
        val license =
            createNewLicense(request.userId, request.expiryTimestamp) ?: throw ValidationException("Invalid userId!")
        val mdmLicense = MdmLicense(license.key, request.userId, license.validTill)
        licenseCollection.insertOne(mdmLicense)
        return mdmLicense.toResponse()
    }

    override suspend fun bindLicense(): LicenseResponse? {
        return null
    }
}