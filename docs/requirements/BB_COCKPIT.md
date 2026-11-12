# BB-NATOR: Berufsbildner Cockpit and Learning Task Management

**Status:** product specification / proposed implementation; no claim of existing UI or integrations.
**Owner:** Berufsbildner (BB); **related users:** apprentices, deputy mentors, administrators.
**Source of truth:** official BiVo documents for educational goals, Jira for linked Jira tickets, GitLab for code/merge requests, approved time-tracking systems for official work hours.

## 1. Product purpose
The Berufsbildner cockpit is the **daily command center** for training four or more software-development apprentices: *see progress, spot gaps, assign meaningful work, follow up, coach, collect evidence and prepare semester assessments*. It is **not merely an analytics dashboard**.

In under a minute a mentor should answer:
1. Who needs my support today and why?
2. What is each apprentice currently working on?
3. Which training objectives are due, missing, or not yet verified?
4. What would be a suitable next learning task?
5. What do I need to review or approve?
6. How have independence, practical competency and learning confidence changed?

Success is not high ticket throughput, XP accumulation or focus minutes. Success is **verified competence and growing independence**, anchored in the apprenticeship learning plan.

## 2. Seven perspectives, one coherent plan

| Source / perspective | Content and authority | Typical evidence and use |
| --- | --- | --- |
| **Official Bildungsplan / BiVo 2021** | Mandatory workplace Handlungskompetenzen/Leistungsziele, identified by source and version | Educational coverage and verification, QV gap analysis |
| **Betrieb** | Relevant technologies, DLP domain, team practices, coding standards and safe production readiness | Productive tasks, code reviews, team feedback |
| **Berufsbildner-Vision** | Responsible independent engineering, critical thinking, testing, AI literacy, security and communication | Structured mentor assessments and growth goals |
| **Wünsche der Lernenden** | Interests, project ideas, electives and individual aspirations | Learner-chosen tasks, projects and agreed objectives |
| **Betrieblicher Lehrpfad / Task-Lehrplan** | Eight-semester roadmap, milestones, skill prerequisites and EDU tasks | Task completion, practical evidence and milestone checks |
| **Überbetriebliche Kurse (üK)** | Modules and external learning outcomes; imported/recorded with provenance | ÜK completion, support and coordination; external authority remains üK |
| **Berufsfachschule** | Curriculum/modules, grades and assessments only when sharing is authorized | Support, semester coordination and risk identification |

Use a **many-to-many mapping**: one task can support several goals from different perspectives, one goal can be supported by many tasks. Cross-source equivalence is **explicitly reviewed**, not inferred from similar names. Goal records must carry source, official identifiers where available, version and provenance. The portal does not replace official school, üK or examination records.

## 3. Three primary UX surfaces

### A. Berufsbildner overview (/bb/dashboard)
**Top:** learner/semester/date filters, persistent **+ Neue Lernaufgabe**, quick search, notifications.

**KPI summary (contextual, never a fake all-purpose success score):**
- Apprentices and active/paused assignments
- Open tasks by status and awaiting review
- Mandatory goals: not planned / planned / practiced / evidenced / mentor-verified
- Upcoming milestones and overdue follow-ups
- Tasks blocked, missing evidence or requests for help
- Upcoming formal reports or check-ins

**Main panels:**
1. **Learner cards/table:** apprenticeship year, currently assigned tasks, next milestone, due reviews, last check-in, competency evidence indicators and support requests. Quick actions: **Profil**, **Aufgabe zuweisen**, **Feedback**, **Gespräch**.
2. **Needs attention queue:** blockers or help requests, unmet prerequisites, unreviewed MRs, overdue reflections/reports, goals not addressed. Each item has reason, owner and next action; avoid automated stigmatizing labels.
3. **Training coverage heatmap:** seven perspectives as filters, mandatory BiVo coverage by competency area and semester, color with labels and accessible text; distinguish training *planned* from *verified*.
4. **Task board:** Draft → Ready → Assigned → In Progress → In Review → Verified/Done; blocker, due dates and responsible mentor. External Jira state is mapped, not overwritten silently.
5. **Upcoming:** school/üK modules, milestones, mentoring conversations, IPA checkpoints.
6. **Activity and outcomes:** Jira changes, GitLab MRs/pipelines, journal entries shared for review, knowledge checks, completed evidence. Activity counts are contextual, **not performance ratings**.
7. **Quick create:** new learning task, check-in, evidence review, reminder/feedback.

**Empty/error states:** new organization with no apprentices; Jira not connected; missing official goal import; sync stale; data privacy blocked; zero training evidence. Every view states when data was last synchronized.

