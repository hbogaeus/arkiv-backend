import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse

const val AUTHORIZATION_HEADER = "Authorization"

class AuthorizationHeaderChecker(val authorizationSecret: String ) {
    fun check(ctx: Context) {
        val authHeader = ctx.header(AUTHORIZATION_HEADER) ?: throw ForbiddenResponse()

        if (!authHeader.startsWith("Basic ") || !authHeader.substringAfter("Basic ").equals(authorizationSecret)) {
            throw ForbiddenResponse()
        }
    }
}