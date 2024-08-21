package developidea.com.service

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import developidea.com.application.response.UserResponse
import developidea.com.domain.model.DbUser
import developidea.com.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserService(
    mongoDatabase: MongoDatabase
) : UserRepository {

    companion object {
        private const val USER_COLLECTION = "users"
    }

    private val userCollection = mongoDatabase.getCollection<DbUser>(USER_COLLECTION)

    override suspend fun authenticateUser(email: String, passwordHash: String): UserResponse? {
        val user = userCollection.find(Filters.eq("email", email)).firstOrNull() ?: return null
        if (passwordHash != user.passwordHash) {
            return null
        }
        return user.toResponse()
    }

    override suspend fun createUser(user: DbUser): UserResponse {
        try {
            val isUserEmailExist = userCollection.find(Filters.eq("email", user.email)).firstOrNull()
            if (isUserEmailExist != null) {
                throw IllegalStateException("User Email-Id already exist!")
            }

            userCollection.insertOne(user)
            return user.toResponse()
        } catch (exception: MongoException) {
            throw IllegalStateException("Unable to insert due to an error: ${exception.localizedMessage}")
        }
    }

    override suspend fun getUserById(id: String): UserResponse? {
        val user = userCollection.find(Filters.eq("_id", ObjectId(id))).firstOrNull() ?: return null
        return user.toResponse()
    }

}