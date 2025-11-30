CREATE TABLE swipes(
    swipe_id  BIGSERIAL PRIMARY KEY,
    user_id_1 BIGINT NOT NULL,
    user_id_2 BIGINT NOT NULL,
    decision1 BOOLEAN,
    decision2 BOOLEAN,
    created_at TIMESTAMP DEFAULT NOW()
);
