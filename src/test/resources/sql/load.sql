INSERT INTO company(name)
VALUES ('Ozon');

INSERT INTO post(title)
VALUES ('director'),
       ('java-developer');

INSERT INTO worker(id)
VALUES (1),
       (2);

INSERT INTO personal_info(worker_id, firstname, lastname, username, company_id, post_id)
VALUES (1,'Roman','Zemchenkov','Roman_Zemchenkov',1,1),
       (2,'Ivan','Petrov','Petya_1732',1,2);