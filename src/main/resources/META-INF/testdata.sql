INSERT INTO users (user_name, user_pass) VALUES ('user', 'test');
INSERT INTO users (user_name, user_pass) VALUES ('admin', 'test');
INSERT INTO roles (role_name) VALUES ('user');
INSERT INTO roles (role_name) VALUES ('admin');
INSERT INTO user_roles (user_name, role_name) VALUES ('user', 'admin');
INSERT INTO user_roles (user_name, role_name) VALUES ('admin', 'admin');