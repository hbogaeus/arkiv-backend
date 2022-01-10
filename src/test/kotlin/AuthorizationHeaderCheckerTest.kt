import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class AuthorizationHeaderCheckerTest {
    val ctx = mockk<Context>()
    val authorizationSecret = "::very-secret::"
    val authorizationHeaderChecker = AuthorizationHeaderChecker(authorizationSecret)

    @Test
    fun `throws if no authorization header is set`() {
        every { ctx.header(AUTHORIZATION_HEADER) } returns null

        assertThatThrownBy {
            authorizationHeaderChecker.check(ctx)
        }.isInstanceOf(ForbiddenResponse::class.java)
    }

    @Test
    fun `throws if no authorization header doesn't have the correct format`() {
        every { ctx.header(AUTHORIZATION_HEADER) } returns "::some value::"

        assertThatThrownBy {
            authorizationHeaderChecker.check(ctx)
        }.isInstanceOf(ForbiddenResponse::class.java)
    }

    @Test
    fun `does not throw if authorization has correct format and matches secret`() {
        every { ctx.header(AUTHORIZATION_HEADER) } returns "Basic $authorizationSecret"

        assertThatCode {
            authorizationHeaderChecker.check(ctx)
        }.doesNotThrowAnyException()
    }

}