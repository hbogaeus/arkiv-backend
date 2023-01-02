package bookmarks

import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.mapTo
import java.time.ZonedDateTime

class DatabaseBookmarkStore(connectionString: String) : BookmarkStore {
    private val jdbi: Jdbi

    init {
        val flyway = Flyway.configure().dataSource(connectionString, null, null).load()
        flyway.migrate()
        jdbi = Jdbi.create(connectionString)
            .installPlugin(KotlinPlugin())
            .registerColumnMapper(ZonedDateTime::class.java) { resultSet, columnNumber, _ ->
                ZonedDateTime.parse(resultSet.getString(columnNumber))
            }
    }

    override fun getAll(): List<Bookmark> {
        val bookmarks = jdbi.withHandle<List<Bookmark>, Exception> { handle ->
            handle.createQuery("SELECT * FROM bookmarks")
                .mapTo<Bookmark>()
                .list()
        }
        return bookmarks
    }

    override fun getOne(id: Int): Bookmark {
        val bookmark = jdbi.withHandle<Bookmark, Exception> { handle ->
            handle.createQuery("SELECT * FROM bookmarks WHERE id = :id")
                .bind("id", id)
                .mapTo<Bookmark>()
                .one()
        }
        return bookmark
    }

    override fun insert(createBookmarkRequest: CreateBookmarkRequest): Bookmark {
        val bookmark = jdbi.withHandle<Bookmark, Exception> { handle ->
            handle.createQuery("INSERT INTO bookmarks (title, url) VALUES (:title, :url) RETURNING *")
                .bind("title", createBookmarkRequest.title)
                .bind("url", createBookmarkRequest.url)
                .mapTo<Bookmark>()
                .one()
        }
        return bookmark
    }

    override fun update(id: Int, bookmark: Bookmark): Bookmark {
        val updatedBookmark = jdbi.withHandle<Bookmark, Exception> { handle ->
            handle.createQuery("UPDATE bookmarks SET title = :title, url = :url WHERE id = :id RETURNING *")
                .bind("id", bookmark.id)
                .bind("title", bookmark.title)
                .bind("url", bookmark.url)
                .mapTo<Bookmark>()
                .one()
        }
        return updatedBookmark
    }

    override fun delete(id: Int) {
        jdbi.withHandle<Unit, Exception> { handle ->
            handle.execute("DELETE FROM bookmarks WHERE id = ?", id)
        }
    }
}