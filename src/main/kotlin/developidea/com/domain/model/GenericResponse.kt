package developidea.com.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GenericResponse<T> (
    val status: Boolean = true,
    val message: String = "Success",
    val data: T? = null
)


@Serializable
data class GenericErrorResponse (
    val status: Boolean = false,
    val message: String,
    val data: String? = null
)
