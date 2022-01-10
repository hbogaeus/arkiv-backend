import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DatabaseArchiveItemStoreTest {
    private val databasePath: Path
    private val store: DatabaseArchiveItemStore

    init {
        /*
         * jdbc:sqlite::memory: doesn't work here due to running the migrations
         * connecting through JDBI ends up creating two different in-memory databases,
         * so we're creating a file database here instead
         */
        databasePath = Paths.get("test.db")
        val connectionString = "jdbc:sqlite:${databasePath.pathString}"

        store = DatabaseArchiveItemStore(connectionString)
    }

    @AfterAll
    fun cleanup() {
        Files.delete(databasePath)
    }

    @Test
    fun `creating an item inserts it in the database`() {
        val createItemRequest = CreateItemRequest(":title:", ":url:")
        val insertedItem = store.insert(createItemRequest)
        val fetchedItem = store.getOne(insertedItem.id)

        Assertions
            .assertThat(insertedItem)
            .usingRecursiveComparison()
            .isEqualTo(fetchedItem)
    }

    @Test
    fun `get all return all items in the database`() {
        val item1 = store.insert(CreateItemRequest(":title-1:", ":url-1:"))
        val item2 = store.insert(CreateItemRequest(":title-2:", ":url-2:"))
        val item3 = store.insert(CreateItemRequest(":title-3:", ":url-3:"))

        val all = store.getAll()

        Assertions
            .assertThat(all)
            .contains(item1, item2, item3)
    }

    @Test
    fun `update sets new values for fields`() {
        val createItemRequest = CreateItemRequest(":title:", ":url:")
        val insertedItem = store.insert(createItemRequest)

        val updatedItem = insertedItem.copy(title = ":updated-title:", url = ":updated-url:")
        store.update(insertedItem.id, updatedItem)

        val one = store.getOne(insertedItem.id)
        Assertions.assertThat(one).usingRecursiveComparison().isEqualTo(updatedItem)
    }

    @Test
    fun `delete removes the item from the database`() {
        val createItemRequest = CreateItemRequest(":title:", ":url:")
        val insertedItem = store.insert(createItemRequest)

        store.delete(insertedItem.id)

        val all = store.getAll()

        Assertions.assertThat(all).doesNotContain(insertedItem)
    }
}