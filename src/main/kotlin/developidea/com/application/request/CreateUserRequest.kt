package developidea.com.application.request

import developidea.com.domain.model.DbUser
import developidea.com.utils.encryptPassword
import developidea.com.utils.isEmailValid
import developidea.com.utils.isPasswordValid
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class CreateUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {

    fun toDbUser(): DbUser {
        if (firstName.isEmpty()) {
            throw IllegalStateException("firstName field cannot be empty!")
        }

        if (lastName.isEmpty()) {
            throw IllegalStateException("lastName field cannot be empty!")
        }

        if (!email.isEmailValid()) {
            throw IllegalStateException("Invalid email format!")
        }

        if (!password.isPasswordValid()) {
            throw IllegalStateException("Password must be at least 8 characters long, contain at least 1 special character, 1 uppercase letter, and 1 numeric digit.")
        }

        return DbUser(
            ObjectId(),
            firstName.trim(),
            lastName.trim(),
            email.trim(),
            password.encryptPassword()
        )
    }


}
