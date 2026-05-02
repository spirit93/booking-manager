# Research: Day-Based Seat Booking

## Decision: Full-day bookings use `LocalDate` / ISO `YYYY-MM-DD`

**Rationale**: The specification explicitly excludes time ranges. A timezone-free calendar date avoids accidental shifts between browser and backend time zones and matches the visible date picker.

**Alternatives considered**: `Instant` or `LocalDateTime` were rejected because they imply time-of-day semantics the feature does not need.

## Decision: Persist duplicate protection with a unique active seat/day constraint

**Rationale**: Service checks improve user feedback, but only a database uniqueness rule can safely prevent two concurrent requests from creating active bookings for the same seat on the same day.

**Alternatives considered**: In-memory locks were rejected because they do not survive multiple JVMs or restarts. Frontend-only prevention was rejected because it cannot handle concurrent users.

## Decision: Expose date-scoped availability through `GET /api/seats/availability?day=YYYY-MM-DD`

**Rationale**: The UI needs a single day-scoped read model containing all seats and their availability. This keeps the frontend simple and avoids coupling the seat map to raw booking rows.

**Alternatives considered**: Returning all bookings and filtering in the browser was rejected because it leaks unnecessary data and moves business rules out of the backend.

## Decision: Create bookings through `POST /api/bookings`

**Rationale**: A focused command endpoint keeps validation, transaction boundaries, duplicate checks, logging, and error behavior in the backend service layer.

**Alternatives considered**: Mutating seats directly was rejected because bookings are first-class records and seats remain reusable across days.

## Decision: Use typed frontend API services and interaction tests

**Rationale**: The constitution requires type-safe React boundaries and tests for visible behavior. A small API module and hook make loading, empty, success, and failure states deterministic and testable.

**Alternatives considered**: Fetching directly inside presentation components was rejected because it tangles rendering with remote state and makes day-switch behavior harder to test.

## Decision: Use Liquibase for schema changes

**Rationale**: The constitution requires explicit migrations for persistence changes, and the backend stack already expects Spring Boot with migration-managed storage.

**Alternatives considered**: Hibernate auto-DDL was rejected because it is less reviewable and unsuitable for stable contract evolution.
