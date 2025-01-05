-- Truncate tables
DELETE FROM security_user;

-- Insert dummy data for security_user
INSERT INTO security_user (id, failed_login_count, is_account_non_locked, is_active, lockout_time, password, role, username)
VALUES (1,
        0,
        TRUE,
        TRUE,
        NULL,
        '$2a$12$ZAVALd2TgEFkC6dntq28be1xhNcR.tY2zJPjl0EzOKEROP2/ZORba', -- 123
        'ROLE_TRAINEE',
        'James.Bond');

INSERT INTO security_user (id, failed_login_count, is_account_non_locked, is_active, lockout_time, password, role, username)
VALUES (2,
        0,
        TRUE,
        TRUE,
        NULL,
        '$2a$12$uELAhg7eZtQyLmf8pfeN5.tNYSPQaBIl7jQviGK53TSzlrNBQcj2a', -- 123
        'ROLE_TRAINER',
        'Bruce.Wayne');

INSERT INTO security_user (id, failed_login_count, is_account_non_locked, is_active, lockout_time, password, role, username)
VALUES (3,
        0,
        TRUE,
        TRUE,
        NULL,
        '$2a$12$ORmerHOB.3NLwgwj3Ub8A.qeviQMDcS9DCajNoxHpzpBSHvu5kKci',  -- 123456
        'ROLE_Trainee',
        'Jack.Sparrow');

ALTER SEQUENCE security_user_id_seq RESTART WITH 4;