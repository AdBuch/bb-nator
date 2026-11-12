# BB-NATOR: Product Vision and Functional Target

## Product mission
BB-NATOR is an apprenticeship development cockpit for Swiss Informatiker/in EFZ, Applikationsentwicklung. It connects **what must be learned**, **what the company expects**, **what mentors want to foster**, **what apprentices want to explore**, and **what has actually been learned**. It supports apprentices and Berufsbildner from onboarding through EFZ qualification, without replacing Jira, GitLab, school systems or official assessment processes.

## Seven planning perspectives (not seven competing official curricula)
1. **Official Bildungsplan / BiVo 2021:** mandatory workplace Handlungskompetenzen and Leistungsziele, versioned, traceable, with evidence and gaps.
2. **Betrieb / company fit:** engineering practices, DLP business domain, team readiness, security, production standards, Java/Spring/Angular/DevOps.
3. **Berufsbildner vision:** independence, critical thinking, code comprehension, quality, responsible AI, communication, professional growth.
4. **Lernendenwünsche:** interests, individual projects, specializations and personally agreed goals; apprentices can propose and revise them.
5. **Betrieblicher Lehrpfad / tasks:** eight-semester roadmap, learning assignments, Jira EDU tickets, milestones, dependencies and adaptive hints.
6. **Überbetriebliche Kurse (üK):** modules, schedule, competencies, attendance/results where permitted; track alignment and support gaps.
7. **Berufsfachschule:** school modules, semester schedule, subjects, results and support needs where permitted.

The last two are **external learning locations**, not subordinate to the company. The portal shows their alignment and progress without claiming to replace their official records. Source documents are versioned; imported goals are verified against authoritative originals. The seven perspectives feed a **single individual development plan**, not seven isolated checklists.

## Berufsbildner Cockpit: action-first workspace

The mentor needs an operational dashboard to **track progress, create/assign new learning tasks, follow up with apprentices and evaluate evidence**, not just browse charts. Key screens are: mentor overview, individual learner development view, guided task creation, and pending reviews. Tasks are matched to goals across the seven perspectives; the mentor can identify uncovered mandatory BiVo goals and assign focused learning work.

**Source-of-truth decision:** manual learning tasks and their goal mappings belong to BB-NATOR. Jira remains authoritative for linked Jira issues, GitLab for MRs/pipelines. Starting at v0.4, mentors may explicitly create/link Jira EDU issues from the task wizard after preview; finishing an issue does **not** automatically verify competency.

**Details and screen wireframes:** [BB_COCKPIT.md](BB_COCKPIT.md) and [UX_WIREFRAMES.md](../design/UX_WIREFRAMES.md).

## Core domain model
- `Apprentice`, `Mentor`, `Cohort`, `Semester`
- `GoalSource` (BiVo/company/mentor/apprentice/learning-path/uek/school), `Goal`, `GoalVersion`, `GoalRelation`
- `IndividualLearningPlan`, `Milestone`, `LearningTask`, `GoalAssignment`
- `Evidence` (manual/Jira/GitLab/quiz/report), `CompetencyEvaluation`, `Assessment`
- `LearningSession`, `FocusSession`, `DailyReflection`, `LearningJournal`
- `MentorFeedback`, `CheckIn`, `SemesterReport`, `Document`
- `RewardEvent`, `Badge`, `LearningGameAttempt`, `Hint`, `HintUsage`
- `ExternalReference`, `SyncJob`, `AuditEvent`

## User experiences
### Apprentice dashboard
- Today's learning goals and assigned Jira tickets, focus session, progress, next milestone.
- **What did I achieve today?** quick reflection, blockers, next steps, optional mood check.
- Personal learning path, skill tree, competency evidence, learning games, knowledge checks.
- Learning journal, mentor feedback, school/üK overview, documents and certificates.
- Progress over time, without public ranking or surveillance.

### Berufsbildner dashboard
- **+ Neue Lernaufgabe** opens a six-step wizard: purpose, goal mapping, acceptance criteria, hints, assignment, preview/publish.
- Task board and pending review queue for follow-up; track goals as planned/practiced/evidenced/mentor-verified.
- Overview of all apprentices: milestones, coverage of official goals, pending reviews, risks and upcoming check-ins.
- Drill-down into learning path, Jira tickets, GitLab merge requests, evidence, quiz results and reflections.
- Semester reports, documented feedback and goal agreements; school/üK alignment.
- **Development dashboard:** trends in demonstrated independence, competencies, learning support and readiness; clearly distinguish actual evidence from inferred signals.
- Coverage analysis: which official workplace goals have not yet been taught or verified?

### Integration model
- **Jira:** source of truth for tasks; synchronize references, status, estimates and educational mappings; avoid duplicating ticket editing.
- **GitLab:** source of truth for code/MRs/reviews/pipelines; evidence links and metadata, not source-code ingestion by default.
- **Zeiterfassung:** optionally connect company time tool (e.g. Projectron/BCS if approved) for project/time context. Separate *official work time*, *self-declared learning time* and *focus timer*. No automatic time booking without explicit approval.
- **School/üK:** initially manual entry/import, later approved interfaces if available; distinguish reported from verified results.
- **Documents:** learning documentation, semester reports, certificates, school reports and apprenticeship references where permitted; secure uploads, access control, retention and export.

## Pedagogy and gamification
- Scaffolding: staged hints (question → method → technical pointer), tailored to skill rather than stereotyped generation-based attention spans.
- Individual focus progression from very short intervals toward ~30 minutes, opt-in and flexible; timer minutes are not productivity or competency grades.
- XP, achievements and mini-games reward meaningful learning, reflection and mastery, not hours or competition.
- Knowledge checks (multiple choice and practical prompts) complement, never replace, observed performance and mentor evaluation.
- AI may draft suggestions, never autonomously assign official competency grades or submit formal reports.

## Success criteria
1. Every applicable official workplace BiVo goal has a traceable teaching plan and verification pathway.
2. Each apprentice has a maintained individual plan combining all seven perspectives.
3. Learners and mentors can see evidence-based progress and identify gaps early.
4. Learning journal and formal reports can be prepared without re-entering the same data.
5. Integrations reduce administrative effort without creating a second Jira/GitLab/time system.
6. Privacy, consent, role-based access, and human review are built in from the beginning.

## Guardrails
This is a **public repository**: no real names, school grades, personal reflections, employment records, secrets or proprietary company information. Use synthetic examples. Educational and employment records are sensitive; apply least privilege, purpose limitation, retention, and human oversight. Never present XP/focus minutes as a qualification grade.
