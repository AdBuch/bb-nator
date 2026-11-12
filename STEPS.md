# BB-NATOR: Agent Execution Playbook

This is the **operational entry point** for Claude, Codex, Goose and other coding agents. Read [AGENTS.md](AGENTS.md) first.

## Active foundation branch
Release `v0.1` uses `release/0.1-foundation`. A first runnable scaffold is committed on that branch; the next agent task is to **verify and repair the CI + Docker build** against [the v0.1 checklist](docs/requirements/RELEASE_0_1.md), not to create the scaffold again. Follow `agents/PLANNER.md` before further implementation.

## Architecture and release branch conventions
Read [TECHNOLOGY.md](docs/architecture/TECHNOLOGY.md) and [RELEASES.md](docs/requirements/RELEASES.md) before planning. Use the named release branch from RELEASES.md, and feature branches from that release branch. Never create all future release branches in advance.

## Golden loop: one feature at a time
**Planning → Developing → Reviewing → Testing → Human-approved Merging → Next Step**

Do not skip gates or implement an entire roadmap release in one uncontrolled batch.

### 0. Inspect
1. Read README.md, AGENTS.md, this file, relevant docs and existing code.
2. Inspect the current branch, working tree and CI state.
3. Identify the next unfinished release goal; do not assume code exists.
4. Check for conflicting work before editing.

### 1. Planning (Planner)
- Define user value, scope, non-goals and dependencies.
- Map relevant official BiVo 2021 objectives where applicable; do not invent legal requirements.
- Specify API/data changes, migrations, UI states, security and privacy implications.
- Break work into small reviewable tasks with acceptance criteria and tests.
- Document risks and unknowns. **Stop for human approval** before coding if the plan changes architecture, scope or sensitive data processing.

**Output:** short implementation plan, task checklist, acceptance criteria and proposed branch name.

### 2. Developing (Developer)
- Create a feature branch from current main: `feat/<issue>-<short-name>`.
- Implement only the approved task; keep changes cohesive.
- Write or update tests with code. Add migrations for schema changes.
- Commit meaningful increments using conventional commits.
- Never push credentials, personal data or proprietary source code.
- No direct commits to main.

**Output:** code, tests, updated docs and concise implementation notes.

### 3. Reviewing (Independent reviewer)
- Review diff against acceptance criteria, architectural boundaries and security requirements.
- Check correctness, error handling, accessibility, performance and maintainability.
- Examine data exposure, authorization, auditability and AI prompt/data leakage.
- Record findings by severity: blocker, major, minor.
- Do not silently rewrite the developer's work during independent review.

**Output:** findings and explicit pass/fail recommendation.

### 4. Testing (Tester)
- Run relevant unit, integration, API and UI tests.
- Run builds, static checks and security checks where configured.
- Test negative paths and permission boundaries.
- Validate Docker startup for infrastructure changes.
- Report exact commands, results and limitations; never claim tests passed if not run.

**Output:** test evidence and pass/fail gate.

### 5. Merging (Human gate)
- Open a PR referencing the task, acceptance criteria and test results.
- Require passing CI, no blocking review findings and human approval.
- **Agents must not merge to main autonomously.**
- After approval, a human merges and confirms deployment if applicable.

### 6. Next Step
- Update task status and release checklist.
- Summarize delivered functionality, known limitations and follow-ups.
- Planner selects the next small task; repeat the loop.

## Definition of Done
- [ ] Acceptance criteria demonstrated
- [ ] Automated tests added/updated and run successfully
- [ ] Build and applicable CI checks pass
- [ ] Security/privacy and authorization considered
- [ ] Documentation and API contracts updated
- [ ] Independent review completed, blockers resolved
- [ ] Human approval recorded before merge

## First release: v0.1 Foundation
**Plan before coding**:
1. Establish monorepo folders, Maven backend and Angular frontend.
2. Define domain module boundaries and API conventions.
3. Add PostgreSQL and Flyway via Docker Compose.
4. Add a minimal health endpoint and working frontend shell.
5. Add baseline automated tests and GitHub Actions.
6. Add environment examples, setup guide and safe local seed data.
7. Demonstrate a clean local startup and successful CI.

**First agent instruction:** Inspect the repository, propose the v0.1 task breakdown and acceptance criteria, then stop for approval. Do not implement yet.

## Later release gates
- v0.2: complete, versioned official goal import and individual mapping.
- v0.3: journal/focus features with opt-in tracking and no surveillance.
- v0.4: least-privilege integration, no credentials in source control.
- v0.5: optional gamification; no ranking of minors or equating XP with skill.
- v0.6: protected mentor feedback and formal report workflows.
- v0.7: staged hints, human oversight and no unauthorized external AI data transfer.
- v0.8: QV readiness aligned with current authoritative criteria.
- v1.0: security, accessibility, retention, export, backup and recovery validated.
