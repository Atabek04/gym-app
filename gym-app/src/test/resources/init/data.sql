INSERT INTO training_type (id, training_type_name)
VALUES (1, 'CARDIO'),
       (2, 'STRENGTH_TRAINING'),
       (3, 'YOGA'),
       (4, 'PILATES'),
       (5, 'HIIT');
ALTER SEQUENCE training_type_id_seq RESTART WITH 6;

INSERT INTO users_table (id, first_name, last_name, username)
VALUES (1, 'Bruce', 'Wayne', 'Bruce.Wayne');
INSERT INTO users_table (id, first_name, last_name, username)
VALUES (2, 'Clark', 'Kent', 'Clark.Kent');
INSERT INTO users_table (id, first_name, last_name, username)
VALUES (3, 'Tony', 'Stark', 'Tony.Stark');
INSERT INTO users_table (id, first_name, last_name, username)
VALUES (4, 'Diana', 'Prince', 'Diana.Prince');
INSERT INTO users_table (id, first_name, last_name, username)
VALUES (5, 'Peter', 'Parker', 'Peter.Parker');
ALTER SEQUENCE user_table_id_seq RESTART WITH 6;

INSERT INTO trainee (date_of_birth, id, user_id, address)
VALUES ('1990-01-01', 1, 1, 'Gotham City');
INSERT INTO trainee (date_of_birth, id, user_id, address)
VALUES ('1988-02-15', 2, 2, 'Metropolis');
INSERT INTO trainee (date_of_birth, id, user_id, address)
VALUES ('1980-05-29', 3, 3, 'Malibu, California');
ALTER SEQUENCE trainee_id_seq RESTART WITH 4;

INSERT INTO trainer (training_type_id, id, user_id)
VALUES (1, 1, 4); -- Diana Prince is a Yoga trainer
INSERT INTO trainer (training_type_id, id, user_id)
VALUES (2, 2, 5); -- Peter Parker is a Cardio trainer
ALTER SEQUENCE trainer_id_seq RESTART WITH 3;

INSERT INTO training (training_type_id, id, trainee_id, trainer_id, training_date, training_duration, training_name)
VALUES (1, 1, 1, 1, '2025-01-10 10:00:00', 60, 'Morning Yoga'), -- Diana Prince -> Bruce Wayne
       (2, 2, 2, 2, '2025-01-10 14:00:00', 45, 'Cardio Blast'); -- Peter Parker -> Clark Kent
ALTER SEQUENCE training_id_seq RESTART WITH 3;
