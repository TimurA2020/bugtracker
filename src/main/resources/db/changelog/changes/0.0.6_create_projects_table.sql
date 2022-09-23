CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    author_id INT NOT NULL,
    name TEXT NOT NULL,
    content TEXT NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY(author_id) REFERENCES users(id)
);