# TaskFlow — Task Management REST API
![CI](https://github.com/mdsamialsohan/taskflow/actions/workflows/ci.yml/badge.svg)

A backend REST API for managing tasks and projects, built with
Java 21, Spring Boot 4, PostgreSQL, and Docker.

Users create projects, add tasks to them, assign tasks to team
members, and move tasks through statuses with enforced business
rules.

## Tech Stack

- Java 21
- Spring Boot 4
- PostgreSQL 16
- Hibernate / Spring Data JPA
- Flyway (database migrations)
- Docker and Docker Compose
- JUnit 5 and Testcontainers
- GitHub Actions (CI)

## Getting Started

### Prerequisites

- Docker and Docker Compose

That is it. You do not need Java installed — the app builds
inside Docker.

### Run the project

```bash
git clone https://github.com/mdsamialsohan/taskflow
cd taskflow
docker compose up --build
```

The API will be available at `http://localhost:8080`.

### Run locally (for development)

If you have Java 21 installed and want faster reload:

```bash
# Start the database
docker compose up db -d

# Run the app
./mvnw spring-boot:run
```

### Run tests

```bash
./mvnw test
```

Tests use Testcontainers, so Docker must be running. A real
PostgreSQL container is started automatically — no H2 or
in-memory databases.

## Project Structure

```
src/main/java/com/samialsohan/taskflow/
├── controller/   REST endpoints
├── service/      Business logic
├── repository/   Database access (Spring Data JPA)
├── entity/       JPA entities mapped to tables
├── dto/          Request and response objects
└── exception/    Custom exceptions and global handler

src/main/resources/
├── application.properties
└── db/migration/  Flyway SQL migrations
```

## API Endpoints

### Users

| Method | Endpoint           | Description           |
|--------|--------------------|-----------------------|
| POST   | `/api/users`       | Create a user         |
| GET    | `/api/users`       | List users (paginated)|
| GET    | `/api/users/{id}`  | Get user by ID        |
| PUT    | `/api/users/{id}`  | Update a user         |
| DELETE | `/api/users/{id}`  | Delete a user         |

### Projects

| Method | Endpoint              | Description        |
|--------|-----------------------|--------------------|
| POST   | `/api/projects`       | Create a project   |
| GET    | `/api/projects`       | List projects      |
| GET    | `/api/projects/{id}`  | Get project by ID  |
| PUT    | `/api/projects/{id}`  | Update a project   |
| DELETE | `/api/projects/{id}`  | Delete a project   |

### Tasks

| Method | Endpoint                    | Description                |
|--------|-----------------------------|----------------------------|
| POST   | `/api/tasks`                | Create a task              |
| GET    | `/api/tasks`                | Search tasks with filters  |
| GET    | `/api/tasks/{id}`           | Get task by ID             |
| PUT    | `/api/tasks/{id}`           | Update a task              |
| PATCH  | `/api/tasks/{id}/status`    | Change task status         |
| DELETE | `/api/tasks/{id}`           | Delete a task              |

## API Examples

### Create a user

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice", "email": "alice@example.com"}'
```

### Create a project

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{"name": "Website Redesign", "description": "Revamp the site"}'
```

### Create a task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design homepage",
    "projectId": 1,
    "assigneeId": 1,
    "priority": "HIGH"
  }'
```

### Move a task through statuses

```bash
# TODO -> IN_PROGRESS
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "IN_PROGRESS"}'

