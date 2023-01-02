package bookmarks

import java.time.ZonedDateTime

class InMemoryBookmarkStore : BookmarkStore {
    var currentId = 0
    val store = mutableMapOf<Int, Bookmark>()

    override fun getAll(): List<Bookmark> {
        return store.values.toList()
    }

    override fun getOne(id: Int): Bookmark {
        return store.get(id)!!
    }

    override fun insert(createBookmarkRequest: CreateBookmarkRequest): Bookmark {
        val bookmark = Bookmark(
            currentId++,
            createBookmarkRequest.title,
            createBookmarkRequest.url,
            ZonedDateTime.now()
        )
        store.put(bookmark.id, bookmark)
        return bookmark
    }

    override fun update(id: Int, bookmark: Bookmark): Bookmark {
        store.replace(bookmark.id, bookmark)
        return bookmark
    }

    override fun delete(id: Int) {
        store.remove(id)
    }
}