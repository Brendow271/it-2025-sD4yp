CREATE TABLE user_image(
    image_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);
