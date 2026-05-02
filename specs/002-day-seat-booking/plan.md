# Implementation Plan: Day-Based Seat Booking

**Branch**: `002-day-seat-booking` | **Date**: 2026-05-02 | **Spec**: `specs/002-day-seat-booking/spec.md`
**Input**: Feature specification from `specs/002-day-seat-booking/spec.md`

## Summary

Implement full-calendar-day seat booking so users select a date, view seat availability for that date only, and reserve an available seat through a persisted backend API. The plan uses a React + TypeScript frontend backed by a layered Spring Boot service with database-level uniqueness on active `(seat_id, booking_day)` bookings to prevent duplicate reservations under concurrency.

## Technical Context

**Language/Version**: Java 21 for backend; TypeScript with React 19 for frontend  
**Primary Dependencies**: Spring Boot, Spring Web, Spring Data JPA, Bean Validation, Liquibase, PostgreSQL JDBC driver for runtime, H2 for tests; React, Vite, Vitest, Testing Library  
**Storage**: PostgreSQL managed by Liquibase migrations for runtime; H2 in PostgreSQL compatibility mode for automated tests  
**Testing**: Gradle/JUnit/Spring Boot tests for backend; Vitest + React Testing Library for frontend  
**Target Platform**: Browser client served by Vite during development; Spring Boot HTTP API on JVM  
**Project Type**: Web application with separate `backend/` and `frontend/` projects  
**Performance Goals**: Availability lookup and booking confirmation should complete within 2 seconds on local/test datasets; UI should update selected-day availability immediately after successful booking  
**Constraints**: Prevent duplicate active bookings for the same seat and day in both service logic and persistence; validate ISO calendar dates and known seat identifiers before booking; expose loading, empty, success, and failure states  
**Scale/Scope**: Initial single-venue seat map with predefined seats, create/read booking flow only, no authentication, cancellation, editing, or time-of-day ranges in this feature

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **React frontend quality**: Pass. The frontend will use typed API DTOs, focused seat-map/date-picker components, a booking hook or service for remote state, accessible form labels, keyboard-usable seat controls, and responsive layout.
- **Spring Boot backend quality**: Pass. Controllers own HTTP concerns, services own booking rules and transactions, repositories own persistence, DTOs define API boundaries, Bean Validation guards requests, and Liquibase owns schema changes.
- **Testing quality gates**: Pass. Required tests include backend service duplicate-booking tests, repository uniqueness tests, controller validation/error tests, and frontend interaction tests for day switching, loading/failure states, and successful booking refresh.
- **Contract and validation stability**: Pass. API contracts are documented in `specs/002-day-seat-booking/contracts/openapi.yaml`; the feature introduces new booking endpoints and typed error responses rather than changing an existing public contract.
- **Operability, security, maintainability**: Pass. The service logs booking success/conflict/failure events without personal data, returns actionable errors, keeps configuration environment-driven, and adds no new external dependencies beyond the established stack.

Post-design check: Pass. Phase 1 artifacts preserve the same boundaries: `Booking` and `Seat` entities are modeled separately, the OpenAPI contract defines request/response/error shapes, and the quickstart includes required quality gates.

## Project Structure

### Documentation (this feature)

```text
specs/002-day-seat-booking/
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
|-- src/
|   |-- main/
|   |   |-- java/com/vits/booking/
|   |   |   |-- BookingManagerApplication.java
|   |   |   |-- booking/
|   |   |   |   |-- Booking.java
|   |   |   |   |-- BookingController.java
|   |   |   |   |-- BookingRepository.java
|   |   |   |   |-- BookingRequest.java
|   |   |   |   |-- BookingResponse.java
|   |   |   |   `-- BookingService.java
|   |   |   |-- common/
|   |   |   |   `-- ApiError.java
|   |   |   `-- seat/
|   |   |       |-- Seat.java
|   |   |       |-- SeatAvailabilityResponse.java
|   |   |       |-- SeatController.java
|   |   |       |-- SeatRepository.java
|   |   |       `-- SeatService.java
|   |   `-- resources/
|   |       |-- application.yml
|   |       `-- db/changelog/
|   `-- test/
|       |-- java/com/vits/booking/
|       `-- resources/application-test.yml
frontend/
|-- package.json
|-- tsconfig.json
|-- vite.config.ts
|-- src/
|   |-- App.tsx
|   |-- main.tsx
|   |-- booking/
|   |   |-- api.ts
|   |   |-- types.ts
|   |   |-- useSeatAvailability.ts
|   |   |-- SeatBookingPage.tsx
|   |   |-- SeatMap.tsx
|   |   `-- SeatBookingPage.test.tsx
|   `-- shared/
|       `-- request.ts
```

**Structure Decision**: Use the existing two-project web application layout: `backend/` for Spring Boot and `frontend/` for React/Vite. Keep feature code grouped by backend domain packages (`booking`, `seat`, `common`) and frontend booking feature modules.

## Complexity Tracking

No constitution violations or complexity exceptions are required.
