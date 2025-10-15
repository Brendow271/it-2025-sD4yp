CREATE TABLE user_info(
      user_id SERIAL PRIMARY KEY,
      user_age INTEGER NOT NULL,
      user_genres VARCHAR(255),
      user_instrument VARCHAR(255),
      user_location VARCHAR(255),
      user_about TEXT NOT NULL
);
