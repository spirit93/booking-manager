# Implementation Plan: Seat Booking

**Branch**: `001-seat-booking` | **Date**: 2026-05-02 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-seat-booking/spec.md`

## Summary

Build a full-stack seat booking application where users can view available and occupied seats, select one available seat, submit a booking through a REST API, and receive clear feedback for successful bookings, validation failures, conflicts, and temporary service failures. The frontend will be a React + TypeScript app. The backend will be Java 21 with Spring Boot 4, Gradle, PostgreSQL persistence, and Liquibase-managed schema migrations.

## Technical Context

**Language/Version**: Frontend: TypeScript with React; Backend: Java 21 with Spring Boot 4  
**Primary Dependencies**: React, Vite, React Testing Library, Spring Web, Spring Validation, Spring Data JPA, PostgreSQL JDBC driver, Liquibase, Gradle  
**Storage**: PostgreSQL stores seats and bookings; Liquibase owns all schema changes  
**Testing**: Frontend component/hook tests with React Testing Library; backend unit tests, Spring MVC tests, repository/integration tests with PostgreSQL-compatible test database  
**Target Platform**: Browser frontend served separately during development; JVM backend service on Linux/container-friendly runtime  
**Project Type**: Web application with separate frontend and backend projects  
**Performance Goals**: Seat availability view loads within 2 seconds under normal conditions; booking confirmation returns within 1 second for typical requests; UI remains responsive during async operations  
**Constraints**: Prevent duplicate active bookings for the same seat; validate all external input; refresh availability after successful bookings and conflict responses; do not commit secrets or environment-specific credentials  
**Scale/Scope**: Initial single booking page, REST API, PostgreSQL schema for seats/bookings/customers, and automated tests for core availability and conflict behavior

## Constitution Check

*GATE: Passed before Phase 0 research. Re-checked after Phase 1 design: Passed.*

- **React frontend quality**: The frontend will use typed components for seat grid/list rendering, dedicated hooks/services for API calls and booking state, accessible labels for booking inputs, disabled states for occupied seats, and responsive layouts for the booking page.
- **Spring Boot backend quality**: The backend will separate controllers, DTOs, services, repositories, and persistence entities. Bean Validation will protect request DTOs, service methods will own booking rules and transactions, repositories will encapsulate database access, and Liquibase will represent all schema changes.
- **Testing quality gates**: Required tests include frontend availability/selection/feedback component tests, backend booking rule unit tests, MVC tests for REST payloads and error formats, persistence/integration tests for unique active booking enforcement, and conflict regression tests.
- **Contract and validation stability**: REST payloads, response shapes, status codes, and error formats are documented in `contracts/openapi.yaml`. This is the first version of the contract, so there is no backward compatibility break yet; future changes must update contracts and tests together.
- **Operability, security, maintainability**: Backend booking conflicts and failures will log structured events without personal data or secrets. Frontend async flows will expose loading, empty, success, and failure states. Configuration will be environment-driven for database connection values.

No constitution exceptions are required.

## Project Structure

### Documentation (this feature)

```text
specs/001-seat-booking/
|-- plan.md
|-- research.md
|-- data-model.md
|-- quickstart.md
|-- contracts/
|   `-- openapi.yaml
`-- tasks.md
```

### Source Code (repository root)

```text
backend/
|-- build.gradle
|-- settings.gradle
|-- gradle/
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   `-- com/vits/booking/
|   |   |       |-- BookingManagerApplication.java
|   |   |       |-- booking/
|   |   |       |-- seat/
|   |   |       |-- customer/
|   |   |       |-- common/
|   |   |       `-- config/
|   |   `-- resources/
|   |       |-- application.yml
|   |       `-- db/changelog/
|   |           |-- db.changelog-master.yaml
|   |           `-- changes/
|   `-- test/
|       `-- java/
|           `-- com/vits/booking/
|
frontend/
|-- package.json
|-- vite.config.ts
|-- tsconfig.json
|-- src/
|   |-- app/
|   |-- components/
|   |-- features/booking/
|   |   |-- api/
|   |   |-- components/
|   |   |-- hooks/
|   |   `-- types.ts
|   |-- shared/
|   `-- test/
`-- README.md
```

**Structure Decision**: Use separate `frontend/` and `backend/` projects because the feature has a browser UI and a REST-backed Spring Boot service with independent dependencies, tests, and runtime concerns.

## Complexity Tracking

No constitution violations or additional complexity exceptions are required.
