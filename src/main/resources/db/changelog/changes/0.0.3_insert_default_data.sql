INSERT INTO roles (role) VALUES ('ROLE_USER'), ('ROLE_DEVELOPER'), ('ROLE_ADMIN');
INSERT INTO users (firstname, lastname, email, password) VALUES ('Admin', 'Admin', 'admin@bugtracker.io', 'admin');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (1, 3);