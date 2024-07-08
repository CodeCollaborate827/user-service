
-- Create the users table
CREATE TABLE IF NOT EXISTS users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       display_name VARCHAR(255),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS  user_address (
    id        SERIAL PRIMARY KEY,
    user_id   VARCHAR(255),
    country   VARCHAR(255),
    city      VARCHAR(255),
    district  VARCHAR(255),
    ward      VARCHAR(255),
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the friendships table
CREATE TABLE IF NOT EXISTS friendships (
    id SERIAL PRIMARY KEY,
    user1_id VARCHAR(255),
    user2_id VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS  friend_requests (
    id SERIAL PRIMARY KEY,
    sender_id VARCHAR(255),
    recipient_id VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE
);