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

    val bookmarkStore = DatabaseBookmarkStore("jdbc:sqlite:${databasePath.pathString}")
    val bookmarkController = BookmarkController(bookmarkStore)

    val authorizationHeaderChecker = AuthorizationHeaderChecker(authSecret)

    Javalin.create { config ->
        config.showJavalinBanner = false
    }.routes {
        before("bookmarks", authorizationHeaderChecker::check)
        crud("bookmarks/{bookmark-id}", bookmarkController)
    }.start(port)
}
