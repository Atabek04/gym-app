CREATE TABLE trainee
(
    id            SERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    date_of_birth DATE,
    address       VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users_table (id)
);