import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.mapTo

class DatabaseArchiveItemStore(connectionString: String) : ArchiveItemStore {
    private val jdbi: Jdbi

    init {
        val flyway = Flyway.configure().dataSource(connectionString, null, null).load()
        flyway.migrate()
        jdbi = Jdbi.create(connectionString).installPlugin(KotlinPlugin())
    }

    override fun getAll(): List<ArchiveItem> {
        val archiveItems = jdbi.withHandle<List<ArchiveItem>, Exception> { handle ->
            handle.createQuery("SELECT * FROM archive_items")
                .mapTo<ArchiveItem>()
                .list()
        }
        return archiveItems
    }

    override fun getOne(id: Int): ArchiveItem {
        val archiveItem = jdbi.withHandle<ArchiveItem, Exception> { handle ->
            handle.createQuery("SELECT * FROM archive_items WHERE id = :id")
                .bind("id", id)
                .mapTo<ArchiveItem>()
                .one()
        }
        return archiveItem
    }

    override fun insert(createItemRequest: CreateItemRequest): ArchiveItem {
        val archiveItem = jdbi.withHandle<ArchiveItem, Exception> { handle ->
            handle.createQuery("INSERT INTO archive_items (title, url) VALUES (:title, :url) RETURNING *")
                .bind("title", createItemRequest.title)
                .bind("url", createItemRequest.url)
                .mapTo<ArchiveItem>()
                .one()
        }
        return archiveItem
    }

    override fun update(id: Int, archiveItem: ArchiveItem): ArchiveItem {
        val updatedArchiveItem = jdbi.withHandle<ArchiveItem, Exception> { handle ->
            handle.createQuery("UPDATE archive_items SET title = :title, url = :url WHERE id = :id RETURNING *")
                .bind("id", archiveItem.id)
                .bind("title", archiveItem.title)
                .bind("url", archiveItem.url)
                .mapTo<ArchiveItem>()
                .one()
        }
        return updatedArchiveItem
    }

    override fun delete(id: Int) {
        jdbi.withHandle<Unit, Exception> { handle ->
            handle.execute("DELETE FROM archive_items WHERE id = ?", id)
        }
    }
}