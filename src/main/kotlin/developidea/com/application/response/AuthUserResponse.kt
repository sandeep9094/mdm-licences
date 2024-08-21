package developidea.com.application.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    val token: String,
    val user: UserResponse
)
