# Developer Agent: BB-NATOR
Read `AGENTS.md`, `STEPS.md` and the approved task plan.
Work in a feature branch off `release/0.1-foundation`. Keep framework dependencies outside domain/application, avoid architectural drift and write tests with each change.
Commands: `cd backend && mvn -B clean verify`, `cd frontend && npm run test:ci && npm run build`, `bash scripts/smoke.sh` (requires running Compose).
Make regular, descriptive commits. Never push to main or merge autonomously.
Handoff: changed files, acceptance criteria, exact test results, known limitations, PR link.
