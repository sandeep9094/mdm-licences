package developidea.com.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import developidea.com.domain.repository.LicenseRepository
import developidea.com.domain.repository.UserRepository
import developidea.com.service.JwtService
import developidea.com.service.LicenseService
import developidea.com.service.UserService
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

const val DB_COLLECTION_USERS = "users"
const val DB_COLLECTION_LICENSES = "licenses"

fun Application.configureDatabase() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single {
                connectToMongoDB()
            }

        }, module {
            single<UserRepository> { UserService(get()) }
            single<LicenseRepository> { LicenseService(get()) }
            single<JwtService> { JwtService(this@configureDatabase, get()) }
        })
    }
}


fun Application.connectToMongoDB(): MongoDatabase {
    val user = environment.config.tryGetString("db.mongo.user")
    val password = environment.config.tryGetString("db.mongo.password")
    val host = environment.config.tryGetString("db.mongo.host") ?: "127.0.0.1"
    val port = environment.config.tryGetString("db.mongo.port") ?: "27017"
    val databaseName = environment.config.tryGetString("db.mongo.database.name") ?: "mdm-licenses"
    val connectionString = "mongodb://$user:$password@$host:$port/?retryWrites=true&w=majority&appName=licenses"
    val mongoClient = MongoClient.create(connectionString)
    val database = mongoClient.getDatabase(databaseName)
    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}