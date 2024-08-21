package developidea.com.domain.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class DbRefreshToken(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val token: String
)