### B. Apprentice development profile (/bb/apprentices/:id)
- Hero: learner, apprenticeship year/semester, individualized plan and current learning objectives.
- **Development timeline:** eight-semester roadmap with baseline, evidence and planned next milestones.
- **Competency matrix:** domain skills and official BiVo criteria, last assessed level, target level, evidence and assessor.
- **Seven perspective tabs:** BiVo, Betrieb, Berufsbildner, Lernendenwünsche, Lehrpfad, üK, Schule.
- **Learning objectives radar/trend:** only for internally defined dimensions and thresholds; show actual evidence, trend and uncertainty; never conflate official goals with XP.
- Current/upcoming/completed task list with educational impact and mentor attention.
- GitLab/Jira evidence, associated MR test/review outcomes, knowledge checks, upcoming check-ins.
- Learning journal and daily reflections **only when shared** and authorized; learner retains drafts privately.
- Progress/history of focus and learning time **only on purpose-limited, opt-in basis**; no attendance or productivity inference.
- Reports, certificates/Zeugnisse, semester Bildungsberichte, supported document exports and access log.
- Quick action **+ Aufgabe zuweisen** preselecting the apprentice, plus **Review**, **Feedback**, **Gespräch planen**.

### C. Guided learning task authoring (/bb/tasks/new)
**Wizard steps:**
1. **Learning intention:** title, purpose ("why"), assignment type (training exercise / practical project / Jira EDU ticket / school-üK support / self-selected project), context and expected outcome.
2. **Educational alignment:** select one or more goals across the seven perspectives. Set primary objective, semester, difficulty, prerequisites, expected competence target, and how to verify learning. Suggest gaps, allow mentor override and audit.
3. **Task details:** description, deliverables, acceptance criteria, quality/security/tests, reference material, time estimate/story points (not forced), suggested milestones, dependencies and review checkpoints.
4. **Support plan:** hints by level (thinking prompt → method → technical pointer), feedback expectations, optional quiz, AI policy, whether links/resources may be exposed to learners.
5. **Assignment:** select apprentice(s), mentor, due date, planned start, available capacity and possible scheduling conflicts (school, üK, leave where authorized). Cohort tasks yield **individual assignment/progress records**, not a single shared competency grade.
6. **Preview & publish:** display goal mapping, intended development impact, evidence plan and scope. Publish to **portal learning task** OR create/link Jira EDU ticket when integration exists, with preview and human confirmation.

**Important:** "competency impact" on the screen is **expected learning opportunity**, not an automatic +12% skill increase. Completed ticket or passed pipeline creates potential evidence, not a verified learning goal. Mentor reviews demonstration and records competency evaluation separately.

**Task status workflow:** Draft → Ready → Assigned → In Progress ↔ Blocked → In Review → Done; optional Cancelled. Separate **evidence status**: Missing / Submitted / Reviewed / Verified / Needs More Work.

**Publishing to Jira (from v0.4):** explicit "Create in Jira" action, preview project/issue type/labels, validate permissions, create once using idempotency key, store Jira issue key+URL, reconcile failures and duplicates, and never overwrite edits without conflict awareness. Jira is authoritative for issue status/description; BB-NATOR owns learning-goal mappings, hints, reflections and mentor evaluation. Support linking an existing ticket as well. Before integration (v0.2) tasks can be created and assigned internally.

**Templates:** Java/OOP, Maven/JUnit, REST API, Angular, Git fundamentals, integration tests, GitLab MR, DevOps/Docker, security/PII, AI agents/MCP, DLP domain, IPA preparation, mentor-custom templates. All are editable.

## 4. Outcome-centric progress model
Avoid misleading single percentages without a documented denominator.

| Indicator | Measurement | Caveat |
| --- | --- | --- |
| Mandatory BiVo coverage | `verified goals / applicable goals`, plus separate planned/practiced/evidenced breakdown | Requires maintained applicability and mentor verification |
| Training plan progress | Agreed milestones reached versus planned; report overdue separately | Time plan changes must be visible |
| Competency development | Explicit 0–4 rubric with assessor, date, evidence, target and confidence | This is an internal growth rubric, not official QV grading |
| Tasks | By status, blocked age, awaiting-review age | Task counts do not equal competence |
| Evidence | Linked reviewed MRs, demos, reviews, projects, assessments | Link alone is not proof of understanding |
| Knowledge checks | Attempts, topic strengths/gaps and explanations | Knowledge checks are only one evidence type |
| Learner autonomy | Mentor-observed independence, support needs and ability to explain decisions | Use coaching, not punitive automated scoring |
| Learning/focus time | Voluntary self-reported learning sessions and focus intervals | Separate from HR attendance/time booking and strictly purpose-limited |
| School & üK | Modules/grades/status with provenance and permission | External result; absence may mean "not provided" |
| QV readiness | Checklist and evidence-backed qualitative status | No fabricated pass probability |

Default dashboard figures should use **synthetic demo data** until real data is explicitly connected. Provide detail drill-down for each indicator.

## 5. Mentor operating flows

