class InMemoryArchiveItemStore : ArchiveItemStore {
    private var currentId = 0
    private val store = mutableMapOf<Int, ArchiveItem>()

    override fun getAll(): List<ArchiveItem> {
        return store.values.toList()
    }

    override fun getOne(id: Int): ArchiveItem {
        return store[id]!!
    }

    override fun insert(createItemRequest: CreateItemRequest): ArchiveItem {
        val archiveItem = ArchiveItem(currentId++, createItemRequest.title, createItemRequest.url)
        store[archiveItem.id] = archiveItem
        return archiveItem
    }

    override fun update(id: Int, archiveItem: ArchiveItem): ArchiveItem {
        store.replace(archiveItem.id, archiveItem)
        return archiveItem
    }

    override fun delete(id: Int) {
        store.remove(id)
    }
}