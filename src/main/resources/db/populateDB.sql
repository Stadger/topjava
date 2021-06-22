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
VALUES (100000, 'user meal 1', '2020-12-31 11:17', 500),
       (100000, 'user meal 2', '2020-12-30 16:30', 800),
       (100001, 'admin meal 1', '2021-12-31 23:20', 2000),
       (100000, 'user meal 3', '2020-12-29 09:30', 900),
       (100000, 'user meal 4', '2020-12-30 11:30', 2000),
       (100000, 'user meal 5', '2020-12-31 16:30', 800),
       (100000, 'user meal 6', '2020-12-31 20:30', 1500),
       (100000, 'user meal 7', '2021-06-30 16:30', 800);
