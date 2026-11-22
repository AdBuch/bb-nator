# Release v0.2a: Synthetic demonstration / local updater

**Status:** implementation in `feat/0.2a-demo-and-updater` PR #6. The product's full release v0.2 still needs LDAP, roles, proper user/tenant domain and official educational goal imports.

## First usable workflow
- Four fictional apprentices (two in semester 2; two in semester 6).
- Mentor sees learner cards, counts and tasks.
- Mentor creates a task with a title, selected fictional apprentice and simple learning-goal label.
- Learner sees assigned tasks and submits for review.
- Mentor moves tasks through `ASSIGNED`, `IN_PROGRESS`, `IN_REVIEW`, `DONE`; evidence tracked separately as `MISSING`, `SUBMITTED`, `VERIFIED`.
- Demo tasks persist in PostgreSQL across Docker restarts, with tenant-scoped rows and row-level security.
- No real people, school grades, confidential company data or integration tokens.

## Restrictions
- **Demonstration only.** The `demo` Spring profile exposes unauthenticated task creation/status endpoints. It is enabled in local `compose.yaml` and MUST NEVER run on a published host, real tenant or Internet-reachable deployment.
- The UI's view switch does not enforce roles. Never enter real apprentice information here.
- Learning-goal labels are illustrative, not official BiVo competency IDs or verified coverage.
- A task's `VERIFIED` flag is a demo-only simulated mentor action, not a formal competency assessment.
- Production security mode (`!demo`) retains deny-all for application endpoints; no LDAP implementation is claimed.
- Official source goals and protected entities are reserved for later v0.2 changes.
- Multi-user ownership and audit logging are not implemented in this demo.

## Local preview on feature branch
```bash
git clone https://github.com/AdBuch/bb-nator.git
cd bb-nator
git switch feat/0.2a-demo-and-updater
cp .env.example .env
# Set a random strong POSTGRES_PASSWORD in .env
docker compose up --build -d
EXPECT_DEMO=1 bash scripts/smoke.sh
```
Open http://127.0.0.1:8088. These instructions are for testing the unmerged PR, not for the automatic-updating main installation.

## After merge to main: automatic startup updates and admin button
Read [UPDATES.md](../process/UPDATES.md).

## Acceptance criteria
- [ ] Maven, Angular and Python unit tests pass in GitHub CI.
- [ ] Docker Compose smoke confirms demo API and basic technical status.
- [ ] Mentor can assign a synthetic task and see it after page reload.
- [ ] Demo learner can submit a task; mentor can review evidence separately.
- [ ] Direct business endpoints stay denied without explicit demo profile.
- [ ] Admin updater is loopback-only, token-protected and uses allowlisted Git commands.
- [ ] Human review, no real personal data, no production deployment.

## Next slices
1. LDAP/local roles with real server authorization and CSRF, plus RLS transaction boundary.
2. Replace demo labels with versioned official BiVo catalogue import.
3. Introduce production-grade learner/mentor entities, ownership and protection classes.
4. Evidence, review and reporting workflows with audit and retention.
