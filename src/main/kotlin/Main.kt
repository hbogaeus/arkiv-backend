import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.before
import io.javalin.apibuilder.ApiBuilder.crud
import java.nio.file.Files
import kotlin.io.path.pathString

fun main() {
    val (databasePath, authSecret, port) = AppConfig.fromEnvironment()

    if (!Files.exists(databasePath)) {
        throw IllegalStateException("Database file does not exist at '${databasePath.toAbsolutePath()}'")
    }

    val itemStore = DatabaseArchiveItemStore("jdbc:sqlite:${databasePath.pathString}")
    val archiveItemController = ArchiveItemController(itemStore)

    val authorizationHeaderChecker = AuthorizationHeaderChecker(authSecret)

    Javalin.create { config ->
        config.showJavalinBanner = false
    }.routes {
        before("items", authorizationHeaderChecker::check)
        crud("items/{item-id}", archiveItemController)
    }.start(port)
}
