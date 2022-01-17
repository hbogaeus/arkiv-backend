data class CreateBookmarkRequest(val title: String, val url: String)

interface BookmarkStore {
    fun getAll(): List<Bookmark>
    fun getOne(id: Int): Bookmark
    fun insert(createBookmarkRequest: CreateBookmarkRequest): Bookmark
    fun update(id: Int, bookmark: Bookmark): Bookmark
    fun delete(id: Int)
}
