# BB-NATOR Integration & Reuse Plan
Status: **architecture proposal**. No application code or live integration is implemented here.

## 1. Decision summary

**BB-NATOR connects directly to Jira and GitLab. It must NOT proxy ordinary issue/MR reads through Tinyops.** Tinyops is a separate personal engineering cockpit with different users, query scope and workflows. Reuse its *ideas, test scenarios and security practices*, not its runtime as a data dependency.

**BCS/Projectron is the exception:** use the dedicated BCS Agent / middleware API for authorized time/absence/balance data instead of implementing fragile Projectron SOAP/browser-session logic twice. BB-NATOR should never call Tinyops as a BCS proxy. Initially BCS is **read-only and optional**.

One deployment per product and explicit domain-specific APIs. No shared production database, no shelling into another project's repo.

## 2. Repository evidence versus assumptions

### Tinyops (verified from AdBuch/tinyops, main, 2026-11-12)
- Local Python 3.13 application, mostly standard library, built as a **modularized monolith**.
- `server.py` is approximately 4,970 lines and contains `GitLabClient`, `JiraClient`, `App`, HTTP handler and orchestration; other modules include `agents.py`, `github.py`, `bcs.py`, `timesheet.py`, `net.py`, `secretstore.py`.
- Direct GitLab/Jira HTTP access; GitHub read-only integration in `github.py`; per-user local settings/secrets.
- Tinyops `bcs.py` is an **HTTP client for a separate middleware**, not the BCS integration engine. It allows loopback URLs only, default `http://127.0.0.1:3210`.
- Observed BCS-client endpoints: `GET /api/v1/reports/apprentices`, `GET /api/v1/reports/dashboard`, `GET /api/v1/reports/time-balance`, `GET /api/v1/overviews`, `GET /api/v1/days/{date}`, plus draft preview/commit.
- Important: Tinyops v0.29 documentation says BCS write integration was tested against a mock/demo, **not a confirmed real BCS deployment**.

### BCS Agent (from previous project design; **not independently code-audited**)
- Described as modular TypeScript/Node.js software with web UI, HTTP workflow wrapper and MCP interface, and Projectron adapters.
- Read of live Projectron may involve supported SOAP operations when available and a local browser-session adapter otherwise.
- Its exact architecture, deployment topology, access policy and WSDL rights **must be verified against the local source** `D:\bcs\bcs-agent-client` or a subsequently accessible repository.
- No verified GitHub repository under `AdBuch` was found for this BCS agent. Do not treat Tinyops' client/test double as the BCS server implementation.

### BB-NATOR (target)
- Java 21, Spring Boot modular monolith + Spring Data JPA + PostgreSQL, Angular, Docker Compose.
- Onion-style feature modules with dependency-inward architecture described in `TECHNOLOGY.md`.
- Multiple apprentices and authorized trainers, education goals, evidence and reports; significantly different query model from Tinyops.

## 3. System boundaries / topology

```text
                          ┌────────────────────────┐
   Apprentice / Mentor ───▶│ BB-NATOR               │
                          │ Angular + Spring Boot  │
                          │ own PostgreSQL         │
                          └───┬────────┬────────┬──┘
                              │        │        │
                   direct     │        │        │ approved read-only
                              ▼        ▼        ▼
                         Jira API  GitLab API  BCS Agent API
                                                 │
                                                 ▼
                                               Projectron

   Engineer ─────────────▶ Tinyops (Python)
                           ├────── Jira API (direct, personal scope)
                           ├────── GitLab/GitHub API (direct, personal scope)
                           └────── BCS Agent API (its existing loopback client)
```

Do not share BB-NATOR's learner DB or Tinyops' local SQLite/config files. Shared *source systems* do not imply shared credentials or blanket access.

## 4. Reuse choices

