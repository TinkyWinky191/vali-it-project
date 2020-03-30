INSERT INTO user(created_date, last_modified_date, email, first_name, last_name, gender, username, password)
VALUES
       (now(), now(),'andreivaino@live.ru', 'Andrei', 'Vaino', true, 'Andrew191', '$2y$12$VgM3WyBXVdULghpMG9HiyOZo.csQCugoHa4.1UNn5WUjBUvQQWvxa'),
        (now(), now(),'jack.oneill@gmail.ru', 'Jack', 'Oneill', true, 'Jack191', '$2y$12$VgM3WyBXVdULghpMG9HiyOZo.csQCugoHa4.1UNn5WUjBUvQQWvxa');
/*USERS*/
INSERT INTO role(created_date, last_modified_date, name)
VALUES
(now(), now(), 'ROLE_USER'),
(now(), now(), 'ROLE_ADMIN');
/*ROLES*/
INSERT INTO users_roles(user_id, role_id)
VALUES
(1, 1),
(1, 2),
(2, 1);
/*USERS_ROLES*/
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (1,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about programming languages.','Programming',1);
/*CATEGORIES*/
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (2,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','Languages learning.','Languages',2);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (3,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about biology.','Biology',2);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (4,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about technology.','Technology',1);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (5,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about programming languages.','Baking',1);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (6,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about programming languages.','Sport',1);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (7,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about programming languages.','Cars',1);
INSERT INTO `category` (`id`,`created_date`,`last_modified_date`,`description`,`name`,`user_id`) VALUES (8,'2020-03-27 12:48:54.000000','2020-03-27 12:48:54.000000','All about programming languages.','Motocycles',1);
INSERT INTO theme(created_date, last_modified_date, name, description, category_id, user_id)
VALUES (now(), now(),'Java', 'All about java.', 1,1),
        (now(), now(),'Python', 'All about python.', 1, 1),
        (now(), now(),'Russian', 'All about russian language.', 2, 2),
        (now(), now(),'Estonian', 'All about estonian language.', 2, 2),
        (now(), now(),'Reproduction', 'reproduction study...', 3, 2),
        (now(), now(),'Evolution', 'Theme about evolution.', 3, 2),
        (now(), now(),'Robotics', 'All about Robotics.', 4, 1),
        (now(), now(),'3D printing', 'All about 3D printing.', 4, 1);

/*NOTES*/
INSERT INTO note(created_date, last_modified_date, name, content_text, theme_id, user_id)
VALUES (now(), now(),'NoteAbout JavaBasics', 'Java basics for beginners.', 1, 1),
       (now(), now(),'NoteAbout PythonBasics', 'PythonBasics for beginners.', 2, 1),
       (now(), now(),'NoteAbout Russian', 'All ab out russian language.', 3, 2),
       (now(), now(),'NoteAbout Estonian', 'All about estonian language.', 4, 2),
       (now(), now(),'NoteAbout Reproduction', 'reproduction study...', 5, 2),
       (now(), now(),'NoteAbout Evolution', 'Theme about evolution.', 6, 2),
       (now(), now(),'NoteAbout Robotics', 'All about Robotics.', 7, 1),
       (now(), now(),'NoteAbout 3D printing', 'All about 3D printing.', 8, 1);
