# Technology and Architecture Guidance for AI Agents

Status: **target architecture**; not a claim that code exists. Read `AGENTS.md` and `STEPS.md` first.

## Principles
- **Onion architecture** on the backend and feature-oriented, layered architecture on the frontend.
- Modular monolith first. Strong boundaries without excessive abstractions.
- Domain and use cases must not depend on Spring, JPA, HTTP, Angular, Docker or AI providers.
- Favor boring, readable, testable solutions; no speculative microservices.
- Never expose personal apprentice data or proprietary company content in this public repository.

## Backend: Java 21 / Spring Boot / Maven
Use Spring Boot, Spring Web, Spring Validation, Spring Security, Spring Data JPA, PostgreSQL, Flyway, Actuator and springdoc OpenAPI where appropriate. Use JUnit 5, Mockito, AssertJ and Testcontainers for tests. Keep the compatible versions pinned in ADR-0001 and verified by CI; version changes need a review and tested migration plan.

Suggested package layout **within each business module**:
```text
backend/src/main/java/ch/bbnator/learningpath/
  domain/             # entities, value objects, rules, domain services, ports
  application/        # use cases, commands/queries, orchestration, port interfaces
  infrastructure/     # Spring Data repositories, JPA mappings, external adapters
  interfaces/rest/    # REST controllers, request/response DTOs, mappers
```
The planned module list is maintained in [MODULE_TEMPLATE.md](MODULE_TEMPLATE.md).

### Dependency rule
`interfaces/rest → application → domain`; infrastructure implements inward-facing ports. The **domain imports no Spring/JPA classes**. Use explicit mapping between domain models, persistence entities and REST DTOs. Spring Data `JpaRepository` interfaces live in infrastructure, not domain.

### Backend rules
- Prefer constructor injection and Java records for immutable request/response DTOs.
- Put business rules in domain/application, not controllers or JPA entities.
- Validate requests and authorize at API/use-case boundaries; never rely on frontend permissions.
- Use `@Transactional` at application service boundaries where needed.
- Use Flyway migrations; no Hibernate auto-DDL in production.
- Keep persistence relationships deliberate; avoid eager fetching and unbounded queries.
- Return consistent problem responses and avoid leaking internal exceptions.
- Make integrations adapters with explicit timeouts and retry policies.
- Use ports for Jira/Git integrations and optional AI hint providers.
- Class C (voluntary) and class D (sensitive) apprentice data must never be sent to external AI providers (PRIVACY.md). Company secrets must not be sent to external AI without explicit company authorization. AI features are disabled per tenant by default.

## Frontend: Angular / TypeScript
Use a maintained Angular version, strict TypeScript, standalone components, Angular Router, reactive forms, signals where helpful, and accessible semantic HTML. Prefer built-in Angular capabilities before adding state libraries.

Feature-oriented layout:
```text
frontend/src/app/
  core/                   # auth, HTTP interceptors, app configuration
  shared/                 # reusable UI primitives, formatting utilities
  features/
    learning-path/
      pages/
      components/
      application/        # facades, orchestration, state
      domain/             # types, pure validation and rules
      infrastructure/     # typed HTTP clients, DTO adapters
    journal/
    focus/
    mentoring/
```
Dependencies: pages/components → application/domain; infrastructure implements data-access contracts. **Do not copy Spring's class-heavy onion pattern into Angular**; use pragmatic feature boundaries. Keep components focused on presentation and interactions, business rules in pure functions/services. Prefer generated or checked OpenAPI types and avoid `any`.

## API and data
- Versioned REST endpoints under `/api/v1`; OpenAPI is the contract.
- PostgreSQL with Flyway and stable IDs; UTC instants for events, local dates for learning days.
- Explicit ownership and authorization checks on every personal record.
- Data minimization, configurable retention and export for learner records.
- Distinguish **official BiVo goals**, **evidence**, **mentor evaluation**, **XP**, and **focus time**. Never infer qualification solely from activity or quiz scores.

## Docker and delivery
- Docker Compose for local development and initial deployment: backend, frontend, PostgreSQL.
- Multi-stage builds; non-root application containers, health checks, persistent database volume.
- Environment variables for configuration; secrets never committed.
- CI: backend tests, frontend tests/build, dependency/security checks, Docker smoke test when feasible.
- Feature branch → PR → independent review → tests → human-approved merge.

## Testing strategy
- Domain: fast unit tests with no Spring context.
- Application: use-case tests with fake ports.
- Infrastructure: JPA integration tests against PostgreSQL Testcontainers.
- API: controller and authorization tests.
- Frontend: component/service tests; critical user flows as E2E later.
- Verify error, permission and privacy cases, not only happy paths.

## Non-goals until justified
Microservices, Kubernetes, event sourcing, complex CQRS, generalized workflow engines, large frontend state frameworks, and autonomous agent merges.

## Agent decision protocol
If a proposed implementation violates the dependency rule or adds a major dependency, write an ADR and request human approval **before implementation**.
