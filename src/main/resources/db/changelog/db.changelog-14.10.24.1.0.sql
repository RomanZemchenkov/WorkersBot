--liquibase formatted:sql

--changeset roman:1
CREATE TABLE company
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

--changeset roman:2
CREATE TABLE post
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL UNIQUE
);

--changeset roman:3
CREATE TABLE worker
(
    id BIGINT PRIMARY KEY
);

--changeset roman:4
CREATE TABLE state
(
    worker_id BIGINT PRIMARY KEY REFERENCES worker(id),
    stage VARCHAR(32),
    state VARCHAR(32)
);

--changeset roman:5
CREATE TABLE personal_info
(
    worker_id BIGINT PRIMARY KEY REFERENCES worker(id),
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    patronymic VARCHAR(128),
    username VARCHAR(128) NOT NULL UNIQUE,
    birthday DATE,
    company_id BIGINT REFERENCES company(id),
    post_id BIGINT REFERENCES post(id)

);

--changeset roman:6
CREATE TABLE personal_token
(
    id BIGSERIAL PRIMARY KEY,
    worker_id BIGINT REFERENCES worker(id),
    token VARCHAR(32) NOT NULL  UNIQUE,
    password VARCHAR(32) NOT NULL CHECK ( length(password) >= 8)
);