**Morning (2–5 min):** open attention queue; check blocked learners, pending reviews and today's milestones → assign or adjust learning tasks.

**Weekly (10–20 min per learner):** learner updates journal; mentor reviews tasks, evidence and reflection; agree next task/next support level.

**Semester:** compare individual plan, BiVo coverage, work evidence, school/üK context and learner self-assessment; generate **draft** Bildungsbericht, review and sign off through an explicit workflow.

**When task finishes:** Jira completion → evidence linked → learner reflection / explanation → mentor competency evaluation → next learning task suggested. No "Jira Done = competency achieved".

## 6. Authorization and data protection
Roles: Apprentice (self), Mentor (assigned learners), BB Lead (authorized cohort), Admin (configuration with restricted access to educational contents where possible), read-only auditor (scoped).
- Private journal drafts only visible to learner; explicitly shared entries visible to assigned mentors.
- Certificates, school grades, formal reports and health/leave details are sensitive and access-limited.
- Learners can inspect what is recorded about them and correct factual inaccuracies through controlled workflows.
- Focus and gamification are voluntary and not used for surveillance or employment assessment.
- External Jira/GitLab/BCS integrations have scoped tokens, audit trails and retry/idempotency.
- Do not place personal apprentice, grades, evaluation data, private source code or secrets in the public GitHub repository or AI prompts.
- Logs never contain full journal text, document contents or secrets; backups and exports secured.

## 7. MVP user stories and acceptance tests
**BB-01:** As mentor I can see all assigned learners with current tasks, milestones and missing verified learning goals; every metric links to underlying items.
**BB-02:** As mentor I can define a task with title, purpose, acceptance criteria, assignee and ≥1 learning-goal mapping; save draft and assign.
**BB-03:** As mentor I can inspect a learner's task/goal/evidence history and set the next targeted development objective.
**BB-04:** As apprentice I see assignments and can record progress, blockers and a daily reflection; only shared content is visible to mentor.
**BB-05:** As mentor I can verify evidence and record competency level with reason; completing a task does not auto-verify a goal.
**BB-06:** As mentor I can find unmapped mandatory goals and use them when creating a task.
**BB-07:** As mentor I can create or link a Jira EDU task, preserving Jira as source of truth (v0.4).
**BB-08:** As mentor I can review GitLab MR/pipeline references as potential evidence (v0.4).
**BB-09:** As authorized BB I can create/export a semester Bildungsbericht with evidence references (v0.6).
**BB-10:** As mentor I can see knowledge-check outcomes, supports used, skill trends and QV preparation without equating activities with qualification (v0.5–0.8).

## 8. Engineering interfaces (proposed, subject to API design review)
- `GET /api/v1/bb/dashboard?semester=&mentor=`: summary, attention queue, KPIs, recent activity.
- `GET /api/v1/bb/apprentices/{id}/development`: multi-perspective progress/evidence.
- `GET /api/v1/goals?source=&status=&semester=`: goal finder and missing coverage.
- `POST /api/v1/learning-tasks`: draft task; `PATCH /api/v1/learning-tasks/{id}` edit; `POST /api/v1/learning-tasks/{id}/assignments` assign.
- `POST /api/v1/learning-tasks/{id}/publish-to-jira`: explicit publishing (v0.4) with idempotency and authorization.
- `POST /api/v1/competency-evaluations`: mentor evaluation and evidence provenance.
- `GET /api/v1/bb/attention`: support/review queue.

Proposed entities: `LearningTask`, `TaskTemplate`, `TaskAssignment`, `TaskGoalLink`, `TaskHint`, `TaskEvidence`, `TaskExternalLink`, `TaskReview`, `CompetencyEvaluation`, `DashboardAttentionItem`. Distinguish tasks from Jira external issues; avoid duplicate truth.

## 9. Performance and accessibility
- Most-used mentor actions reachable in ≤2–3 interactions (create task, open profile, review evidence).
- Fast skeleton loading and clear sync timestamp; graceful handling of unavailable integrations.
- Keyboard-usable task wizard, sensible labels, accessible chart/table alternatives, responsive tablet support.
- No public apprentice ranking; no unsupported "risk scores" derived from activity metrics.
- Support German first; prepare localization of labels and date formats.

## 10. Open decisions (not blocking early design)
1. Authoritative task system: Jira EDU project key and available issue types/fields.
2. Whether internal tasks remain after Jira integration, and allowed two-way sync directions.
3. Source and policy for official time tracking (e.g. Projectron/BCS).
4. School/üK record import and approvals.
5. Mentor group permissions and certificate retention schedule.
6. Who can publish a Jira ticket and who approves formal semester reports.

See also [PRODUCT_VISION.md](PRODUCT_VISION.md), [RELEASES.md](RELEASES.md), and [UX_WIREFRAMES.md](../design/UX_WIREFRAMES.md).
