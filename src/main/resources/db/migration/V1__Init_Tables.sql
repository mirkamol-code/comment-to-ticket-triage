CREATE TYPE category_type AS ENUM ('BUG', 'FAILURE', 'BILLING', 'ACCOUNT', 'OTHER');
CREATE TYPE priority_type AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE IF NOT EXISTS comment (
    id UUID PRIMARY KEY,
    sender_email VARCHAR(254) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS ticket (
    id UUID PRIMARY KEY,
    title VARCHAR(254) NOT NULL,
    category VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    summary VARCHAR(2000) NOT NULL,
    comment_id UUID,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_ticket_comment
    FOREIGN KEY (comment_id)
    REFERENCES comment (id)
    ON DELETE CASCADE
    );