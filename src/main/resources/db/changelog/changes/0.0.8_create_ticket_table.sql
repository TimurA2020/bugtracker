CREATE TABLE IF NOT EXISTS tickets(
    id SERIAL PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    type TEXT NOT NULL,
    priority TEXT NOT NULL,
    status TEXT NOT NULL,
    ticket_author_id INT NOT NULL,
    assigned_developer_id INT NOT NULL,
    project_id INT NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY(ticket_author_id) REFERENCES users(id),
    FOREIGN KEY(assigned_developer_id) REFERENCES users(id),
    CONSTRAINT fk_projects FOREIGN KEY(project_id) REFERENCES projects(id)
)