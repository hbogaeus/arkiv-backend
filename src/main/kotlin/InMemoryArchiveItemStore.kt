class InMemoryArchiveItemStore : ArchiveItemStore {
    var currentId = 0
    val store = mutableMapOf<Int, ArchiveItem>()

    override fun getAll(): List<ArchiveItem> {
        return store.values.toList()
    }

    override fun getOne(id: Int): ArchiveItem {
        return store.get(id)!!
    }

    override fun insert(createItemRequest: CreateItemRequest): ArchiveItem {
        val archiveItem = ArchiveItem(currentId++, createItemRequest.title, createItemRequest.url)
        store.put(archiveItem.id, archiveItem)
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