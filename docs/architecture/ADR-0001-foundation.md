# ADR-0001: Modular monolith foundation

**Status:** accepted for v0.1 scaffold, review during PR.
**Date:** 2026-11-12

## Decision
- Angular 22 standalone components, strict TS, no heavy state library.
- Spring Boot 4.1.2, Java 21 and Maven.
- PostgreSQL 17, Flyway migrations and JPA; Hibernate DDL disabled (`validate`).
- Docker Compose deployment with Nginx frontend reverse proxy to API.
- Backend code grouped by feature using Onion Architecture:
  - `domain/`: framework-free business rules and value objects.
  - `application/`: use cases and port interfaces, no Spring APIs.
  - `infrastructure/`: Spring Data JPA implementations and external adapters.
  - `interfaces/rest/`: controllers, DTOs and mapping.
- Frontend grouped by feature: `domain/`, `application/`, `infrastructure/`, `pages/` and `components/`.
- Fail-closed API access; only technical status and health public until OIDC is evaluated.
- Real Jira and GitLab access from future BB-NATOR adapters, not through Tinyops; BCS integration requires its own reviewed boundary. See pending architecture PR if unmerged.

## Rationale
Start simple and testable while preserving boundaries for educational goal and learner data. Do not add microservices, Kubernetes, autonomous merges or attendance monitoring to v0.1.

## Risks and follow-ups
- Compatible versions must be proven in CI.
- Frontend npm lockfile is a priority follow-up.
- SSO/RBAC and data retention must be reviewed before implementing person data.
- Backend/Frontend smoke tests prove bootstrap, not product readiness.
