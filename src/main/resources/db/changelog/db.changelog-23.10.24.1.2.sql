--liquibase formatted-sql

--changeset roman:1
CREATE TABLE meeting
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    time TIMESTAMP NOT NULL
);

--changeset roman:2
CREATE TABLE worker_meeting
(
    worker_id BIGINT REFERENCES worker(id),
    meeting_id BIGINT REFERENCES meeting(id),
    PRIMARY KEY (worker_id,meeting_id)
);