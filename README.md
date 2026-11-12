# BB-NATOR 🎓

**BB-NATOR** is an open-source, AI-assisted vocational training companion for Swiss software-development apprentices and their mentors.

> Status: **planning / foundation**. The features below are targets, not yet implemented.

## Product specification
- [Complete product vision and seven planning perspectives](docs/requirements/PRODUCT_VISION.md)
- [Release targets, acceptance gates and branch names](docs/requirements/RELEASES.md)

## Mission
Help apprentices become independent, responsible software engineers while supporting the Swiss **Informatiker/in EFZ, Applikationsentwicklung (BiVo 2021)** learning objectives. The portal connects educational goals, practical evidence, mentoring, reflection, focus habits and preparation for the qualification procedure (QV/IPA).

## Intended features
- **Learning path:** eight-semester roadmap, official competency goals, individual interests and company objectives.
- **Daily companion:** daily goals, “What did I achieve today?”, learning journal and optional focus sessions.
- **Evidence:** links to Jira EDU tickets, GitHub/GitLab merge requests, tests and mentor evaluations.
- **Mentoring:** regular check-ins, feedback, semester reports and goal agreements.
- **Learning support:** staged ticket hints, quizzes, mini-games, XP and skill trees. Time and XP are **not** competency grades.
- **QV readiness:** practical milestones, mock IPA, presentation and technical interview preparation.

## Planned technology
| Layer | Choice |
| --- | --- |
| Frontend | Angular / TypeScript |
| Backend | Java 21 / Spring Boot / Maven |
| Database | PostgreSQL / Flyway |
| Runtime | Docker Compose |
| API | REST / OpenAPI |
| Quality | JUnit, Testcontainers, frontend tests, GitHub Actions |

Architecture target: **modular monolith in one monorepo**, with clear domain boundaries.

## Planned repository layout
```text
backend/                 Spring Boot application
frontend/                Angular application
infra/                   Docker and deployment configuration
docs/architecture/       ADRs and technical design
docs/requirements/       Scope, BiVo mapping and release plans
docs/process/            Engineering workflow
agents/                  Role-specific agent instructions
.github/workflows/       CI checks
AGENTS.md                Shared agent guardrails
STEPS.md                 Agent execution sequence
```
Directories will be added as implementation begins.

## Roadmap (indicative, weekly milestones)

Detailed release outcomes and acceptance criteria are maintained in [RELEASES.md](docs/requirements/RELEASES.md).
| Release | Focus |
| --- | --- |
| 0.1 | Foundation: skeleton, Docker, database, CI, roles |
| 0.2 | BiVo learning goals, semester and individual learning paths |
| 0.3 | Daily wrap-up, learning journal, focus sessions |
| 0.4 | Jira/Git integration and evidence links |
| 0.5 | Gamification, skill tree and first learning mini-game |
| 0.6 | Mentoring, quizzes, feedback and semester reports |
| 0.7 | Context-aware, staged hints and optional AI assistance |
| 0.8 | QV/IPA readiness and mock assessments |
| 1.0 | Operational hardening, accessibility, privacy, backup and exports |

Milestones are **targets, not commitments**. Release gates are based on tests, review, security and actual usability.

## Working with AI agents
Read [STEPS.md](STEPS.md) and [AGENTS.md](AGENTS.md) before changing code.

**Every implementation stage:** Plan → Develop → Review → Test → Human-approved Merge → Next stage.

## Public repository and data protection
Never commit real apprentice identities, grades, personal reflections, confidential company information, credentials, or proprietary DLP code. Use fictional seed data. Respect Swiss data-protection requirements when designing any tracking, reports or AI integration.

## Contributing
Open an issue describing the problem and acceptance criteria, then submit a focused pull request. All changes require review and passing checks. See [STEPS.md](STEPS.md).

## License
MIT. See [LICENSE](LICENSE).
