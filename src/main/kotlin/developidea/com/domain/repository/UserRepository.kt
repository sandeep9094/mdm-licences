package developidea.com.domain.repository

import developidea.com.application.response.UserResponse
import developidea.com.domain.model.DbUser

interface UserRepository {

    suspend fun createUser(user: DbUser): UserResponse?

    suspend fun getUserById(id: String): UserResponse?

    fun getUserByUserName(username: String): UserResponse?

}