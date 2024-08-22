package developidea.com.plugins

import developidea.com.service.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity(
) {
    val jwtService by inject<JwtService>()

    authentication {
        jwt {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)

            validate { jwtCredential ->
                jwtService.customValidator(jwtCredential)

            }
        }
    }

}
