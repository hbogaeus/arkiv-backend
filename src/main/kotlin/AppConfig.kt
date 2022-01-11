import io.github.cdimascio.dotenv.dotenv
import java.nio.file.Path
import kotlin.io.path.Path

data class AppConfig(val databasePath: Path, val authSecret: String, val port: Int) {
    companion object {
        private const val DATABASE_PATH_KEY = "DATABASE_PATH"
        private const val AUTH_SECRET_KEY = "AUTH_SECRET"
        private const val PORT_KEY = "PORT"
        private const val DEFAULT_PORT = 7001

        fun fromEnvironment(): AppConfig {
            val dotenv = dotenv()

            val databasePath = dotenv[DATABASE_PATH_KEY]?.let(::Path)
                ?: throw IllegalStateException("No $DATABASE_PATH_KEY environment variable specified")
            val authorizationSecret = dotenv[AUTH_SECRET_KEY]
                ?: throw IllegalStateException("No $AUTH_SECRET_KEY environment variable specified")

            val port = dotenv.get(PORT_KEY)?.toInt() ?: DEFAULT_PORT

            return AppConfig(databasePath, authorizationSecret, port)
        }
    }
}