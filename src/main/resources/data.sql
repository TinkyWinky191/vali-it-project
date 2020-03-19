/*USERS*/
INSERT INTO user(created_date, last_modified_date, email, first_name, last_name, gender, username, password)
VALUES
       (now(), now(),'andreivaino@live.ru', 'Andrei', 'Vaino', true, 'Andrew191', 'pass'),
        (now(), now(),'jack.oneill@gmail.ru', 'Jack', 'Oneill', true, 'Jack191', 'pass');
/*CATEGORIES*/
INSERT INTO category(created_date, last_modified_date, name, description, user_id)
VALUES (now(), now(),'Programming', 'All about programming languages.', 1),
        (now(), now(),'Languages', 'Languages learning.', 2),
        (now(), now(),'Biology', 'All about biology.', 2),
        (now(), now(),'Technology', 'All about technology.', 1);
/*CATEGORIES*/
INSERT INTO theme(created_date, last_modified_date, name, description, category_id)
VALUES (now(), now(),'Java', 'All about java.', 1),
        (now(), now(),'Python', 'All about python.', 1),
        (now(), now(),'Russian', 'All about russian language.', 2),
        (now(), now(),'Estonian', 'All about estonian language.', 2),
        (now(), now(),'Reproduction', 'reproduction study...', 3),
        (now(), now(),'Evolution', 'Theme about evolution.', 3),
        (now(), now(),'Robotics', 'All about Robotics.', 4),
        (now(), now(),'3D printing', 'All about 3D printing.', 4);
