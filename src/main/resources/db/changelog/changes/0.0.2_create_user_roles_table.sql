CREATE TABLE IF NOT EXISTS users_roles(
    user_id INT REFERENCES users(id) NOT NULL,
    role_id INT REFERENCES roles(id) NOT NULL,
    PRIMARY KEY (user_id, role_id)
)