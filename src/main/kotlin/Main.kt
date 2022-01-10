import com.typesafe.config.ConfigFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.before
import io.javalin.apibuilder.ApiBuilder.crud

fun main() {
    val config = ConfigFactory.load()
    val databaseFilePath = config.getString("database_file")
    val authorizationSecret = config.getString("authorization_secret")

    val itemStore = DatabaseArchiveItemStore("jdbc:sqlite:$databaseFilePath")
    val archiveItemController = ArchiveItemController(itemStore)

    val authorizationHeaderChecker = AuthorizationHeaderChecker(authorizationSecret)

    Javalin.create().routes {
        before("items", authorizationHeaderChecker::check)
        crud("items/{item-id}", archiveItemController)
    }.start(7001)
}
