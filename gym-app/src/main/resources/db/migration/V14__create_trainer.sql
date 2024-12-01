CREATE TABLE trainer
(
    id               SERIAL PRIMARY KEY,
    user_id          BIGINT  NOT NULL,
    training_type_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_table (id)
);