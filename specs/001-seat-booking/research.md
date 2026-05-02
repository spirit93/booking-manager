# Research: Seat Booking

## Decision: Use a separated React frontend and Spring Boot backend

**Rationale**: The feature has a clear browser workflow and a REST API boundary. Separate projects keep frontend build tooling, component tests, and backend persistence/service tests independent while matching the constitution's React and Spring Boot quality rules.

**Alternatives considered**: A single Spring Boot server-rendered UI was rejected because the user requested a React frontend. A monorepo package workspace was deferred until there is shared generated client code or multiple frontend packages.

## Decision: Java 21 with Spring Boot 4 and Gradle for the backend

**Rationale**: Java 21 is the requested runtime and aligns with modern Spring Boot support. Gradle provides concise dependency management and repeatable build/test tasks for the backend service.

**Alternatives considered**: Maven was rejected because the user requested Gradle. Older Java versions were rejected because Java 21 was explicitly requested.

## Decision: PostgreSQL as the source of truth for seats and bookings

**Rationale**: PostgreSQL can enforce booking invariants with transactional writes and uniqueness constraints, which is essential for preventing duplicate active bookings for the same seat under concurrent requests.

**Alternatives considered**: In-memory storage was rejected because bookings must persist. File storage was rejected because it cannot reliably enforce concurrent booking constraints.

## Decision: Liquibase owns database schema changes

**Rationale**: Liquibase gives explicit, reviewable, repeatable migrations for tables, indexes, constraints, and seed data. This satisfies the constitution's migration requirement for persistence changes.

**Alternatives considered**: Hibernate auto-DDL was rejected for managed environments because it hides schema intent and is unsafe for repeatable delivery.

## Decision: REST contract documented with OpenAPI

**Rationale**: The React frontend and Spring Boot backend need an explicit contract for availability, booking creation, conflict responses, validation errors, and service failures. OpenAPI is readable, testable, and can later drive client generation if useful.

**Alternatives considered**: Informal markdown-only endpoint notes were rejected because they are harder to validate against tests and implementation.

## Decision: Prevent conflicts with transaction-level checks plus a database uniqueness constraint

**Rationale**: The service will check seat availability before creating a booking and PostgreSQL will enforce that only one active booking exists for a seat. The database constraint is the final protection against race conditions.

**Alternatives considered**: UI-only disabling of occupied seats was rejected because availability can change between page load and confirmation. Application-only checks were rejected because concurrent requests can bypass them without a database invariant.

## Decision: Model a customer reference in the booking request

**Rationale**: The specification says bookings must preserve the user or customer associated with the booking, while assuming users are identifiable by the surrounding application. The initial contract will accept a `customerId` so the booking can be associated without designing authentication in this feature.

**Alternatives considered**: Free-form customer names were rejected because they do not identify a user/customer reliably. Full authentication/session design was deferred because it is outside the initial scope.
