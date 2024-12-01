CREATE TABLE users_table
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    role       VARCHAR(255)
        CONSTRAINT valid_role CHECK (role IN ('ROLE_TRAINEE', 'ROLE_TRAINER') OR role IS NULL)
);