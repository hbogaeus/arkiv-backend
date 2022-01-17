package bookmarks

import java.time.ZonedDateTime

data class Bookmark(val id: Int, val title: String, val url: String, val createdAt: ZonedDateTime)
