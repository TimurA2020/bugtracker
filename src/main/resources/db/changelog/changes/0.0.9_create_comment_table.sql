CREATE TABLE IF NOT EXISTS comments(
    id SERIAL PRIMARY KEY NOT NULL,
    comment TEXT NOT NULL,
    user_id INT NOT NULL,
    ticket_id INT NOT NULL,
    date date NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_tickets FOREIGN KEY(ticket_id) REFERENCES tickets(id)
);