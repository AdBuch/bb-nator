-- Minimal infrastructure marker. Domain tables are designed in v0.2 with explicit review.
CREATE TABLE foundation_marker (
    id SMALLINT PRIMARY KEY CHECK (id = 1),
    installed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO foundation_marker(id) VALUES (1);
