CREATE TABLE users(
    id SERIAL PRIMARY KEY NOT NULL,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE roles(
    id SERIAL PRIMARY KEY NOT NULL,
    role TEXT NOT NULL
);