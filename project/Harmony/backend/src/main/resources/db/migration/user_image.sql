CREATE TABLE user_auth(
      image_id SERIAL PRIMARY KEY,
      user_id INTEGER NOT NULL,
      image_url VARCHAR(255) NOT NULL ,
      created_at TIMESTAMP DEFAULT NOW()
);