| Concern | Existing Tinyops/BCS experience | BB-NATOR choice | Shared asset |
| --- | --- | --- | --- |
| GitLab | Tinyops `GitLabClient`, MR/reviews/pipelines | **Direct Spring GitLab adapter** with apprentice/group/project scope | Query semantics, security tests, endpoint examples, normalized event concepts |
| Jira EDU | Tinyops `JiraClient`, JQL, link by ticket keys | **Direct Spring Jira adapter** and controlled create/link | JQL patterns, Jira field discovery, test fixtures, workflow mapping |
| GitHub (optional) | Tinyops read-only `github.py` | Direct GitHub adapter *if* educational projects use GitHub | Provider-neutral evidence model |
| BCS / Projectron | BCS Agent workflow API + Tinyops `bcs.py` client | **Direct BB-NATOR → BCS Agent API**, read-only learner balances/summaries initially | Versioned OpenAPI/JSON Schema contracts, error semantics |
| Focus sessions | Tinyops `timesheet.py` | Own privacy-first focus and journal model | UX insights; do **not** reuse time-booking logic |
| AI agent execution | Tinyops `agents.py` | Separate optional integration **later**, not an MVP dependency | Security policies, invocation contract, review workflow |
| Credentials/TLS | Tinyops `net.py`, `secretstore.py` | Spring Security + secret provider + HTTPS verification | Test cases and policy, **not Python code copied to Java** |
| Angular frontend | Tinyops own UI, BCS web UI | Native Angular feature modules | Design ideas and text contracts; no framework-foreign component imports |
| Domain/competencies | Not present as BB-NATOR business domain | BB-NATOR-owned | No shared DB/schema |

Cross-language libraries are not the default sharing mechanism: Python, TypeScript and Java cannot meaningfully share one business-client package without maintaining bindings. **Share small versioned HTTP/JSON/OpenAPI contracts**, sample fixtures, vocabulary and security rules. Keep API contracts in the owning project's repository, mirror validated consumer snapshots/tests only if required. Do not create a fourth "shared-core" repository prematurely.

## 5. GitLab/Jira direct adapter design

```text
modules/evidence/
  domain/                 Evidence, ExternalIdentity, Reference, Provenance
  application/            CollectEvidence, LinkTicket, ReviewEvidence
  ports/                  IssueTrackerPort, CodeHostPort
  infrastructure/jira/    JiraRestAdapter, StatusMapper
  infrastructure/gitlab/  GitLabRestAdapter, MrMapper
  interfaces/rest/        BB-facing DTOs and endpoints
```

Example use cases:
- Find Jira EDU assignments by project/issue type, mapped users and training window.
- Find GitLab MRs authored/reviewed by authorized apprentices across allowlisted group/projects.
- Link Jira key ↔ branch/MR title/commit when valid; display confidence and require mentor confirmation on ambiguous matches.
- Import only metadata needed for evidence: project, issue/MR key, status, author, review state, pipeline status, link, timestamps; do not ingest proprietary source code, diffs, comments or secrets by default.
- Cache/synchronize with safe backoff, bounded pagination, incremental cursor or webhook when approved; every record has `source`, `externalId`, `observedAt` and freshness.
- Cross-project author IDs mapped with explicit mentor-administered `ExternalIdentityMapping`; never infer person identity from name collisions.
- Jira remains authoritative for tickets and transitions; GitLab remains authoritative for MRs/pipelines; BB-NATOR owns learning-goal mapping, verification and mentoring.

**Authorization model:** scope queries by relevant apprentice/cohort and allowlisted projects, enforce BB-NATOR roles on every result, request least-privilege credentials. Distinguish personally delegated tokens from a service account; do not assume one personal Tinyops token gives permitted access to all apprentice work.

## 6. BCS contract and network design

BCS Agent is the integration boundary because it knows Projectron time-booking rules and login protocol. BB-NATOR's needs differ from Tinyops' personal time-booking flow.

**Initial read-only BB use cases** (subject to access permissions and actual API availability):
1. Apprentice week/month availability or booked-time summary.
2. Vacation balance and planned leave only when educational scheduling legitimately needs it.
3. An authorized BB overview of participating apprentices; do not retrieve every employee.
4. Optional educational planning hints (course day conflicts), **not automatic competency grading**.

Do not write time automatically. Focus sessions, learning estimates and official BCS bookings are three different data categories.

**Deployment issue:** Tinyops currently restricts BCS endpoints to loopback. BB-NATOR in another Docker container/host cannot simply reuse that loopback client. Validate whether the BCS Agent can be deployed in the same private Docker network or behind a deliberately authorized HTTPS gateway. Do **not** relax Tinyops' loopback protection, expose Projectron cookies, mount Tinyops secret files or open middleware to the internet.

**Minimum contract for BB-NATOR**:
- `GET /api/v1/reports/apprentices` for authorized identities
- `GET /api/v1/reports/dashboard?memberId=...` for allowed learner summary
- `GET /api/v1/overviews?date=...&scope=week|month` only if account/scope and semantics meet BB needs
- Consistent `asOf`, `source`, authorization/forbidden/unavailable statuses, and stable IDs.

