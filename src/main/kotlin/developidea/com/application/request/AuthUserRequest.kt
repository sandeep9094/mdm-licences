package developidea.com.application.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserRequest(
    val email: String,
    val password: String
)
