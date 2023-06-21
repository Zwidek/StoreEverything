INSERT INTO application_user(id, age, email, name, password, surname, username)
VALUES (1L, 23, '71033@student.pb.edu.pl', 'admin', '$2a$12$8hMBpwwwtgvFWAbhd6omd.P2U0DJIzCGJY0tOhV/extkRXlIQTOEu', 'admin', 'admin');

INSERT INTO role (id, title)
VALUES (1L, 'ADMIN'),
       (2L, 'FULL_USER'),
       (3L, 'USER');

INSERT INTO users_roles (user_id, role_id)
VALUES (1L, 1L),
       (1L, 2L),
       (1L, 3L);

INSERT INTO category
VALUES (1L, 'First'),
       (2L, 'Second');

INSERT INTO access_type
VALUES
    (1L, 'PRIVATE'),
    (2L, 'PUBLIC'),
    (3L, 'SHARED VIA LINK'),
    (4L, 'SHARED WITH USERS');


INSERT INTO stored_element (id, creation_date, description, link, shared_link, title, access_type_id, category_id, user_id)
VALUES (1L, now(), 'Test1', 'http://localhost:8080/index', null, 'Test1', 1L, 1L, 1L);

INSERT INTO stored_element (id, creation_date, description, link, shared_link, title, access_type_id, category_id, user_id)
VALUES (2L, now(), 'Test2', 'http://localhost:8080/index', null, 'Test2', 2L, 2L, 1L);