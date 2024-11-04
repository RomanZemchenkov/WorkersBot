INSERT INTO company(name)
VALUES ('Ozon');

INSERT INTO post(title)
VALUES ('director'),
       ('java-developer');

INSERT INTO worker(id, company_id)
VALUES (1,1),
       (2,1);

INSERT INTO personal_info(worker_id, firstname, lastname, username, post_id,chat_id)
VALUES (1,'Roman','Zemchenkov','Roman_Zemchenkov',1,11),
       (2,'Ivan','Petrov','Petya_1732',2,22);

INSERT INTO state(worker_id, stage, state)
VALUES (2,'EMPTY_STAGE','EMPTY_STATE');

INSERT INTO meeting(title, time)
VALUES ('First meeting', '2020-02-02 20:20'),
       ('Second meeting', '2021-02-02 20:20'),
       ('Third meeting', '2022-02-02 20:20'),
       ('Fourth meeting', '2023-02-02 20:20');

INSERT INTO worker_meeting(worker_id, meeting_id)
VALUES (1,1),
       (1,2),
       (1,3),
       (1,4);