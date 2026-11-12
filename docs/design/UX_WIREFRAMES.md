# BB-NATOR: UX mockup concepts (conceptual wireframes)

The three image mockups in the product discussion are **illustrative**; their names, scores, dates and percentage gains are not live data. Implement accessible HTML, not a static screenshot.

## Screen A. Berufsbildner Cockpit (desktop)

```text
┌──────── Sidebar ────────┬──────────────────── BB-NATOR / Berufsbildner ───────────────────────────┐
│ Dashboard              │ Search learners / tasks / goals       Semester: current       + Task    │
│ Lernende               ├─────────────┬─────────────────┬──────────────────┬────────────────────┤
│ Aufgaben               │ 4 Lernende  │ 7 offene Reviews│ 5 Ziele ungeplant│ 2 nächste Termine  │
│ Bildungsplan           ├──────────────────────────────────┬─────────────────────────────────────┤
│ Entwicklung            │ Learner progress / attention      │ NEXT ACTIONS                        │
│ Schule / üK            │ [name] [semester] [current task] │ □ Review MR 123                     │
│ Berichte & Zeugnisse   │ [goals practiced/verified]       │ □ assign next REST exercise         │
│ Knowledge Checks       │ [support needed] [assign]        │ □ Semester check-in                 │
│ Gamification           ├──────────────────────────────────┼─────────────────────────────────────┤
│ Einstellungen          │ Coverage across seven visions    │ Task board / upcoming milestones    │
│                        ├──────────────────────────────────┴─────────────────────────────────────┤
│                        │ Activity & evidence, upcoming school / üK, integration sync health     │
└────────────────────────┴────────────────────────────────────────────────────────────────────────┘
```
Visual emphasis: **one-click "Neue Lernaufgabe" and attention queue**, then learner development and BiVo coverage.

## Screen B. Individual development cockpit

```text
┌────────────────── Apprentice profile / Semester 3 ───────────────────────────────────┐
│ [Learning path: S1 ✓ S2 ✓ S3 ● S4 ○ S5 ○ S6 ○ S7 ○ S8 ○]   + Assign task           │
├────────────────────────────┬─────────────────────────────────────────────────────────┤
│ Competency radar/trends    │ 7 PERSPECTIVES: BiVo | Company | Mentor | Interests ...  │
│ [actual vs target]         │ Official goals: planned / practiced / evidenced / verified│
├────────────────────────────┼─────────────────────────────────────────────────────────┤
│ Current assignments        │ Evidence: Jira issues, GitLab MRs, knowledge checks      │
│ Next milestones            │ Learning journal (shared), feedback, reports             │
└────────────────────────────┴─────────────────────────────────────────────────────────┘
```
Every progress measure must link to assessable evidence, and absent data must appear as "unknown/not provided" rather than zero competence.

## Screen C. Guided assignment wizard (BB)

```text
┌───────────────────────────────────── Create learning task ─────────────────────────────────────┐
│ 1 Purpose → 2 Goals → 3 Criteria → 4 Hints → 5 Assign → 6 Preview / Publish                   │
├─────────────────────────────────────────────────┬─────────────────────────────────────────────┤
│ Title, Why / expected outcome                   │ learner context & current goals             │
│ Type: internal learning task / Jira EDU         │ [current skills and prerequisites]           │
│ Description, acceptance criteria, test plan     │ [missing official goals this supports]       │
│ Goals: BiVo + company + mentor + learner wishes │ Expected development opportunity             │
│ Difficulty, timebox, milestones                 │ (NOT auto-updated competency %)              │
│ Hints: thinking / approach / technical pointer │ Capacity / calendar conflicts               │
│ Apprentices, mentor, date                       │ Publishing destinations and status           │
├─────────────────────────────────────────────────┴─────────────────────────────────────────────┤
│ [Save draft]                                   [Assign in BB-NATOR] [Create/link Jira issue]  │
└───────────────────────────────────────────────────────────────────────────────────────────────┘
```
`Create/link Jira issue` is disabled with clear reason until the v0.4 integration is authorized/configured.

## Visual direction
- Keep existing BB-NATOR look: dark navy sidebar, airy white workspace, rounded cards and teal/blue accent.
- Mentor cockpit should be **action-first**, not a gallery of charts.
- Apprentice cockpit should be **motivation-and-growth-first**, with daily reflection and skill tree.
- Use semantic statuses, textual labels alongside colors, keyboard access and responsive layouts.
- Avoid public leaderboards, fake percentages, "time = productivity" design, or auto-inferred school grades.

## Prioritized navigation
**Mentor:** Dashboard / Lernende / Aufgaben & Tickets / Ausbildungsplan / Entwicklungsanalyse / Berichte & Zeugnisse / Schule & üK / Integrationen / Einstellungen.
**Apprentice:** Heute / Mein Lernpfad / Aufgaben / Meine Entwicklung / Lernjournal / Wissen & Spiele / Fokus / Schule & üK / Dokumente.

## Interactions worth usability testing
1. Mentor assigns a task targeting an uncovered BiVo goal in under two minutes.
2. Mentor detects an overdue review in less than 30 seconds.
3. Learner finds their next learning task without opening Jira.
4. Mentor discovers why a goal is still unverified even when Jira says Done.
5. Learner can distinguish private from shared journal entries.
