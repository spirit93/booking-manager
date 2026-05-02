# Tasks: Day-Based Seat Booking

**Input**: Design documents from `/specs/003-day-seat-booking/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/openapi.yaml, quickstart.md

**Tests**: Automated tests are required by the constitution for frontend behavior, backend business rules, API validation, persistence invariants, and contract-aligned payload changes.

**Organization**: Tasks are grouped by user story so each story can be implemented and tested as an independently valuable increment.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel with other tasks in the same phase because it touches different files or has no dependency on incomplete work
- **[Story]**: User story traceability label, used only in user story phases
- Every task includes an exact file path

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Confirm the existing React and Spring Boot projects are ready for day-based booking changes.

- [X] T001 Inspect backend dependencies and test configuration for Java 21, Spring Validation, JPA, Liquibase, and PostgreSQL support in backend/build.gradle
- [X] T002 Inspect frontend dependencies and test scripts for React, TypeScript, Vitest, and React Testing Library support in frontend/package.json
- [X] T003 [P] Review existing backend test profile and database setup for date-aware persistence tests in backend/src/test/resources/application-test.yml
- [X] T004 [P] Review existing frontend test setup for component, hook, and API tests in frontend/src/test/setup.ts
- [X] T005 [P] Record day-based API contract expectations from specs/003-day-seat-booking/contracts/openapi.yaml in frontend/src/features/booking/api/bookingApi.test.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Add shared date, validation, persistence, and API structures required before user stories can be completed.

**CRITICAL**: No user story implementation can be considered complete until this phase is done.

- [X] T006 Add required booked_day column and active uniqueness constraint by seat/day in backend/src/main/resources/db/changelog/changes/002-add-booked-day-to-bookings.yaml
- [X] T007 Include the booked-day Liquibase changeset in backend/src/main/resources/db/changelog/db.changelog-master.yaml
- [X] T008 Add bookedDay and date mapping to BookingEntity in backend/src/main/java/com/vits/booking/booking/BookingEntity.java
- [X] T009 Add bookedDay to BookingResponse in backend/src/main/java/com/vits/booking/booking/BookingResponse.java
- [X] T010 Add bookedDay validation to CreateBookingRequest in backend/src/main/java/com/vits/booking/booking/CreateBookingRequest.java
- [X] T011 Create centralized booking day range configuration in backend/src/main/java/com/vits/booking/booking/BookingDayProperties.java
- [X] T012 Create booking day validation service for availability and booking commands in backend/src/main/java/com/vits/booking/booking/BookingDayValidator.java
- [X] T013 Add validation and conflict error response support for day-specific failures in backend/src/main/java/com/vits/booking/common/api/GlobalExceptionHandler.java
- [X] T014 [P] Add day/bookedDay fields to frontend booking domain types in frontend/src/features/booking/types.ts
- [X] T015 [P] Add default selected-day helper for local ISO dates in frontend/src/features/booking/hooks/useBookingDay.ts
- [X] T016 [P] Add supported booking day range configuration values in backend/src/main/resources/application.yml

**Checkpoint**: Foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - View Availability for a Selected Day (Priority: P1) MVP

**Goal**: A user can choose a calendar day and see seat availability scoped to that day.

**Independent Test**: Prepare a booking for A1 on 2026-05-02 only, request/select 2026-05-02 and 2026-05-03 independently, and verify A1 changes from occupied to free based on the selected day.

### Tests for User Story 1

- [X] T017 [P] [US1] Add repository test for day-specific seat availability queries in backend/src/test/java/com/vits/booking/seat/SeatRepositoryTest.java
- [X] T018 [P] [US1] Add MVC tests for GET /api/seats/availability day validation and response day payload in backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java
- [X] T019 [P] [US1] Add API client test for availability requests with day query parameter in frontend/src/features/booking/api/bookingApi.test.ts
- [X] T020 [P] [US1] Add hook test for default day and reload-on-day-change behavior in frontend/src/features/booking/hooks/useSeats.test.tsx
- [X] T021 [P] [US1] Add booking page test for visible selected day, loading, empty, and failure states in frontend/src/features/booking/components/BookingPage.test.tsx

### Implementation for User Story 1

- [X] T022 [US1] Add day-aware availability query methods in backend/src/main/java/com/vits/booking/seat/SeatRepository.java
- [X] T023 [US1] Return availability with selected day from SeatService in backend/src/main/java/com/vits/booking/seat/SeatService.java
- [X] T024 [US1] Add day payload wrapper for availability responses in backend/src/main/java/com/vits/booking/seat/SeatAvailabilityResponse.java
- [X] T025 [US1] Expose GET /api/seats/availability?day=YYYY-MM-DD in backend/src/main/java/com/vits/booking/seat/SeatController.java
- [X] T026 [US1] Update frontend API client to call /api/seats/availability with day in frontend/src/features/booking/api/bookingApi.ts
- [X] T027 [US1] Update useSeats to own selected-day availability loading and refetch behavior in frontend/src/features/booking/hooks/useSeats.ts
- [X] T028 [US1] Add accessible day selector and selected-day state wiring in frontend/src/features/booking/components/BookingPage.tsx
- [X] T029 [US1] Display selected-day availability states in frontend/src/features/booking/components/SeatGrid.tsx
- [X] T030 [US1] Style responsive day selector and availability states in frontend/src/app/App.css

**Checkpoint**: User Story 1 is independently functional and testable.

---

## Phase 4: User Story 2 - Book a Seat for One Day (Priority: P2)

**Goal**: A user can book an available seat for the currently selected day, and the confirmed booking records and displays that day.

**Independent Test**: Select B2 for 2026-05-02, create a booking, verify the response includes bookedDay=2026-05-02, and verify B2 remains available for 2026-05-03.

### Tests for User Story 2

- [X] T031 [P] [US2] Add service tests for creating bookings with bookedDay and allowing same seat on different days in backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java
- [X] T032 [P] [US2] Add controller tests for POST /api/bookings bookedDay validation and response payload in backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java
- [X] T033 [P] [US2] Add repository test proving same seat can have active bookings on different days in backend/src/test/java/com/vits/booking/booking/BookingRepositoryTest.java
- [X] T034 [P] [US2] Add API client test for booking request payload with bookedDay in frontend/src/features/booking/api/bookingApi.test.ts
- [X] T035 [P] [US2] Add booking form test for submitting selected day and showing seat/day confirmation in frontend/src/features/booking/components/BookingForm.test.tsx

### Implementation for User Story 2

- [X] T036 [US2] Persist bookedDay when creating bookings in backend/src/main/java/com/vits/booking/booking/BookingService.java
- [X] T037 [US2] Return bookedDay in booking responses from backend/src/main/java/com/vits/booking/booking/BookingController.java
- [X] T038 [US2] Update frontend create booking API payload and response parsing in frontend/src/features/booking/api/bookingApi.ts
- [X] T039 [US2] Update useCreateBooking to accept selected day and surface day-specific success state in frontend/src/features/booking/hooks/useCreateBooking.ts
- [X] T040 [US2] Pass selected day into booking submission and refresh availability after success in frontend/src/features/booking/components/BookingPage.tsx
- [X] T041 [US2] Show selected day in booking form and confirmation copy in frontend/src/features/booking/components/BookingForm.tsx

**Checkpoint**: User Stories 1 and 2 work independently and together.

---

## Phase 5: User Story 3 - Prevent Same-Day Booking Conflicts (Priority: P3)

**Goal**: The system rejects duplicate active bookings for the same seat on the same day while allowing valid bookings on other days.

**Independent Test**: Attempt to book C3 twice for 2026-05-02 and verify a conflict, then book C3 for 2026-05-03 and verify success when free.

### Tests for User Story 3

- [X] T042 [P] [US3] Add active booking uniqueness integration test for same seat and bookedDay in backend/src/test/java/com/vits/booking/booking/ActiveBookingUniquenessTest.java
- [X] T043 [P] [US3] Add service conflict tests for same-day duplicate bookings and different-day success in backend/src/test/java/com/vits/booking/booking/BookingConflictTest.java
- [X] T044 [P] [US3] Add controller error tests for 409 same-day conflict messages including bookedDay in backend/src/test/java/com/vits/booking/booking/BookingControllerErrorTest.java
- [X] T045 [P] [US3] Add frontend hook test for conflict response refreshing selected-day availability in frontend/src/features/booking/hooks/useCreateBooking.test.tsx
- [X] T046 [P] [US3] Add booking page error test for conflict feedback naming the selected day in frontend/src/features/booking/components/BookingPageErrors.test.tsx

### Implementation for User Story 3

- [X] T047 [US3] Add same-day active conflict lookup methods in backend/src/main/java/com/vits/booking/booking/BookingRepository.java
- [X] T048 [US3] Enforce same-seat same-day conflict checks and translate database uniqueness violations in backend/src/main/java/com/vits/booking/booking/BookingService.java
- [X] T049 [US3] Include seat and bookedDay context in conflict errors without sensitive data in backend/src/main/java/com/vits/booking/common/domain/BookingConflictException.java
- [X] T050 [US3] Map 409 conflict payloads to actionable frontend errors in frontend/src/features/booking/types.ts
- [X] T051 [US3] Refresh selected-day availability after conflict responses in frontend/src/features/booking/components/BookingPage.tsx
- [X] T052 [US3] Render day-specific validation and conflict feedback in frontend/src/features/booking/components/BookingForm.tsx

**Checkpoint**: All user stories are independently functional and conflict-safe.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final quality gates, documentation updates, and end-to-end verification.

- [X] T053 [P] Update manual verification and smoke check notes for day-based booking in specs/003-day-seat-booking/quickstart.md
- [X] T054 [P] Update developer-facing API behavior notes in README.md
- [X] T055 [P] Update frontend user flow notes in specs/003-day-seat-booking/quickstart.md
- [X] T056 Run backend automated tests for service, MVC, repository, and integration coverage in backend/build.gradle
- [X] T057 Run frontend automated tests for API, hooks, and components in frontend/package.json
- [ ] T058 Run backend boot smoke check and day-based API smoke requests from specs/003-day-seat-booking/quickstart.md
- [X] T059 Run frontend build or static analysis gate in frontend/package.json
- [X] T060 Review OpenAPI contract alignment after implementation in specs/003-day-seat-booking/contracts/openapi.yaml

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion and blocks all user story completion.
- **User Story 1 (Phase 3)**: Depends on Foundational and is the MVP.
- **User Story 2 (Phase 4)**: Depends on Foundational; integrates with US1 for refresh behavior but can be validated independently through API/service tests.
- **User Story 3 (Phase 5)**: Depends on Foundational; integrates with US1 and US2 for stale availability refresh and booking conflict handling.
- **Polish (Phase 6)**: Depends on all desired user stories being complete.

### User Story Dependencies

- **User Story 1 (P1)**: Start after Phase 2. No dependency on other stories.
- **User Story 2 (P2)**: Start after Phase 2. Uses the selected day state introduced by US1 for full UI integration.
- **User Story 3 (P3)**: Start after Phase 2. Uses booking creation and availability refresh behavior from US1/US2 for full UI integration.

### Within Each User Story

- Write tests first and confirm they fail before implementation.
- Backend persistence and DTO changes precede services.
- Services precede controllers.
- API client changes precede hook/component integration.
- Complete story-specific tests before moving to the next priority checkpoint.

### Parallel Opportunities

- Setup reviews T003, T004, and T005 can run in parallel.
- Foundational frontend types/hooks T014 and T015 can run in parallel with backend configuration T016.
- Story test tasks marked [P] can be written in parallel across backend and frontend files.
- Backend and frontend implementation within a story can be parallelized after the shared contract and data shapes are agreed.
- Polish documentation tasks T053, T054, and T055 can run in parallel.

---

## Parallel Example: User Story 1

```text
Task: "T017 [P] [US1] Add repository test for day-specific seat availability queries in backend/src/test/java/com/vits/booking/seat/SeatRepositoryTest.java"
Task: "T018 [P] [US1] Add MVC tests for GET /api/seats/availability day validation and response day payload in backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java"
Task: "T019 [P] [US1] Add API client test for availability requests with day query parameter in frontend/src/features/booking/api/bookingApi.test.ts"
Task: "T020 [P] [US1] Add hook test for default day and reload-on-day-change behavior in frontend/src/features/booking/hooks/useSeats.test.tsx"
Task: "T021 [P] [US1] Add booking page test for visible selected day, loading, empty, and failure states in frontend/src/features/booking/components/BookingPage.test.tsx"
```

## Parallel Example: User Story 2

```text
Task: "T031 [P] [US2] Add service tests for creating bookings with bookedDay and allowing same seat on different days in backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java"
Task: "T032 [P] [US2] Add controller tests for POST /api/bookings bookedDay validation and response payload in backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java"
Task: "T034 [P] [US2] Add API client test for booking request payload with bookedDay in frontend/src/features/booking/api/bookingApi.test.ts"
Task: "T035 [P] [US2] Add booking form test for submitting selected day and showing seat/day confirmation in frontend/src/features/booking/components/BookingForm.test.tsx"
```

## Parallel Example: User Story 3

```text
Task: "T042 [P] [US3] Add active booking uniqueness integration test for same seat and bookedDay in backend/src/test/java/com/vits/booking/booking/ActiveBookingUniquenessTest.java"
Task: "T043 [P] [US3] Add service conflict tests for same-day duplicate bookings and different-day success in backend/src/test/java/com/vits/booking/booking/BookingConflictTest.java"
Task: "T045 [P] [US3] Add frontend hook test for conflict response refreshing selected-day availability in frontend/src/features/booking/hooks/useCreateBooking.test.tsx"
Task: "T046 [P] [US3] Add booking page error test for conflict feedback naming the selected day in frontend/src/features/booking/components/BookingPageErrors.test.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 setup checks.
2. Complete Phase 2 foundational date, validation, migration, and type work.
3. Complete Phase 3 for selected-day availability.
4. Stop and validate US1 independently through backend tests, frontend tests, and manual day switching.

### Incremental Delivery

1. Deliver selected-day availability as the MVP.
2. Add day-aware booking creation and confirmation.
3. Add same-day conflict prevention and stale availability refresh.
4. Run full backend/frontend quality gates and quickstart smoke checks.

### Parallel Team Strategy

1. One developer owns backend persistence, validation, and service/controller changes.
2. One developer owns frontend API, hooks, and booking page state.
3. One developer owns contract alignment, documentation, and cross-story verification.
4. Integrate at each story checkpoint before progressing to the next priority.

---

## Notes

- [P] tasks are safe to run in parallel because they target distinct files or independent tests.
- [US1], [US2], and [US3] labels map directly to the prioritized user stories in spec.md.
- The MVP scope is User Story 1: selected-day availability.
- Do not skip test tasks unless a reviewed exception is added to specs/003-day-seat-booking/plan.md.
