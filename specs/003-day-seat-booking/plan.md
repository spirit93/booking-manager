# Implementation Plan: Day-Based Seat Booking

**Branch**: `003-day-seat-booking` | **Date**: 2026-05-02 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-day-seat-booking/spec.md`

## Summary

Extend the existing seat booking workflow so availability and bookings are scoped by a selected calendar day. The frontend will expose a visible day selector, include the day in availability and booking requests, and refresh day-specific availability after success or conflicts. The Spring Boot backend will accept day-specific availability queries and booking commands, validate supported dates, persist `bookedDay` on bookings, and enforce uniqueness for active bookings by `(seat_id, booked_day)`.

## Technical Context

**Language/Version**: Frontend: TypeScript with React; Backend: Java 21 with Spring Boot 4  
**Primary Dependencies**: React, Vite, React Testing Library, Spring Web, Spring Validation, Spring Data JPA, PostgreSQL JDBC driver, Liquibase, Gradle  
**Storage**: PostgreSQL stores seats and bookings; Liquibase owns schema changes, including adding a required `booked_day` column and an active booking uniqueness constraint by seat/day  
**Testing**: Frontend component/hook/API tests with React Testing Library and Vitest; backend unit tests, Spring MVC tests, repository/integration tests with PostgreSQL-compatible test database  
**Target Platform**: Browser frontend served separately during development; JVM backend service on Linux/container-friendly runtime  
**Project Type**: Web application with separate frontend and backend projects  
**Performance Goals**: Day-specific seat availability loads within 2 seconds under normal conditions; booking confirmation returns within 1 second for typical requests; UI remains responsive during day changes and booking submission  
**Constraints**: Prevent duplicate active bookings for the same seat on the same day; allow the same seat across different days; validate all external input, including missing/invalid/out-of-range days; refresh selected-day availability after successful bookings and conflict responses; do not commit secrets or environment-specific credentials  
**Scale/Scope**: Existing single booking page and REST API refined for day-based behavior, with PostgreSQL schema migration, OpenAPI contract updates, and automated tests for cross-day availability and same-day conflict behavior

## Constitution Check

*GATE: Passed before Phase 0 research. Re-checked after Phase 1 design: Passed.*

- **React frontend quality**: Day selection will be owned by booking feature state/hooks, while typed components render the selected day, day-specific availability, booking form state, and accessible loading/error/confirmation states. Inputs will have accessible labels and deterministic validation feedback.
- **Spring Boot backend quality**: Controllers will handle HTTP parameters and DTO validation only. Services will own day range checks, booking conflict rules, and transaction boundaries. Repositories will expose day-aware availability/conflict queries, and Liquibase will represent all persistence changes.
- **Testing quality gates**: Required coverage includes frontend day selector and refresh tests, API serialization tests for `day`, backend booking service tests for same-day conflicts and different-day success, MVC validation/error tests, and repository/integration tests for the `(seat_id, booked_day)` active uniqueness rule.
- **Contract and validation stability**: REST contract changes are documented in `contracts/openapi.yaml`. This feature intentionally changes request/response payloads by adding `day`/`bookedDay` and changing availability to `GET /api/seats/availability?day=YYYY-MM-DD`; frontend and backend tests must update together.
- **Operability, security, maintainability**: Backend conflict and validation logs must include seat/day context without personal data or secrets. Frontend async flows must expose loading, empty, success, conflict, validation, and temporary failure states. Date range settings must be environment-driven or centrally configurable.

No constitution exceptions are required.

## Project Structure

### Documentation (this feature)

```text
specs/003-day-seat-booking/
|-- plan.md
|-- research.md
|-- data-model.md
|-- quickstart.md
|-- contracts/
|   `-- openapi.yaml
`-- checklists/
    `-- requirements.md
```

### Source Code (repository root)

```text
backend/
|-- build.gradle
|-- settings.gradle
|-- gradle/
|-- src/
|   |-- main/
|   |   |-- java/com/vits/booking/
|   |   |   |-- BookingManagerApplication.java
|   |   |   |-- booking/
|   |   |   |   |-- BookingController.java
|   |   |   |   |-- BookingService.java
|   |   |   |   |-- BookingRepository.java
|   |   |   |   |-- BookingEntity.java
|   |   |   |   |-- CreateBookingRequest.java
|   |   |   |   `-- BookingResponse.java
|   |   |   |-- seat/
|   |   |   |   |-- SeatController.java
|   |   |   |   |-- SeatService.java
|   |   |   |   |-- SeatRepository.java
|   |   |   |   `-- SeatResponse.java
|   |   |   `-- common/
|   |   `-- resources/
|   |       |-- application.yml
|   |       `-- db/changelog/
|   |           |-- db.changelog-master.yaml
|   |           `-- changes/
|   `-- test/java/com/vits/booking/
|
frontend/
|-- package.json
|-- vite.config.ts
|-- tsconfig.json
|-- src/
|   |-- app/
|   |-- features/booking/
|   |   |-- api/
|   |   |-- components/
|   |   |-- hooks/
|   |   `-- types.ts
|   |-- shared/
|   `-- test/
`-- README.md
```

**Structure Decision**: Continue using the existing separate `frontend/` and `backend/` projects. The feature changes an established React booking page and Spring Boot REST service with independent tests and runtime concerns, so no new project boundary is required.

## Complexity Tracking

No constitution violations or additional complexity exceptions are required.
