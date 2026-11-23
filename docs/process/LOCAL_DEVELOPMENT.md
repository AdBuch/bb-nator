# BB-NATOR: Local development

## Requirements
- Docker Engine + Compose plugin, or Java 21/Maven 3.9+ and Node 24.15+.
- `cp .env.example .env`; replace `POSTGRES_PASSWORD` and `APP_DB_PASSWORD` with two different strong local values.
- Existing `.env` from before v0.2: add `APP_DB_PASSWORD`, otherwise Compose refuses to start.
- No real apprentice data or proprietary company information in demo environments.

## Start full stack
```bash
cp .env.example .env
# Edit .env and set POSTGRES_PASSWORD
docker compose up --build -d
bash scripts/smoke.sh
```
Open http://127.0.0.1:8088. API technical status: http://127.0.0.1:8088/api/v1/system/status.
Postgres binds ONLY 127.0.0.1:5433; there is no public database port.
```bash
docker compose logs -f --tail=100
docker compose down
# Only when you deliberately want to delete all local database data:
docker compose down -v
```

## Database roles
- `POSTGRES_USER` owns the schema and runs Flyway migrations.
- The backend connects as `bbnator_app` (no superuser, no `BYPASSRLS`), so row-level tenant isolation applies. Its login password is set from `APP_DB_PASSWORD` after each migration.
- Running the backend directly as `POSTGRES_USER` (fast loop below) bypasses row-level security; repositories still filter by tenant, but do not rely on that for tests of isolation.

## Sign-in (non-demo)
Remove `demo` from `SPRING_PROFILES_ACTIVE` and configure the directory in `.env`:
```bash
SPRING_PROFILES_ACTIVE=
LDAP_URL=ldaps://directory.example.org:636
LDAP_BASE=dc=example,dc=org
LDAP_USER_SEARCH_BASE=ou=people
LDAP_USER_SEARCH_FILTER=(uid={0})        # Active Directory: (sAMAccountName={0})
LDAP_SUBJECT_ATTRIBUTE=uid               # stable key stored in BB-NATOR
LDAP_MANAGER_DN=                         # service account if anonymous search is not allowed
LDAP_MANAGER_PASSWORD=
BBNATOR_BOOTSTRAP_ADMIN=<your directory uid>
```
On start the tenant `BBNATOR_TENANT_ID` and one administrator with that subject are created if missing. A directory account without a local, active user with a role cannot sign in. API flow: `GET /api/v1/session/csrf`, `POST /api/v1/session/login` with header `X-XSRF-TOKEN`, `GET /api/v1/session/me`, `POST /api/v1/session/logout`. The sign-in screen in the Angular app follows in a separate change.

## Fast local frontend/backend feedback loop
```bash
docker compose up -d postgres
# terminal 1
cd backend
DB_HOST=localhost DB_PORT=5433 DB_NAME=bbnator DB_USER=bbnator DB_PASSWORD='<from-.env>' mvn spring-boot:run
# terminal 2 (Node 24.15+)
cd frontend
npm ci
npm start
```
Visit http://127.0.0.1:4200. Angular dev server proxies `/api` to http://localhost:8080.

## Tests
```bash
cd backend && mvn clean verify
cd frontend && npm run test:ci && npm run build
bash scripts/smoke.sh
```
The current v0.1 tests are minimal and do not represent full coverage.

## Working with AI agents
Read AGENTS.md, STEPS.md and docs/architecture/TECHNOLOGY.md.
Use feature branches from `release/0.1-foundation`, plan → develop → review → test → human-approved merge.
No branch merges, production deployments, or personal-data processing without human approval.
