--liquibase formatted-sql

--changeset roman:1
ALTER TABLE personal_info
ADD COLUMN chat_id BIGINT UNIQUE;