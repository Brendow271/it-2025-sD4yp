CREATE TABLE user_info(
    user_id BIGSERIAL PRIMARY KEY,
    age INTEGER,
    genres VARCHAR(255)[],
    instrument VARCHAR(255)[],
    location VARCHAR(255),
    about TEXT
);
