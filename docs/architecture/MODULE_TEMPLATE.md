# Feature module design template (backend / frontend)

## Backend onion per domain area
```text
backend/src/main/java/ch/bbnator/<feature>/
  domain/                  # pure Java aggregates, entities, value objects and invariants
  application/             # use cases, input/output ports, commands, query services
  infrastructure/          # Spring Data JPA adapters, external clients, config
  interfaces/rest/         # controllers and request/response DTO mapping
```

Inside-out dependency rules:
1. `domain` imports only JDK / stable domain libraries, no Spring/JPA.
2. `application` depends on `domain` and declares ports.
3. `infrastructure` implements ports, contains JPA entities and Spring Data repositories.
4. `interfaces/rest` maps HTTP input/output to application DTOs.
5. Compose via Spring configuration; avoid field injection and hidden cross-module DB access.

Each feature starts with a one-page scope/acceptance criteria, domain model, tests and migrations. Do not create interfaces with only one unused implementation for every tiny method. Keep boundaries meaningful.

## Angular feature shape
```text
frontend/src/app/features/<feature>/
  domain/                  # plain TS types and pure functions
  application/             # facade/state + ports, business UI orchestration
  infrastructure/          # typed HTTP adapters/mappers
  components/              # small presentational components
  pages/                   # route screens
```
Keep `core/` for auth/config/shared HTTP behavior and `shared/` for reusable UI, not a dumping ground. Do not create a second enterprise-Java framework inside Angular.

## Planned modules
`apprentices`, `learning-path`, `goals`, `learning-tasks`, `evidence`, `mentoring`, `journal`, `knowledge-checks`, `gamification`, `integrations`.

## Safety
Any learner personal record requires an approved SSO/RBAC and retention design before implementation. Use fictional examples only in this public repository.
