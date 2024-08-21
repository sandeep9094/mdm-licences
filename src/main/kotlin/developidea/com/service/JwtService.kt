package developidea.com.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import developidea.com.domain.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtService(
    private val application: Application,
    private val userRepository: UserRepository
) {

    private val defaultAccessTokenExpireIn = 1 * 3_600_000 // 1 hour
    private val defaultRefreshTokenExpireIn = 24 * 3_600_000 // 24 hour

    private fun getConfigProperty(path: String) = application.environment.config.property(path).toString()

    private val audience = getConfigProperty("jwt.audience")
    private val issuer = getConfigProperty("jwt.issuer")
    private val secret = getConfigProperty("jwt.secret")
    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier =
        JWT.require(Algorithm.HMAC256(secret)).withIssuer(issuer).withAudience(audience).build()


    fun createAccessToken(userId: String) = createJwtToken(userId, defaultAccessTokenExpireIn)

    fun createRefreshToken(userId: String) = createJwtToken(userId, defaultRefreshTokenExpireIn)

    private fun createJwtToken(userId: String, expireIn: Int) = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
        .sign(Algorithm.HMAC256(secret))

    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val userId = credential.payload.getClaim("userId").asString()
        userRepository.getUserById(userId) ?: return null
        if(!audienceMatches(credential)){
            return null
        }
        return JWTPrincipal(credential.payload)
    }


    fun audienceMatches(audience: String): Boolean = this.audience == audience

    private fun audienceMatches(credential: JWTCredential) = credential.payload.audience.contains(audience)

}