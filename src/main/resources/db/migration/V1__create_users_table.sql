CREATE TABLE users(
    id  BIGSERIAL PRIMARY KEY ,
    name VARCHAR(100) not null,
    email varchar(255) not null unique,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);