# IN_PROGRESS -> DONE (requires an assignee)
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "DONE"}'
```

### Search tasks with filters

All filters are optional and can be combined:

```bash
curl "http://localhost:8080/api/tasks?priority=URGENT"
curl "http://localhost:8080/api/tasks?status=TODO&keyword=login"
curl "http://localhost:8080/api/tasks?assigneeId=1&priority=HIGH"
```

### Validation errors return clear messages

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "not-valid"}'
```

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": [
    {"field": "name", "message": "Name is required"},
    {"field": "email", "message": "Email must be valid"}
  ]
}
```

## Design Decisions

### Why Flyway instead of Hibernate auto-DDL?

Hibernate's `ddl-auto=update` lets Hibernate modify the database
schema automatically. This is dangerous in production — schema
changes should be versioned, reviewed, and applied in order just
like code. Flyway migrations are SQL files checked into Git.
Hibernate is set to `validate` so it catches any mismatch between
entities and the schema.

### Why DTOs instead of exposing entities?

JPA entities have relationships that can trigger lazy-loading
errors when serialized to JSON. They also contain internal fields
that API consumers should not see. DTOs define the API contract
separately from the database model. If the schema changes, the
API does not break.

### Why FetchType.LAZY on relationships?

ManyToOne defaults to EAGER, which loads related entities
automatically. Loading a list of 100 tasks would trigger 100
additional queries to load each project and assignee. LAZY loading
means related entities are only fetched when explicitly accessed.

### Why business rules in the service layer?

The database enforces data integrity (foreign keys, unique
constraints, not-null). But rules like "cannot mark a task as DONE
without an assignee" or "cannot edit a completed task" are domain
logic that belongs in the service layer. Controllers handle HTTP.
Services handle rules. Repositories handle queries.

### Why Testcontainers instead of H2?

H2 is an in-memory database that mostly behaves like PostgreSQL.
The problem is "mostly." Different SQL dialects, different
constraint behavior, different function support. Tests that pass
on H2 can still fail in production. Testcontainers spins up a
real PostgreSQL so tests run against the same database as
production.

### Task status transitions

Tasks follow a defined lifecycle:
TODO -> IN_PROGRESS -> DONE
-> CANCELLED

Rules enforced:
- Cannot skip statuses (TODO cannot go directly to DONE)
- Cannot mark DONE without an assignee
- DONE and CANCELLED are terminal — no further changes allowed
- Completed tasks cannot be edited

## What I learned building this

- Why DTOs matter for API stability when database schemas evolve
- The difference between FetchType.LAZY and EAGER, and why
  defaults are not always right
- How to enforce business rules in the service layer rather than
  scattering them across controllers
- Spring Boot 4 changes — flyway-core no longer auto-configures,
  needed to switch to spring-boot-starter-flyway
- Why Testcontainers is worth the small extra setup over H2
- Writing JPQL queries that handle optional filters cleanly

## Future Improvements

- Authentication and authorization (Spring Security + JWT)
- Async event publishing on task changes (Kafka)
- Caching frequently-read data (Redis)
- API documentation with OpenAPI / Swagger
- Rate limiting on public endpoints
- Soft delete instead of hard delete
- Audit fields (createdBy, updatedBy)

## Notes from Building This

### Spring Boot 4 + Testcontainers 2.x in CI

I hit a tricky issue when setting up GitHub Actions. Testcontainers
worked locally with the standard static `@Container` pattern, but
in CI the second test class would fail with "connection closed"
errors. The Postgres container started successfully, but Spring
created a new connection pool (HikariPool-2) for each test class,
and the connections to the original container were being dropped.

The root cause was that Spring Boot 4 + Testcontainers 2.x do not
reliably reuse the static container reference across multiple
test contexts.

The fix was to switch from the static `@Container` pattern to
Spring Boot 4's recommended approach — declaring the container as
a Spring-managed bean using `@TestConfiguration` and
`@ServiceConnection`:

```java
@Import(BaseIntegrationTest.TestcontainersConfiguration.class)
public abstract class BaseIntegrationTest {

    @TestConfiguration(proxyBeanMethods = false)
    static class TestcontainersConfiguration {

        @Bean
        @ServiceConnection
        PostgreSQLContainer postgresContainer() {
            return new PostgreSQLContainer("postgres:16-alpine");
        }
    }
}
```

With the container managed as a bean, Spring handles its
lifecycle properly across test classes. CI now passes reliably
on every push.

### Spring Boot 4 dependency changes

Spring Boot 4 split several modules that used to be bundled.
A few that caught me out:

- `flyway-core` no longer auto-configures Flyway — needed to
  switch to `spring-boot-starter-flyway`
- `MockMvc` auto-configuration moved to a separate module —
  `spring-boot-starter-webmvc-test`
- `PostgreSQLContainer` moved package — now in
  `org.testcontainers.postgresql` instead of
  `org.testcontainers.containers`

These are documented in the Spring Boot 4 migration guide but
caused real debugging time before I tracked them down.
---

Built by [MD SAMIAL HASAN SOHAN](https://github.com/mdsamialsohan) — feedback welcome.