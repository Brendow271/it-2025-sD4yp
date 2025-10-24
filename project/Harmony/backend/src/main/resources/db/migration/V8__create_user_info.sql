CREATE TABLE user_info(
    user_id BIGSERIAL PRIMARY KEY,
    age INTEGER NOT NULL,
    genres VARCHAR(255)[] NOT NULL,
    instrument VARCHAR(255)[] NOT NULL,
    location VARCHAR(255) NOT NULL,
    about TEXT
);
