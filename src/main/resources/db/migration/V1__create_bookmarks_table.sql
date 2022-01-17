CREATE TABLE IF NOT EXISTS bookmarks
(
    id    INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    url   TEXT NOT NULL,
    created_at TEXT DEFAULT (strftime('%Y-%m-%dT%H:%M:%SZ', 'now'))
)