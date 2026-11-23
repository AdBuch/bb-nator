-- Flyway callback: runs after every migrate, as the owner role.
-- Keeps the runtime role bbnator_app least-privileged and usable for login.
-- bbnator_app is NOSUPERUSER and NOBYPASSRLS (V3), so row-level security applies to it.
GRANT USAGE ON SCHEMA public TO bbnator_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO bbnator_app;
REVOKE ALL ON flyway_schema_history FROM bbnator_app;

-- Enable login only when a password is supplied (APP_DB_PASSWORD). Without it the role stays NOLOGIN.
-- Note: with log_statement=all PostgreSQL would log this block; the image default is 'none'.
DO $callback$
BEGIN
  IF length($pw$${appdbpassword}$pw$) > 0 THEN
    EXECUTE format('ALTER ROLE bbnator_app WITH LOGIN PASSWORD %L', $pw$${appdbpassword}$pw$);
  END IF;
END
$callback$;
