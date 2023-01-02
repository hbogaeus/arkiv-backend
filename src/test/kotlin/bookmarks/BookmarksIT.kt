package bookmarks

import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import kong.unirest.Unirest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BookmarksIT {
    private val app: Javalin

    init {
        val bookmarkController = BookmarkController(InMemoryBookmarkStore())

        app = Javalin.create().routes {
            ApiBuilder.crud("bookmarks/{bookmark-id}", bookmarkController)
        }
    }

    @BeforeEach
    fun setup() {
        app.start(1234)
    }

    @AfterEach
    fun teardown() {
        app.stop()
    }

    @Test
    fun `creating a bookmark works`() {
        val objectMapper = ObjectMapper()

        val objectNode = objectMapper.createObjectNode();

        objectNode.put("url", "https://my-url.com")
        objectNode.put("title", "My Title")

        val json: String = objectMapper.writeValueAsString(objectNode)

        val response = Unirest.post("http://localhost:1234/bookmarks")
            .header("accept", "application/json")
            .body(json)
            .asJson()

        Assertions.assertThat(response.isSuccess).isTrue()
    }
}