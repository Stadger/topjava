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
VALUES (100000, 'user meal 1', '2020-12-31 11:17'::timestamp, 500),
       (100000, 'user meal 2', '2020-12-30 16:30'::timestamp, 800),
       (100001, 'admin meal 1', '2021-12-31 23:20'::timestamp, 2000);
