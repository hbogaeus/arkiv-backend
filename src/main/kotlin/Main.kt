import com.typesafe.config.ConfigFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.before
import io.javalin.apibuilder.ApiBuilder.crud
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

fun main() {
    val config = ConfigFactory.load()
    val databaseFilePath = Path.of(config.getString("database_file"))
    val authorizationSecret = config.getString("authorization_secret")
    val port = config.getInt("port")

    if (!Files.exists(databaseFilePath)) {
        throw IllegalStateException("Database file does not exist at '${databaseFilePath.toAbsolutePath()}'")
    }

    val itemStore = DatabaseArchiveItemStore("jdbc:sqlite:${databaseFilePath.pathString}")
    val archiveItemController = ArchiveItemController(itemStore)

    val authorizationHeaderChecker = AuthorizationHeaderChecker(authorizationSecret)

    Javalin.create().routes {
        before("items", authorizationHeaderChecker::check)
        crud("items/{item-id}", archiveItemController)
    }.start(port)
}
