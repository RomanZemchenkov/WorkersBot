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