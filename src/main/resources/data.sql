/*USERS*/
INSERT INTO user(email, first_name, last_name, gender, username, password)
VALUES
       ('andreivaino@live.ru', 'Andrei', 'Vaino', true, 'Andrew191', 'pass'),
        ('jack.oneill@gmail.ru', 'Jack', 'Oneill', true, 'Jack191', 'pass');
/*CATEGORIES*/
INSERT INTO category(name, description, user_id)
VALUES ('Programming', 'All about programming languages.', 1),
        ('Languages', 'Languages learning.', 2);
