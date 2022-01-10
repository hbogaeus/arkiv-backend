import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context


class ArchiveItemController(val store: ArchiveItemStore) : CrudHandler {
    override fun getAll(ctx: Context) {
        val allItems = store.getAll()
        ctx.json(allItems)
    }

    override fun getOne(ctx: Context, resourceId: String) {
        val item = store.getOne(resourceId.toInt())
        ctx.json(item)
    }

    override fun create(ctx: Context) {
        val bodyValidator = ctx.bodyValidator<CreateItemRequest>()
        val createItemRequest = bodyValidator.get()

        val archiveItem = store.insert(createItemRequest)

        ctx.json(archiveItem)
    }

    override fun update(ctx: Context, resourceId: String) {
        val updateItemRequest = ctx.bodyAsClass<ArchiveItem>()

        val archiveItem = store.update(updateItemRequest.id, updateItemRequest)
        ctx.json(archiveItem)
    }

    override fun delete(ctx: Context, resourceId: String) {
        store.delete(resourceId.toInt())
    }
}
