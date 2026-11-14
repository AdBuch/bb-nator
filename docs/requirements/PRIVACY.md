# BB-NATOR: Data protection mechanism

**Status:** specification, accepted 2026-11-14. No personal data is stored in v0.1.
**Decision record:** [ADR-0002](../architecture/ADR-0002-identity-tenancy-data-protection.md)

This is not legal advice. Each operating company remains responsible for its own legal assessment.

## Principle
Every persisted record containing personal data or tenant-owned training information carries a protection class. Visibility, retention and access logging follow from the class, so a developer cannot add personal data without deciding how it is protected. Pure technical migration metadata and the global read-only BiVo source catalogue are not personal records.

## 1. Protection classes

| Class | Examples | Basis | Default visibility |
| --- | --- | --- | --- |
| **A Technical** | login name, sync status | operation | admins |
| **B Training** | tasks, goal mappings, evidence, competency evaluations | apprenticeship | apprentice and assigned mentors |
| **C Voluntary** | journal drafts, mood check, focus sessions, XP | consent | apprentice only |
| **D Sensitive** | school grades, Bildungsberichte, certificates, absences | apprenticeship, strictly limited | apprentice and explicitly authorised roles |

Each persisted entity containing personal data or tenant-owned training information declares its class in code. An architecture test must fail the build when such an entity has none. Pure technical metadata and globally published, read-only educational source catalogue entities are exempt.

## 2. Ownership and visibility
- The **apprentice owns** records about them. Exceptions are mentor-authored evaluations and formal reports: the mentor is author, the apprentice remains the data subject with full read access.
- Every personal record has a visibility level: `PRIVATE`, `SHARED_WITH_MENTORS` or `FORMAL`.
- Class C starts as `PRIVATE`. Sharing is an explicit action by the apprentice and can be withdrawn for future access.
- Checks happen in the use case on the server. The frontend never decides access.

## 3. Consent register
- One entry per apprentice and purpose: focus tracking, mood check, gamification, AI processing.
- Entries are versioned with the text that was shown, timestamped and revocable at any time.
- Without consent the feature stays off. Mentors do not see who declined.
- Revocation stops processing and deletes the data collected under that purpose.

## 4. Retention and deletion
- Retention period per class and tenant; a scheduled job deletes expired records.
- At the end of the apprenticeship the apprentice receives an export, then records are deleted after the configured period.
- Formal reports keep the period the company defines for training records.
- Backups follow the same periods through rotation.

## 5. Access and correction
- Self-service view "what is stored about me" with export.
- Corrections run through a request workflow; factual records are amended with history, never silently overwritten.

## 6. Access log
- Every read and write of class D records and of shared class C records is logged: who, when, which record, which action. Never the content.
- Apprentices can see who opened their records.
- Application logs contain no journal text, document content or credentials.

## 7. Boundaries
- Tenant isolation through PostgreSQL row-level security.
- Class D documents are encrypted at rest.
- Jira and GitLab contribute references and metadata only.
- Class C and D data is never sent to external AI providers. AI features are off per tenant by default.

## Release mapping
| Release | Delivered |
| --- | --- |
| v0.2 | protection classes, tenant isolation, ownership and visibility model, access log |
| v0.3 | consent register together with journal and focus features |
| v0.6 | encrypted document storage |
| v1.0 | export and deletion verified end to end |

## Acceptance tests (each release that touches personal data)
1. A mentor cannot read records of an apprentice who is not assigned to them.
2. A private journal entry is invisible to every role except its owner.
3. A user of tenant A cannot read or modify any row of tenant B, including through direct identifiers.
4. Revoking a consent removes the related data and disables the feature.
5. Reading a class D record creates an access-log entry visible to the apprentice.
