# Research: Day-Based Seat Booking

## Decision: Represent booking days as date-only values

Use ISO `YYYY-MM-DD` values in the REST API, TypeScript types, Java `LocalDate`, and PostgreSQL `date` columns.

**Rationale**: The feature reserves a seat for one full calendar day and explicitly excludes time-of-day and partial-day reservations. Date-only values avoid timezone conversion surprises between the browser, JVM, and database.

**Alternatives considered**: `Instant` or timestamp columns were rejected because they imply time-of-day semantics. Localized date strings were rejected because they complicate validation, sorting, OpenAPI examples, and backend parsing.

## Decision: Add `booked_day` to booking persistence and require it for active bookings

Persist the booked day on each booking row and make new confirmed bookings require a non-null day.

**Rationale**: Availability and conflict rules depend on the exact day associated with a booking. Keeping the day on the booking record makes historical and future queries direct and testable.

**Alternatives considered**: A separate booking-day table was rejected as unnecessary for single-day bookings. Deriving day from `created_at` was rejected because creation time is not the reservation date.

## Decision: Enforce conflicts with a database-backed active uniqueness rule

Use a uniqueness mechanism for active bookings by seat and booked day, supported by service-level checks and conflict handling.

**Rationale**: Service checks improve user feedback, but concurrent requests can still race. A database-backed uniqueness rule is the final authority for preventing duplicate active bookings for the same seat/day.

**Alternatives considered**: Service-only locking was rejected because it is easier to bypass under concurrency. Full pessimistic locking was rejected for this scope because a uniqueness rule is simpler and matches the invariant.

## Decision: Query availability through a day-specific endpoint

Expose `GET /api/seats/availability?day=YYYY-MM-DD` for day-scoped availability while keeping booking creation under `POST /api/bookings`.

**Rationale**: The current `GET /api/seats` shape is not day-aware. A dedicated availability endpoint makes the required day explicit and allows clear validation responses for missing, malformed, or out-of-range days.

**Alternatives considered**: Reusing `GET /api/seats` without a required query parameter was rejected because it would hide day scope. Embedding the date in the path was rejected because query parameters better fit filtering a collection by a selected day.

## Decision: Refresh selected-day availability after success and conflict

The frontend should reload availability for the active day after a successful booking and after a conflict response.

**Rationale**: Success changes the selected day's state, and conflicts can happen after the page initially loaded. Refreshing the active day keeps the UI consistent with backend truth.

**Alternatives considered**: Optimistic-only updates were rejected because they do not handle stale availability after another user books first. A full page reload was rejected because it is disruptive and unnecessary.

## Decision: Configure a supported booking date range centrally

Validate booking days against a supported range in the backend service layer, and expose actionable validation errors when a day is outside that range.

**Rationale**: The spec requires out-of-range dates to be invalid. Centralized range validation keeps availability queries and booking creation consistent.

**Alternatives considered**: Frontend-only range constraints were rejected because clients can bypass them. Hardcoding ranges across controllers was rejected because it duplicates business rules.
