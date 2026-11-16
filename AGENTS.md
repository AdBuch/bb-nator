# Shared instructions for BB-NATOR agents

## Mission
Build an educational tool that strengthens apprentice independence, practical competence, reflection and successful EFZ completion.

## Mandatory workflow
Follow [STEPS.md](STEPS.md): **plan → develop → independent review → test → human-approved merge → next step**.

## Release 0.1 implementation resources
- [Release 0.1 execution checklist](docs/requirements/RELEASE_0_1.md)
- [Local development & commands](docs/process/LOCAL_DEVELOPMENT.md)
- [Module and onion-layer template](docs/architecture/MODULE_TEMPLATE.md)
- [Agent roles](agents/PLANNER.md) ([Developer](agents/DEVELOPER.md), [Reviewer](agents/REVIEWER.md), [Tester](agents/TESTER.md))

## Required reference documents
- [Product vision and seven planning perspectives](docs/requirements/PRODUCT_VISION.md)
- [Mentor dashboard, task creation and evidence workflows](docs/requirements/BB_COCKPIT.md)
- [UX mockup/wireframe specification](docs/design/UX_WIREFRAMES.md)
- [Release targets and acceptance gates](docs/requirements/RELEASES.md)
- [Technology and Onion Architecture](docs/architecture/TECHNOLOGY.md)
- [Identity, tenancy and data protection decisions](docs/architecture/ADR-0002-identity-tenancy-data-protection.md)
- [Data protection mechanism](docs/requirements/PRIVACY.md)

## Architecture
- Monorepo; modular monolith.
- Java 21, Spring Boot, Maven, Angular, PostgreSQL, Flyway and Docker Compose.
- Keep domain logic separate from persistence, transport and UI.
- Prefer simple maintainable solutions over premature abstractions.

## Safety and boundaries
- Work only on a feature branch. Do not push or merge main without explicit human approval.
- Never use sudo, privileged containers or destructive infrastructure commands.
- Never commit secrets, personal apprentice data, real grades, confidential company material or proprietary code.
- Do not send private source or personal data to external AI services without explicit authorization.
- All demo identities and progress metrics must be fictional.
- Do not equate focus minutes, XP or number of tickets with verified competency.
- Preserve accessibility and privacy-by-design.

## Engineering quality
- Small commits; focused PRs; reproducible builds.
- Add tests for behavior changes; use migrations for schema changes.
- Never report a test as passed unless it was actually executed.
- When uncertain, surface the uncertainty rather than inventing implementation details.
- Review and testing should be independent of the author when possible.

## Agent handoff format
1. Goal and scope
2. Files changed
3. Acceptance criteria status
4. Tests run (exact commands/results)
5. Security/privacy considerations
6. Open issues and next step
