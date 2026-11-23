# GitHub PR and branch inventory · 2026-11-23

Snapshot of `AdBuch/bb-nator`, verified against GitHub on this date. **No branch deleted, PR merged or application code modified during this documentation audit.** Ahead/behind values refer to `main` at the snapshot and change with new merges.

## Pull requests

- **Merged:** #1, #2, #3, #4, #6, #7, #8, #9.
- **Closed without merge:** #5, temporary CI lockfile-generation PR. Its branch is still present.
- **OPEN:** [#10](https://github.com/AdBuch/bb-nator/pull/10): LDAP login, local accounts and roles, unprivileged runtime DB role and effective RLS, demo-start guard. CI claims 41 backend tests and smoke, **but independent review is still required**.
- PR #10 deliberately excludes: Angular sign-in, user/role admin UI, login throttling, access logs. Its `APP_DB_PASSWORD` configuration requires checking migration paths for existing installations.

## Branches (present on remote)

| Branch | Against main | Relationship / suggested housekeeping |
| --- | --- | --- |
| `main` | baseline | v0.1 foundation and merged architecture docs |
| `release/0.2-learning-path` | +23 / -0 | active release branch, F01–F28 planning and demo foundation |
| `feat/0.2-identity-ldap-login` | +27 / -0 | **open PR #10**; review rather than deleting |
| `feat/0.2-identity-tenancy-foundation` | +20 / -0 | PR #8 merged into release; retain or remove after branch-policy check |
| `feat/0.2a-demo-and-updater` | +7 / -0 | PR #6 merged into release; demo available on release branch |
| `docs/0.2-roadmap-features` | +22 / -0 | PR #9 merged into release; F01–F28 source |
| `docs/merge-cockpit-0.2` | +8 / -0 | PR #7 merged into release |
| `release/0.1-foundation` | +0 / -9 | v0.1 already in main; historical branch |
| `feat/0.1-review-hardening` | +0 / -10 | PR #4 merged; historical branch |
| `docs/direct-integrations-reuse-plan` | +0 / -1 | PR #2 merged; historical branch |
| `docs/bb-cockpit-task-management-spec` | +6 / -75 | PR #1 merged; historical/diverged branch, **do not merge stale head** |
| `ci/0.1-lockfile-bootstrap` | +1 / -11 | PR #5 closed, temporary; candidate for removal after human review |

Numbers mean *commits ahead/behind*, not unique product features or missing changes. Ahead counts on merged feature branches can reflect the release branch hierarchy, so **do not blindly merge old branches into main**. Feature PRs target the active release branch; release PR to main is separate.

## Important reconciliation notes

1. PR #10 updates `docs/ROADMAP.md`, `docs/features/F01-...`, `F02-...` and `docs/features/README.md`. Any docs PR modifying those same files must be rebased/reconciled after #10; do not overwrite its more recent identity notes.
2. PR #10 also contains tracked `scripts/__pycache__/*.pyc` files according to its changed-files list. Before merge, review and remove these generated binary files; add/verify `__pycache__/` and `*.pyc` in `.gitignore`.
3. The LDAP PR states it uses an embedded test directory rather than the container anticipated by F01. Update acceptance criteria or document the exception, without falsely claiming a Testcontainer LDAP test.
4. Verify the `demo` profile isolation and `APP_DB_PASSWORD` bootstrap upgrade, with negative tenant/authorization tests and no personal test data.
5. The v0.2a updater works only on localhost, clean `main`, and must not be represented as secure remote admin deployment.

## Branch cleanup policy (proposal only)
Once PR #10 is independently reviewed and merged, delete merged feature branches if desired, retain versioned release branches and use GitHub branch protection. This audit deliberately performs **no deletion**.

## Nachtrag am 2026-11-23: PR #10 inzwischen gemergt

Bei der zweiten Live-Prüfung wurde [PR #10](https://github.com/AdBuch/bb-nator/pull/10) als **closed/merged** in `release/0.2-learning-path` bestätigt. Die oben notierte OPEN-Angabe bezeichnet den früheren Snapshot während dieser Prüfung. Damit sind alle bis dahin bekannten PRs #1–#10 geschlossen (PR #5 ohne Merge); ein neuer Dokumentations-PR entsteht separat.

Die Identitäts-API und die RLS-Laufzeitrolle sind also im **Release-Branch**, nicht automatisch auf `main` oder schon als fertige Produktionsauthentifizierung. Angular-Login-UI, Rollenadministration, Login-Rate-Limit, Zugriffsklassen und Access-Logs bleiben offen. Vor produktivem Einsatz die durch PR #10 aufgenommenen `scripts/__pycache__/*.pyc` prüfen und entfernen. Revisionsstand/Commitzählungen in obiger Branch-Tabelle sind Momentaufnahmen und können nach den Merges abweichen.
