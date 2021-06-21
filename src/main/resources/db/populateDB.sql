DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'ytrtyrty', '2020-12-31 11:59:59'::timestamp, 500),
 (100000, 'ytrtyrhjghjty', '2020-12-30 16:59:59'::timestamp, 500),
 (100000, 'ytrtygjhrty', '2020-12-30 15:59:59'::timestamp, 500),
       (100000, 'ytrtyrty', '2020-12-31 12:59:59'::timestamp, 500),
       (100001, 'eryyyyr', '2021-12-31 23:59:59'::timestamp, 2000);
