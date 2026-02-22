PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    creation INTEGER NOT NULL DEFAULT (strftime('%s', 'now');)
);

CREATE TABLE IF NOT EXISTS posts (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    content TEXT NOT NULL,
    owner_id TEXT NOT NULL,
    reply_to TEXT,
    rating INTEGER NOT NULL DEFAULT 0,
    creation INTEGER NOT NULL DEFAULT (strftime('%s', 'now');),
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reply_to) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_posts_owner_id ON posts(owner_id);
CREATE INDEX IF NOT EXISTS idx_posts_reply_to ON posts(reply_to);
CREATE INDEX IF NOT EXISTS idx_posts_creation ON posts(creation);

CREATE VIEW IF NOT EXISTS user_karma AS
SELECT u.id, COALESCE(SUM(p.rating), 0) AS karma
FROM users u LEFT JOIN posts p ON p.owner_id = u.id
GROUP BY u.id;