If the existing BCS Agent cannot safely expose required multi-user reads, add a narrow read-only endpoint **to that service**; do not rebuild Projectron access inside Spring Boot. Start with synthetic/mock data while permissions are settled.

## 7. Shared contracts and independent changes

Adopt small integration contracts with:
- `externalSystem`, `externalId`, `externalUrl`, `externalActorId`, `eventTime`, `observedAt`
- Provider-specific metadata kept separate, not hidden by a misleading universal model.
- Explicit `EmploymentTime`, `LearningSession`, `FocusSession` types; never sum as if equivalent.
- Contract tests: sanitized JSON fixtures, unknown fields, pagination, 401/403/429/5xx, outages, stale data.
- Independent versioning and backwards compatibility; no deployment lockstep.

### Ownership
- **BB-NATOR:** education plans, assignments, competency evidence, formative evaluation, reports.
- **Tinyops:** personal engineering/AI workflows and personal productivity.
- **BCS Agent:** Projectron protocol, official time data and controlled booking workflows.
- **Jira/GitLab:** canonical tickets, Git history and reviews.

## 8. Proposed delivery sequence and acceptance gates

### v0.1: prepare ports
- `IssueTrackerPort`, `CodeHostPort`, `TimeSummaryPort` interfaces; synthetic adapters.
- Architecture tests prohibiting domain → Spring/JPA/external HTTP dependencies.
- No live tokens in source or frontend.

### v0.2: mentor task management
- Create, assign and map learning tasks to multiple educational goals.
- Stable IDs for future Jira links; tasks work without external systems.
- Apprentice scoped identities established.

### v0.3: daily companion
- Daily reflection and voluntary focus time. **Never automatically book BCS time.**
- Distinguish learning activity from actual work time and evidenced skill.

### v0.4: direct Jira + GitLab
- Configure allowlisted Jira EDU projects and GitLab groups/projects.
- Separate read-only synchronization of tickets, MRs and pipelines.
- Mentor-reviewed matching to educational tasks; authorization & pagination tests.
- Jira "create/link" only with explicit approval, configured scopes and duplicate-safe handling.
- BCS: contract prototype/read-only mock only unless actual middleware access is validated.

### v0.5–v0.6: development and educational reporting
- Knowledge checks, evidence dashboard and formal mentor feedback.
- Optional BCS read-only summaries **after** real middleware audit, authorization and network review.
- Automated school/üK/HR data ingestion must be separately authorized.

### v0.7–v1.0: enhance and harden
- Context-aware ticket hints can reference metadata/learning goals; source code only by explicit policy.
- Source freshness, failure dashboards, audit, user-data retention/export, secret rotation and backup/restore.
- No automatic employment performance scores from Jira, GitLab or time records.

## 9. Target architecture decisions for human review
1. **YES:** separate Java direct adapters for Jira/GitLab; do not route through Tinyops.
2. **YES:** reuse BCS Agent as the *only* Projectron integration boundary, if API access is secure and authorized.
3. **NO:** copy Python code directly into Java or couple Spring to Tinyops' SQLite.
4. **NO:** shared write token or personal token with blanket access to apprentices.
5. **NO:** automatic linking of all commits/MRs to a person based solely on name or ticket text.
6. **LATER:** optional link from BB-NATOR to a Tinyops agent workflow, using an explicit narrow API, only for sanctioned development projects.

## 10. Verification checklist before implementing real connectors
- [ ] Inspect actual BCS-Agent source, API specification, auth roles and deployment.
- [ ] Confirm Jira EDU project, issue types, required fields and permitted read/write identities.
- [ ] Confirm GitLab group/project allowlist and apprentice user ID mapping.
- [ ] Agree access policy for official time, vacation and school/üK data.
- [ ] Specify source freshness and failure behavior; no fake "live" indicators.
- [ ] Create sanitized contract fixtures and test matrices.
- [ ] Review shared schemas for minimum data necessary and retention.
- [ ] Keep public GitHub repo free of real personal or proprietary data.

References: [PRODUCT_VISION.md](../requirements/PRODUCT_VISION.md), [RELEASES.md](../requirements/RELEASES.md), [TECHNOLOGY.md](TECHNOLOGY.md), [BB_COCKPIT.md](../requirements/BB_COCKPIT.md).
