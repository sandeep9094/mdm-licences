package developidea.com.service

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import developidea.com.domain.model.DbRefreshToken
import developidea.com.domain.repository.RefreshTokenRepository
import kotlinx.coroutines.flow.firstOrNull

class RefreshTokenService(
    mongoDatabase: MongoDatabase
): RefreshTokenRepository {

    companion object {
        private const val REFRESH_TOKENS_COLLECTION = "refresh_tokens"
    }

    private val refreshTokenCollection = mongoDatabase.getCollection<DbRefreshToken>(REFRESH_TOKENS_COLLECTION)

    override suspend fun getUserIdByToken(token: String): String? {
        val userDbRefreshToken = refreshTokenCollection.find(Filters.eq("token", token)).firstOrNull() ?: return null
        return userDbRefreshToken.userId
    }

    override suspend fun saveToken(userId: String, token: String) {
        val dbRefreshToken = DbRefreshToken(userId = userId, token = token)
        refreshTokenCollection.insertOne(dbRefreshToken)
    }

}