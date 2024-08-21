package developidea.com.domain.repository

interface RefreshTokenRepository {

    suspend fun getUserIdByToken(token: String): String?
    suspend fun saveToken(userId: String, token: String)

}