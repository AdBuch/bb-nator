# Release Specification and Branch Plan

All branch names are **reserved conventions**, not necessarily created Git refs. Release branches are created only when work on that release starts. One active release branch at a time; small feature branches may branch from the active release branch.

| Version | Release branch | Scope / release gate |
| --- | --- | --- |
| v0.1 Foundation | `release/0.1-foundation` | Spring Boot/Angular skeleton, PostgreSQL/Flyway, Docker, CI and baseline tests |
| v0.2 Learning Path | `release/0.2-learning-path` | Versioned BiVo goals, eight-semester plan, personal goals and competency mapping |
| v0.3 Daily Companion | `release/0.3-daily-companion` | Daily wrap-up, journal, optional focus timer and privacy controls |
| v0.4 Connected Learning | `release/0.4-connected-learning` | Jira EDU/Git integration, evidence links and sync error handling |
| v0.5 Gamification | `release/0.5-gamification` | XP, badges, skill tree and first learning mini-game |
| v0.6 Mentoring | `release/0.6-mentoring` | Quizzes, mentoring feedback, check-ins and formal semester reports |
| v0.7 Smart Learning | `release/0.7-smart-learning` | Three-stage ticket hints, optional AI provider, safe context and approval |
| v0.8 QV Readiness | `release/0.8-qv-readiness` | IPA practice, QV readiness, interview preparation and gap analysis |
| v1.0 Platform | `release/1.0-platform` | Security/privacy audit, accessibility, backup/restore, exports, operational docs |

## Workflow per release
1. **Planning:** start from latest `main`, create the named release branch, plan small tasks and obtain approval.
2. **Developing:** create `feat/<version>-<ticket>-<slug>` from the release branch. Commit in small units.
3. **Reviewing:** open PR targeting the release branch; independent review, fix findings.
4. **Testing:** run automated and functional tests; record evidence.
5. **Merging:** human-approved feature PR into release branch; then human-approved release PR into `main`.
6. **Next step:** tag `v<version>` on main after release, update docs, then start next release.

**Never** create all release branches at once: stale long-lived branches increase merge conflicts. Names are fixed here for reproducibility.

Read [TECHNOLOGY.md](../architecture/TECHNOLOGY.md), [STEPS.md](../../STEPS.md), and [AGENTS.md](../../AGENTS.md).
