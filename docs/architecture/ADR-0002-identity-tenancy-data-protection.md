# ADR-0002: Identity, tenancy and data protection baseline

**Status:** accepted by the product owner on 2026-11-14; implementation starts in v0.2.
**Supersedes:** the "OIDC is evaluated" note in [ADR-0001](ADR-0001-foundation.md) for the first deployment.

## Context
v0.2 introduces the first personal records (apprentices, mentors, assignments). Before any such table exists, four questions had to be answered: how users sign in, who owns the roles, whether the system serves one company or several, and where official learning goals come from.

## Decisions

### 1. Authentication through LDAP, roles kept locally
- The company directory verifies credentials (LDAP bind over LDAPS). BB-NATOR never stores passwords.
- Roles (`APPRENTICE`, `MENTOR`, `BB_LEAD`, `ADMIN`, `AUDITOR`) and mentor-to-apprentice assignments live in the BB-NATOR database, not in LDAP groups.
- A user must exist locally and be active to get access; a successful LDAP bind alone grants nothing.
- Authentication sits behind an application port (`IdentityProvider`) configured per tenant. LDAP is the first adapter; OIDC can be added later without touching use cases.
- Browser sessions use a server-side session with an `HttpOnly`, `SameSite=Lax` cookie and CSRF protection enabled. The stateless, CSRF-disabled configuration of v0.1 is only valid while no login exists.

### 2. Multi-tenant from the first domain migration
- Every table holding business or personal data carries a non-null `tenant_id`.
- Isolation is enforced in PostgreSQL with row-level security; the application sets the tenant for each transaction. Application-level filtering alone is not accepted.
- The local installation runs with exactly one tenant. Tenant onboarding, billing and administration UIs are out of scope until a second tenant is real.
- Official BiVo goals are a global, read-only catalogue. Goals from company, mentor, apprentice and learning path belong to a tenant.

### 3. First deployment is on-premises
- The system runs inside the training company as part of the apprenticeship. No personal data leaves the company network.
- A hosted offering needs its own review (processing agreements, hosting location, per-tenant identity providers) before the first external tenant.

### 4. Official goals are imported, not committed
- BiVo 2021 goals are imported at installation time from the published source file. Each import records source, version, retrieval date and checksum.
- The public repository contains the importer and synthetic fixtures only, until redistribution rights of the source text are confirmed.

### 5. Data protection is a mechanism, not a guideline
See [PRIVACY.md](../requirements/PRIVACY.md). Every entity declares a protection class; visibility, retention and access logging derive from it. The apprentice is the owner of records about them unless a class states otherwise.

## Consequences
- v0.2 scope grows by tenant, user, role and audit foundations before the first learning-path table.
- Integration tests must cover tenant isolation and role boundaries, including negative cases.
- An LDAP test container is required in CI.
- Passwords, LDAP bind credentials and tenant configuration are supplied through environment or secret files, never through the repository.

## Open points
- Retention periods per protection class (decided per tenant; defaults needed).
- Whether guardians of minors receive a separate privacy notice.
- LDAP attribute used as stable user key (`uid`, `sAMAccountName` or `entryUUID`).
