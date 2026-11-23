# Product backlog and requirements reconciliation

**Reviewed:** 2026-11-23 · **Status:** consolidated planning, not an implementation claim. Existing accepted decisions remain authoritative in `docs/requirements/`, `docs/architecture/` and `docs/features/`. The roadmap defines priorities; no automatic acceptance of newly proposed ideas.

## Product definition and educational goals

BB-NATOR is an **actionable educational cockpit for mentors and apprentices**: it plans a four-year apprenticeship, gives and follows up learning tasks, measures **verified progress**, supports reflection and prepares official educational documentation and QV/IPA. It is not a clone of Jira, GitLab, BCS, the school or üK systems.

**Seven inputs, one individual plan:** (1) official BiVo 2021 workplace goals; (2) Betrieb; (3) Berufsbildner's development vision; (4) apprentice aspirations; (5) company task-learning path; (6) üK; (7) vocational school. Every goal identifies its authoritative source and version. Never invent official codes, school grades or BiVo equivalences. Official outcome evidence is distinct from activity indicators.

## Wishes and placement (no lost ideas)

| Desired outcome / idea | Owner feature(s) | Planned stage | Source classification |
| --- | --- | --- | --- |
| Mentor sees all apprentices, blockers, follow-ups, missing goals, reviews, next actions | F09, F12, F26 | v0.2 core / 1.0 analytics | Existing user requirement |
| Mentor creates **new tasks** and assigns individually or to several apprentices, with purpose, prerequisites, acceptance criteria | F10, F29 | v0.2 / v0.3 templates | Existing user requirement |
| Learner can choose interests and a project path; mentor can approve alignment | F07, F08, F29 | v0.2–0.3 | Existing user requirement |
| Full official BiVo 2021 APP coverage + semester gaps + evidence, separate school/üK inputs | F06, F07, F08, F11, F19 | v0.2–0.4 | Existing user requirement; official content still to be verified |
| **Eight-semester narrative**: S1 programming, S2 AI models, S3 isolated practical projects, S4 testing, S5 prompting + business readiness, S6 daily engineering work, S7 responsibility, S8 IPA | F08, F29 | v0.2 model / later curriculum content | Earlier user-approved *company learning path*, NOT official BiVo mapping |
| Coding progression: canvas/figures → Maven → JUnit → Git → REST/API/JSON → AI API | F29 | v0.3 task library | Earlier user exercises; illustrative templates |
| Technical growth: Java, Spring, Angular, Git, Maven, containers, Kubernetes, CI/CD, secure code | F29, F11 | v0.3–0.6 | Company-specific content, generic safe examples |
| Practice project paths: small API app; Go/MailHog proof of concept; integration testing; AI/MCP agent lab | F29, F31 | v0.3–0.7 | Earlier discussed examples, not current assignments |
| Day/weekly experience: learner sees next task, journals progress, reports blockers, daily 'Was erreicht?' | F13, F14 | v0.3 | Existing user requirement |
| Voluntary focus timer from very short intervals (15 seconds) toward ~30 min | F15 | v0.3 | Earlier user idea, optional/adaptive |
| Link Jira EDU task, read GitLab MR/CI review evidence **directly**, not through Tinyops | F16, F17, F11 | v0.4 | Architectural decision |
| BCS-Agent read-only time context + approved school/üK information; **no attendance performance scoring** | F18, F19, F30 | v0.4+ | Architectural decision; actual BCS Agent not audited |
| Evidence-backed skill matrix: independent work, code review, testing, quality, QV readiness | F11, F12, F26 | v0.2 basics / v1.0 full | Existing user requirement |
| Knowledge questions and contextual checks; no false equivalence to demonstrated skill | F21 | v0.5–0.6 | Existing user requirement |
| XP, badges, skill-tree, Bug Hunter mini-game, no ranking | F20 | v0.5 | Existing user requirement |
| Mentor check-ins, feedback, agreements, semester Bildungsbericht, certificates/documents | F22, F23 | v0.6 | Existing user requirement |
| Help ladder: reflection prompt → approach → technical pointer; optional local AI, controlled prompts | F24, F31 | v0.7 | Existing requirement; provider and security policy open |
| Mock IPA, QV checklist, Fachgespräch, expert-verified gap analysis | F25 | v0.8 | Existing user requirement |
| Dashboard for observed development across eight semesters and seven perspectives | F26, F09 | v1.0 full; useful basics earlier | Existing requirement |
| Starter updates via Git; local admin 'Install update' button, safe deployment/rollback | F27, F28 | v0.2a + v1.0 hardening | Implemented demo; production improvements open |
| Check-in/course/availability planning in one read-only calendar view | F30 | v0.4 candidate | Derived UX proposal, **not approved scope yet** |

## Explicitly separate evidence from activity

- **Activity:** focused minutes, number of tasks or commits, XP, Jira progress. Never an employment grade.
- **Potential evidence:** submitted demo, reviewed MR, test run, quiz explanation, mentor observation.
- **Verified learning:** assessed against goal, assessor, dated reasoning, supporting evidence. Completion of a Jira ticket alone is insufficient.
- **Official training assessment:** stays with the responsible human and applicable original forms / procedures; BB-NATOR only prepares reviewable drafts.

## MVP slices and non-goals

**Current v0.1:** technology foundation on main. **v0.2a demo:** 4 *fictional* learners, manually assigned demo tasks and local updater in release branch; no safe real-data pilot. **v0.2 pilot gate:** LDAP/local roles, active account/mentor assignment, tenant RLS with unprivileged runtime role, protection classes and access logs, usable task workflow with goal mappings and negative auth tests. **Next:** v0.3 learner daily use, then v0.4 Jira/GitLab/school/üK coordination. Do not ship real learner information behind the demo profile.

**Not MVP:** automatic QV pass prediction, autonomous competence grades, copying proprietary code into public samples, public leaderboards, full BCS write access, a public Docker-control API.

## Product and safety questions needing product-owner decisions

1. Stable LDAP identifier and directory configuration, bootstrap/admin rights, login lockout strategy.
2. Permissions for mentor-to-learner mapping, class D visibility, parental-information process if relevant.
3. Who curates official BiVo sources and verifies identifiers, versions and copyright status?
4. Internal 0–4 development rubric definitions, required evidence and mentor approval workflow.
5. Jira EDU project/issue mapping and allowed GitLab groups/projects/tokens.
6. Actual BCS Agent repository/API/tenant scoping and school/üK data-sharing approvals.
7. Curriculum templates to offer by default versus company-specific templates.
8. Production updater rollback, signed release provenance and protected secret handling.

See [feature index](../features/README.md), [roadmap](../ROADMAP.md), [branch audit](BRANCH_AUDIT_2026-11-23.md).

## Präzisierung: fünf Vorschläge für die v0.2-Fachfreigabe

[RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md](../requirements/RELEASE_0_2_MVP_APPROVAL_PROPOSAL.md) beschreibt Abläufe, Daten, Negativtests und noch zu genehmigende Entscheidungen für F01, F06, F11, F29/F10 und die v0.2-MVP-Grenze. Die **T01–T05**-Vorlagen werden als möglicher kleiner MVP-Inhalt vorgeschlagen, nicht als freigegebene Produktanforderung. Die Quellen-, Datenschutz-, Betriebs- und Berechtigungsentscheidungen bleiben ausdrücklich **offen**.
