package bookmarks

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DatabaseBookmarkStoreTest {
    private val databasePath: Path
    private val store: DatabaseBookmarkStore

    init {
        /*
         * jdbc:sqlite::memory: doesn't work here due to running the migrations
         * connecting through JDBI ends up creating two different in-memory databases,
         * so we're creating a file database here instead
         */
        databasePath = Paths.get("test.db")
        val connectionString = "jdbc:sqlite:${databasePath.pathString}"

        store = DatabaseBookmarkStore(connectionString)
    }

    @AfterAll
    fun cleanup() {
        Files.delete(databasePath)
    }

    @Test
    fun `creating a bookmark inserts it in the database`() {
        val createBookmarkRequest = CreateBookmarkRequest(":title:", ":url:")
        val insertedBookmark = store.insert(createBookmarkRequest)
        val fetchedBookmark = store.getOne(insertedBookmark.id)

        Assertions
            .assertThat(insertedBookmark)
            .usingRecursiveComparison()
            .isEqualTo(fetchedBookmark)
    }

    @Test
    fun `get all return all bookmarks in the database`() {
        val bookmark1 = store.insert(CreateBookmarkRequest(":title-1:", ":url-1:"))
        val bookmark2 = store.insert(CreateBookmarkRequest(":title-2:", ":url-2:"))
        val bookmark3 = store.insert(CreateBookmarkRequest(":title-3:", ":url-3:"))

        val all = store.getAll()

        Assertions
            .assertThat(all)
            .contains(bookmark1, bookmark2, bookmark3)
    }

    @Test
    fun `update sets new values for fields`() {
        val createBookmarkRequest = CreateBookmarkRequest(":title:", ":url:")
        val insertedBookmark = store.insert(createBookmarkRequest)

        val updatedBookmark = insertedBookmark.copy(title = ":updated-title:", url = ":updated-url:")
        store.update(insertedBookmark.id, updatedBookmark)

        val one = store.getOne(insertedBookmark.id)
        Assertions.assertThat(one).usingRecursiveComparison().isEqualTo(updatedBookmark)
    }

    @Test
    fun `delete removes the bookmark from the database`() {
        val createBookmarkRequest = CreateBookmarkRequest(":title:", ":url:")
        val insertedBookmark = store.insert(createBookmarkRequest)

        store.delete(insertedBookmark.id)

        val all = store.getAll()

        Assertions.assertThat(all).doesNotContain(insertedBookmark)
    }
}