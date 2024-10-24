DROP TABLE IF EXISTS personal_info CASCADE ;
DROP TABLE IF EXISTS state CASCADE ;
DROP TABLE IF EXISTS worker_meeting CASCADE ;
DROP TABLE IF EXISTS meeting CASCADE ;
DROP TABLE IF EXISTS worker CASCADE ;
DROP TABLE IF EXISTS post CASCADE ;
DROP TABLE IF EXISTS company CASCADE ;

CREATE TABLE company
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL
);

CREATE TABLE post
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL UNIQUE
);

CREATE TABLE worker
(
    id BIGINT PRIMARY KEY,
    company_id BIGINT REFERENCES company(id)
);


CREATE TABLE state
(
    worker_id BIGINT PRIMARY KEY REFERENCES worker(id),
    stage VARCHAR(32),
    state VARCHAR(32)
);

CREATE TABLE personal_info
(
    worker_id BIGINT PRIMARY KEY REFERENCES worker(id),
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    patronymic VARCHAR(128),
    username VARCHAR(128) NOT NULL UNIQUE,
    birthday DATE,
    post_id BIGINT REFERENCES post(id),
    chat_id BIGINT UNIQUE

);

CREATE TABLE meeting
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    time TIMESTAMP NOT NULL
);

CREATE TABLE worker_meeting
(
    worker_id BIGINT REFERENCES worker(id),
    meeting_id BIGINT REFERENCES meeting(id),
    PRIMARY KEY (worker_id,meeting_id)
);