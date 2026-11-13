# BB-NATOR Release Targets and Agent Branch Plan

**Product goal:** See [PRODUCT_VISION.md](PRODUCT_VISION.md), [BB_COCKPIT.md](BB_COCKPIT.md), and [UX_WIREFRAMES.md](../design/UX_WIREFRAMES.md). Each release is a usable increment toward a complete, privacy-conscious vocational learning and development cockpit. Weekly milestones are aspirational, not guaranteed.

| Release | Reserved branch | Product target / demo | Acceptance gate |
| --- | --- | --- | --- |
| **v0.1 Foundation** | `release/0.1-foundation` | Working Angular + Spring Boot + PostgreSQL monorepo, Docker Compose, Flyway, fail-closed access baseline (OIDC/RBAC pending), initial learner/mentor preview shells including visible task creation entry point, CI. | Local startup, tests, documented API and security boundaries. No personal or sample data yet. |
| **v0.2 Seven Perspectives** | `release/0.2-learning-path` | **LDAP sign-in with local roles, tenant isolation, protection classes and access log ([ADR-0002](../architecture/ADR-0002-identity-tenancy-data-protection.md), [PRIVACY.md](PRIVACY.md))**; synthetic seed data; versioned official BiVo workplace goals plus **company**, **mentor vision**, **apprentice wishes**, **task learning path**, **üK**, **school**; eight-semester roadmap, individual plans, goal relationships, **functional mentor cockpit and internal task authoring/assignment**. | A fictional learner's plan shows all seven perspectives; mentor can create, save and assign an internal learning task linked to goals, identify missing mandatory goals and review a learner/task overview. Role and tenant boundaries are covered by negative tests. |
| **v0.3 Daily Companion** | `release/0.3-daily-companion` | Daily goals, “What did I achieve today?”, learning journal, optional focus timer and learning-session tracking, personal progress charts. | Learner completes a day and sees a history; time types remain distinct; opt-in/privacy verified. |
| **v0.4 Connected Learning** | `release/0.4-connected-learning` | Jira EDU create/link/publish from mentor task wizard, issue status sync, GitLab MRs/reviews/pipelines as evidence; time-tracking integration contract and optional approved read-only adapter; school/üK manual imports. | Mentor can publish/link a Jira issue with confirmation; linked task → goal → potential evidence visible; safe sync/retry/error handling; no duplicate source-of-truth. |
| **v0.5 Gamification** | `release/0.5-gamification` | Skill tree, XP, badges, focus milestones, first Bug Hunter mini-game and knowledge-check foundation. | Rewards based on learning activities, no public rankings, no grades inferred from XP/time. |
| **v0.6 Mentoring & Documentation** | `release/0.6-mentoring` | Knowledge-check question bank, attempts, mentor evidence verification, BB feedback, check-ins, formal semester Bildungsberichte, learning documentation and protected uploads for certificates/school records. | Mentor can review evidence, create a report and export it; permissions tested; school/üK data provenance clear. |
| **v0.7 Smart Learning** | `release/0.7-smart-learning` | Context-aware multi-level hints for Jira tickets, mentor-approved hint library, optional AI-generated suggestions, adaptive next-task recommendations. | Hints support thinking without revealing full solutions; safe context filtering, audit and human control. |
| **v0.8 QV Readiness** | `release/0.8-qv-readiness` | IPA/mock IPA milestones, Fachgespräch practice, official-goal gap analysis, QV preparation dashboard, individualized improvement plans. | Readiness evidence and gaps are transparent, not a fabricated pass probability. |
| **v1.0 Production Platform** | `release/1.0-platform` | Polished apprentice + operational BB cockpit and development dashboards, consolidated reporting across seven perspectives, secure deployment, accessibility, audit, retention, backup/restore and export. | End-to-end demo from goal → task → evidence → reflection → mentor review → report; operational/security checks passed. |

## Agent release workflow
1. **Planning:** read `README.md`, `AGENTS.md`, `STEPS.md`, `docs/architecture/TECHNOLOGY.md`, `PRODUCT_VISION.md`, this file; produce small tasks, acceptance tests, dependencies, risks. Obtain approval.
2. **Branching:** create the named release branch from current `main` **only when that release starts**. Create `feat/<version>-<ticket>-<slug>` from the active release branch.
3. **Developing:** implement small changes with tests, migrations, documentation and meaningful commits.
4. **Reviewing:** independent review of PR targeting release branch; resolve blocking findings.
5. **Testing:** run unit/integration/UI tests, authorization/privacy cases and Docker smoke tests as applicable; report exact results.
6. **Merging:** human-approved feature PRs into release branch, then human-approved release PR into `main`; tag `v<version>`.
7. **Next step:** update release notes, verify outcomes, select next feature/release.

**Do not create all future branches now**: reserved branch names are specifications, not live Git refs. No autonomous merges to main.

## Cross-release nonfunctional requirements
- Domain and application layers independent of Spring/JPA; frontend feature-oriented architecture.
- Versioned learning goals and evidence, not simple completion counters.
- Fail-closed access from v0.1; role-based access, tenant isolation and data minimization from v0.2 ([PRIVACY.md](PRIVACY.md)).
- Privacy and human oversight for reflections, time records, evaluations and AI.
- Accessible, responsive Angular UI and reliable automated tests.
