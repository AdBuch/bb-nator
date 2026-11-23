-- Flyway callback: runs after every migrate, as the owner role.
-- Keeps the runtime role bbnator_app least-privileged and usable for login.
-- bbnator_app is NOSUPERUSER and NOBYPASSRLS (V3), so row-level security applies to it.
GRANT USAGE ON SCHEMA public TO bbnator_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO bbnator_app;
REVOKE ALL ON flyway_schema_history FROM bbnator_app;

-- Enable login only when a password is supplied (APP_DB_PASSWORD). Without it the role stays NOLOGIN.
-- Note: with log_statement=all PostgreSQL would log this block; the image default is 'none'.
-- The leading 'x' keeps the placeholder from directly following '$': Flyway reads '$${...}' as an escaped,
-- literal placeholder. substr(..., 2) removes it again.
DO $callback$
DECLARE
  app_password text := substr($pw$x${appdbpassword}$pw$, 2);
BEGIN
  IF length(app_password) > 0 THEN
    EXECUTE format('ALTER ROLE bbnator_app WITH LOGIN PASSWORD %L', app_password);
  END IF;
END
$callback$;
