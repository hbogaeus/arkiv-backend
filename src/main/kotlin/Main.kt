import com.typesafe.config.ConfigFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.before
import io.javalin.apibuilder.ApiBuilder.crud
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

fun main() {
    val applicationConfig = ConfigFactory.load()
    val databaseFilePath = Path.of(applicationConfig.getString("database_path"))
    val authorizationSecret = applicationConfig.getString("authorization_secret")
    val port = applicationConfig.getInt("port")

    if (!Files.exists(databaseFilePath)) {
        throw IllegalStateException("Database file does not exist at '${databaseFilePath.toAbsolutePath()}'")
    }

    val itemStore = DatabaseArchiveItemStore("jdbc:sqlite:${databaseFilePath.pathString}")
    val archiveItemController = ArchiveItemController(itemStore)

    val authorizationHeaderChecker = AuthorizationHeaderChecker(authorizationSecret)

    Javalin.create { config ->
        config.showJavalinBanner = false
    }.routes {
        before("items", authorizationHeaderChecker::check)
        crud("items/{item-id}", archiveItemController)
    }.start(port)
}
