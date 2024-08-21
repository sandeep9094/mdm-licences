package developidea.com.application.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val emailId: String
)
