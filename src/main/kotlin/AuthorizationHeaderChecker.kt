import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse

const val AUTHORIZATION_HEADER = "Authorization"

class AuthorizationHeaderChecker(val authorizationSecret: String) {
    fun check(ctx: Context) {
        ctx.header(AUTHORIZATION_HEADER)?.let { authHeader ->
            if (!authHeader.substringAfter("Basic ").equals(authorizationSecret)) {
                throw ForbiddenResponse()
            }
        } ?: throw ForbiddenResponse()

    }
}