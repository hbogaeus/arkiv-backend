data class CreateItemRequest(val title: String, val url: String)

interface ArchiveItemStore {
    fun getAll(): List<ArchiveItem>
    fun getOne(id: Int): ArchiveItem
    fun insert(createItemRequest: CreateItemRequest): ArchiveItem
    fun update(id: Int, archiveItem: ArchiveItem): ArchiveItem
    fun delete(id: Int)
}
