create table training
(
    id                bigint                      not null primary key,
    training_type_id  integer                     not null references training_type(id),
    trainee_id        bigint                      not null references trainee(id),
    trainer_id        bigint                      not null references trainer(id),
    training_date     timestamp(6) with time zone not null,
    training_duration bigint                      not null,
    training_name     varchar(255)                not null
);