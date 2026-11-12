# BB-NATOR: Local development

## Requirements
- Docker Engine + Compose plugin, or Java 21/Maven 3.9+ and Node 24.15+.
- `cp .env.example .env`; replace `POSTGRES_PASSWORD` with a local strong value.
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

## Fast local frontend/backend feedback loop
```bash
docker compose up -d postgres
# terminal 1
cd backend
DB_HOST=localhost DB_PORT=5433 DB_NAME=bbnator DB_USER=bbnator DB_PASSWORD='<from-.env>' mvn spring-boot:run
# terminal 2 (Node 24.15+)
cd frontend
npm install
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
