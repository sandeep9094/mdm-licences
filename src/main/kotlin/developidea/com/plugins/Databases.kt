package developidea.com.plugins

import com.mongodb.ConnectionString
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import developidea.com.domain.repository.UserRepository
import developidea.com.service.UserService
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDatabase() {
    install(Koin) {
        slf4jLogger()
        modules(module {

            val username = environment.config.tryGetString("db.mongo.user")
            val password = environment.config.tryGetString("db.mongo.password")
            val host = environment.config.tryGetString("db.mongo.host") ?: "127.0.0.1"

            val connectionString = "mongodb+srv://$username:$password@$host/?retryWrites=true&w=majority&appName=licenses"
            val databaseName = "mdm-licenses"

            single {
                MongoClient.create(ConnectionString(connectionString))
            }
            single {
                get<MongoClient>().getDatabase(databaseName)
            }
        }, module {
            single<UserRepository> { UserService(get()) }
        })
    }
}


/**
 * Establishes connection with a MongoDB database.
 *
 * The following configuration properties (in application.yaml/application.conf) can be specified:
 * * `db.mongo.user` username for your database
 * * `db.mongo.password` password for the user
 * * `db.mongo.host` host that will be used for the database connection
 * * `db.mongo.port` port that will be used for the database connection
 * * `db.mongo.maxPoolSize` maximum number of connections to a MongoDB server
 * * `db.mongo.database.name` name of the database
 *
 * IMPORTANT NOTE: in order to make MongoDB connection working, you have to start a MongoDB server first.
 * See the instructions here: https://www.mongodb.com/docs/manual/administration/install-community/
 * all the paramaters above
 *
 * @returns [MongoDatabase] instance
 * */
fun Application.connectToMongoDB(): MongoDatabase {
    val user = environment.config.tryGetString("db.mongo.user")
    val password = environment.config.tryGetString("db.mongo.password")
    val host = environment.config.tryGetString("db.mongo.host") ?: "127.0.0.1"
    val port = environment.config.tryGetString("db.mongo.port") ?: "27017"
    val maxPoolSize = environment.config.tryGetString("db.mongo.maxPoolSize")?.toInt() ?: 20
    val databaseName = environment.config.tryGetString("db.mongo.database.name") ?: "myDatabase"

    val credentials = user?.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val uri = "mongodb://$credentials$host:$port/?maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClients.create(uri)
    val database = mongoClient.getDatabase(databaseName)

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}
