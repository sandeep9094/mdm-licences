package developidea.com.domain.model

import developidea.com.application.response.UserResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class DbUser(
    @BsonId
    val id: ObjectId,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
    val role: String = "user",
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) {
    fun toResponse(): UserResponse {
        return UserResponse(
            id = id.toString(),
            firstName = firstName,
            lastName = lastName,
            emailId = email
        )
    }
}
