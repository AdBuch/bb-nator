# Release 0.1: Foundation · Execution Spec

**Release branch:** `release/0.1-foundation` (created). This is infrastructure and minimal UI only, not the full educational product.
**Architecture:** [TECHNOLOGY.md](../architecture/TECHNOLOGY.md)
**Vision:** [PRODUCT_VISION.md](PRODUCT_VISION.md)
**Task workflows:** [BB_COCKPIT.md](BB_COCKPIT.md)

## User value
A collaborator can clone and start a reproducible, privacy-conscious foundation, inspect an illustrative mentor/learner shell and observe a real API health/status response. Future work can be added in isolated feature modules.

## Must deliver
- Java 21 + Spring Boot 4.1.2, modular onion package example and JUnit test.
- Angular 22 + strict TypeScript, responsive mentor and learner empty-state shells.
- PostgreSQL 17 + Flyway baseline migration; Hibernate schema validation.
- Docker Compose for PostgreSQL, API, and Nginx-served Angular.
- Least-privilege security: public system status and actuator health ONLY; all other API routes denied until a reviewed authentication design.
- CI for backend unit tests, Angular unit/build, and Docker smoke.
- Example environment without committed credentials, public-repo privacy rules.
- Explicit plans and release gates for the next feature tasks.

## Acceptance criteria
- [ ] `cd backend && mvn clean verify` passes.
- [ ] `cd frontend && npm install && npm run test:ci && npm run build` passes on Node >=24.15 (major 24).
- [ ] `cp .env.example .env && docker compose up --build -d` starts all three containers and Flyway migration succeeds.
- [ ] `bash scripts/smoke.sh` confirms Angular index and API status.
- [ ] `GET /api/v1/system/status` returns a structured technical READY response; no private apprentice endpoint is exposed.
- [ ] Backend architecture test/example remains framework-free in domain/application.
- [ ] Source repo contains no real personal data or secrets.
- [ ] Independent review, test evidence, and human approval before merge to main.

## Proposed next tasks after foundation
1. Formal API DTO/errors/security design + OIDC provider decision (before personal data).
2. Import and version official Swiss BiVo competence hierarchy with authoritatively verified source.
3. Apprentice/mentor domain and authorized access, synthetic fixtures only.
4. Task authoring and assignment linked to multiple learning goals.
5. Role-specific development dashboards using verified evidence.

## Important limitations
- Current mentor/learner switch is **illustrative only**, not authentication.
- No real learner/task/report database schema, business endpoints, SSO or Jira/BCS integrations yet.
- No npm lockfile is committed in this bootstrap. Add a generated `package-lock.json` from the first successful `npm install`, then change CI and frontend Dockerfile from `npm install` to `npm ci` before production deployment.
- External build dependencies could not be fetched in ChatGPT's execution environment; actual GitHub CI and Docker smoke must confirm release gates.
