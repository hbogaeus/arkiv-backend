package bookmarks

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context

class BookmarkController(val store: BookmarkStore) : CrudHandler {
    override fun getAll(ctx: Context) {
        val allBookmarks = store.getAll()
        ctx.json(allBookmarks)
    }

    override fun getOne(ctx: Context, resourceId: String) {
        val bookmark = store.getOne(resourceId.toInt())
        ctx.json(bookmark)
    }

    override fun create(ctx: Context) {
        val bodyValidator = ctx.bodyValidator<CreateBookmarkRequest>()
        val createBookmarkRequest = bodyValidator.get()

        val bookmark = store.insert(createBookmarkRequest)

        ctx.json(bookmark)
    }

    override fun update(ctx: Context, resourceId: String) {
        val updateBookmarkRequest = ctx.bodyAsClass<Bookmark>()

        val bookmark = store.update(updateBookmarkRequest.id, updateBookmarkRequest)
        ctx.json(bookmark)
    }

    override fun delete(ctx: Context, resourceId: String) {
        store.delete(resourceId.toInt())
    }
}
