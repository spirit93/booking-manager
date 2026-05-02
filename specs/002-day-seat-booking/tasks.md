# Tasks: Day-Based Seat Booking

**Input**: Design documents from `specs/002-day-seat-booking/`
**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `contracts/openapi.yaml`, `quickstart.md`
**Tests**: Required by `.specify/memory/constitution.md`; write story tests before implementation and confirm they fail.
**Organization**: Tasks are grouped by user story so each story can be implemented and tested independently.

## Extension Hooks

**Optional Pre-Hook**: git
Command: `/speckit.git.commit`
Description: Auto-commit before task generation

Prompt: Commit outstanding changes before task generation?
To execute: `/speckit.git.commit`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Establish the Spring Boot backend and React/Vite frontend structure required by the plan.

- [X] T001 Create backend Gradle project files for Java 21 Spring Boot in `backend/settings.gradle` and `backend/build.gradle`
- [X] T002 Create backend application entry point in `backend/src/main/java/com/vits/booking/BookingManagerApplication.java`
- [X] T003 [P] Create backend runtime and test configuration in `backend/src/main/resources/application.yml` and `backend/src/test/resources/application-test.yml`
- [X] T004 Create frontend Vite React TypeScript project files in `frontend/package.json`, `frontend/tsconfig.json`, and `frontend/vite.config.ts`
- [X] T005 [P] Create frontend application entry files in `frontend/src/main.tsx` and `frontend/src/App.tsx`
- [X] T006 [P] Create frontend test setup in `frontend/src/test/setup.ts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared persistence, DTO, error, and API infrastructure that all user stories depend on.

**CRITICAL**: No user story work can begin until this phase is complete.

- [X] T007 Create Liquibase master changelog in `backend/src/main/resources/db/changelog/db.changelog-master.yaml`
- [X] T008 Create initial seat and booking schema migration with active `(seat_id, booking_day)` uniqueness in `backend/src/main/resources/db/changelog/changes/001-create-seat-booking-schema.yaml`
- [X] T009 Create `Seat` entity and `SeatRepository` in `backend/src/main/java/com/vits/booking/seat/Seat.java` and `backend/src/main/java/com/vits/booking/seat/SeatRepository.java`
- [X] T010 Create `Booking` entity and `BookingRepository` in `backend/src/main/java/com/vits/booking/booking/Booking.java` and `backend/src/main/java/com/vits/booking/booking/BookingRepository.java`
- [X] T011 [P] Create shared API error DTO and exception handling in `backend/src/main/java/com/vits/booking/common/ApiError.java` and `backend/src/main/java/com/vits/booking/common/ApiExceptionHandler.java`
- [X] T012 [P] Create frontend HTTP request helper in `frontend/src/shared/request.ts`
- [X] T013 [P] Create typed frontend booking API models in `frontend/src/booking/types.ts`
- [X] T014 [P] Add backend schema/repository baseline tests for seat seeding and active booking uniqueness in `backend/src/test/java/com/vits/booking/booking/ActiveBookingUniquenessTest.java`

**Checkpoint**: Foundation ready; user story implementation can now begin.

---

## Phase 3: User Story 1 - View Seat Availability For A Day (Priority: P1) MVP

**Goal**: A visitor selects a calendar day and sees free/occupied seats for that day only.

**Independent Test**: Seed bookings for two days, select each day, and verify the same seat can be occupied on one day and available on another.

### Tests for User Story 1

- [X] T015 [P] [US1] Add controller tests for `GET /api/seats/availability?day=YYYY-MM-DD` success and invalid-day errors in `backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java`
- [X] T016 [P] [US1] Add service tests for date-scoped availability mapping in `backend/src/test/java/com/vits/booking/seat/SeatServiceTest.java`
- [X] T017 [P] [US1] Add React interaction tests for day switching, loading, empty, and failure states in `frontend/src/booking/SeatBookingPage.test.tsx`

### Implementation for User Story 1

- [X] T018 [P] [US1] Create `SeatAvailabilityResponse` DTO in `backend/src/main/java/com/vits/booking/seat/SeatAvailabilityResponse.java`
- [X] T019 [US1] Implement date-scoped availability queries in `backend/src/main/java/com/vits/booking/booking/BookingRepository.java`
- [X] T020 [US1] Implement `SeatService` availability calculation in `backend/src/main/java/com/vits/booking/seat/SeatService.java`
- [X] T021 [US1] Implement `SeatController` availability endpoint and day validation in `backend/src/main/java/com/vits/booking/seat/SeatController.java`
- [X] T022 [P] [US1] Implement frontend availability API client in `frontend/src/booking/api.ts`
- [X] T023 [US1] Implement `useSeatAvailability` loading, empty, and failure states in `frontend/src/booking/useSeatAvailability.ts`
- [X] T024 [US1] Implement accessible date selection and availability layout in `frontend/src/booking/SeatBookingPage.tsx`
- [X] T025 [US1] Implement keyboard-usable seat map with free and occupied states in `frontend/src/booking/SeatMap.tsx`
- [X] T026 [US1] Wire booking page into the app shell in `frontend/src/App.tsx`

**Checkpoint**: User Story 1 is fully functional and independently testable.

---

## Phase 4: User Story 2 - Book A Seat For A Selected Day (Priority: P1)

**Goal**: A visitor chooses an available seat, confirms the booking, and sees that seat become occupied for the selected day.

**Independent Test**: Select a day, book a free seat, verify it becomes occupied for that day, and verify duplicate booking attempts return a clear conflict.

### Tests for User Story 2

- [X] T027 [P] [US2] Add controller tests for `POST /api/bookings` created, validation, not-found, and conflict responses in `backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java`
- [X] T028 [P] [US2] Add service tests for booking creation, disabled/unknown seat rejection, and duplicate booking rejection in `backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java`
- [X] T029 [P] [US2] Add React interaction tests for selecting a seat, confirming booking, conflict display, and availability refresh in `frontend/src/booking/SeatBookingPage.test.tsx`

### Implementation for User Story 2

- [X] T030 [P] [US2] Create booking request and response DTOs in `backend/src/main/java/com/vits/booking/booking/BookingRequest.java` and `backend/src/main/java/com/vits/booking/booking/BookingResponse.java`
- [X] T031 [US2] Implement transactional booking creation and conflict handling in `backend/src/main/java/com/vits/booking/booking/BookingService.java`
- [X] T032 [US2] Implement `BookingController` create endpoint with Bean Validation in `backend/src/main/java/com/vits/booking/booking/BookingController.java`
- [X] T033 [US2] Add structured booking success/conflict/failure logging in `backend/src/main/java/com/vits/booking/booking/BookingService.java`
- [X] T034 [US2] Extend frontend API client with `createBooking` and typed error mapping in `frontend/src/booking/api.ts`
- [X] T035 [US2] Add selected-seat confirmation, validation, success, and conflict states in `frontend/src/booking/SeatBookingPage.tsx`
- [X] T036 [US2] Clear or validate selected seat when the selected day changes in `frontend/src/booking/SeatBookingPage.tsx`

**Checkpoint**: User Stories 1 and 2 both work independently.

---

## Phase 5: User Story 3 - Preserve Booking Information (Priority: P2)

**Goal**: Confirmed bookings remain persisted and continue to affect only their own booking day after refreshes or later visits.

**Independent Test**: Create a booking, reload or restart the flow, revisit the same day, and verify the seat remains occupied while other days remain unaffected.

### Tests for User Story 3

- [X] T037 [P] [US3] Add repository persistence tests for retaining bookings across transactions and filtering by day in `backend/src/test/java/com/vits/booking/booking/BookingRepositoryTest.java`
- [X] T038 [P] [US3] Add backend integration test for create-then-read persistence behavior in `backend/src/test/java/com/vits/booking/booking/BookingPersistenceFlowTest.java`
- [X] T039 [P] [US3] Add frontend test for reloading availability after an existing booking is returned by the API in `frontend/src/booking/SeatBookingPage.test.tsx`

### Implementation for User Story 3

- [X] T040 [US3] Seed deterministic predefined seats through Liquibase in `backend/src/main/resources/db/changelog/changes/001-create-seat-booking-schema.yaml`
- [X] T041 [US3] Ensure repository read methods include only active bookings for the requested day in `backend/src/main/java/com/vits/booking/booking/BookingRepository.java`
- [X] T042 [US3] Ensure availability responses include persisted booking identifiers only for occupied seats in `backend/src/main/java/com/vits/booking/seat/SeatService.java`
- [X] T043 [US3] Ensure the frontend reload path renders persisted occupied seats without local booking state in `frontend/src/booking/useSeatAvailability.ts`

**Checkpoint**: All user stories are independently functional.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Verification, documentation, and final quality gates across all stories.

- [X] T044 [P] Update API compatibility notes for `GET /api/seats/availability` and `POST /api/bookings` in `specs/002-day-seat-booking/contracts/openapi.yaml`
- [X] T045 [P] Update developer verification notes and any discovered setup differences in `specs/002-day-seat-booking/quickstart.md`
- [X] T046 Run backend automated quality gate with `./gradlew test` from `backend/` and record failures or fixes in `specs/002-day-seat-booking/quickstart.md`
- [X] T047 Run frontend automated quality gate with `npm test` from `frontend/` and record failures or fixes in `specs/002-day-seat-booking/quickstart.md`
- [ ] T048 Validate manual acceptance flow for `2026-05-02` and `2026-05-03` from `specs/002-day-seat-booking/quickstart.md`
- [X] T049 Review UI accessibility for labels, keyboard seat controls, and actionable error states in `frontend/src/booking/SeatBookingPage.tsx` and `frontend/src/booking/SeatMap.tsx`
- [X] T050 Review backend validation and error responses for sensitive detail leakage in `backend/src/main/java/com/vits/booking/common/ApiExceptionHandler.java`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion; blocks all user stories.
- **User Stories (Phase 3+)**: Depend on Foundational completion.
- **Polish (Phase 6)**: Depends on all desired stories being complete.

### User Story Dependencies

- **US1 - View Seat Availability For A Day (P1)**: Starts after Foundational; MVP scope.
- **US2 - Book A Seat For A Selected Day (P1)**: Starts after Foundational; uses the same entities and can proceed alongside US1, then integrates with availability refresh.
- **US3 - Preserve Booking Information (P2)**: Starts after Foundational; benefits from US1/US2 behavior but is independently testable through persistence tests.

### Within Each User Story

- Tests first and failing before implementation.
- DTOs/entities before services.
- Services before controllers.
- Backend API and frontend API client before UI integration.
- Complete each checkpoint before moving to the next priority when working sequentially.

### Parallel Opportunities

- T003, T005, and T006 can run in parallel after T001/T004 ownership is clear.
- T011, T012, T013, and T014 can run in parallel during Foundation.
- US1 test tasks T015, T016, and T017 can run in parallel.
- US2 test tasks T027, T028, and T029 can run in parallel.
- US3 test tasks T037, T038, and T039 can run in parallel.
- Different user stories can proceed in parallel after Phase 2 if separate developers avoid shared-file conflicts.

---

## Parallel Example: User Story 1

```text
Task: "T015 [P] [US1] Add controller tests for GET /api/seats/availability in backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java"
Task: "T016 [P] [US1] Add service tests for date-scoped availability mapping in backend/src/test/java/com/vits/booking/seat/SeatServiceTest.java"
Task: "T017 [P] [US1] Add React interaction tests for day switching, loading, empty, and failure states in frontend/src/booking/SeatBookingPage.test.tsx"
```

## Parallel Example: User Story 2

```text
Task: "T027 [P] [US2] Add controller tests for POST /api/bookings in backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java"
Task: "T028 [P] [US2] Add service tests for booking creation and duplicate rejection in backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java"
Task: "T029 [P] [US2] Add React interaction tests for booking confirmation and conflict display in frontend/src/booking/SeatBookingPage.test.tsx"
```

## Parallel Example: User Story 3

```text
Task: "T037 [P] [US3] Add repository persistence tests in backend/src/test/java/com/vits/booking/booking/BookingRepositoryTest.java"
Task: "T038 [P] [US3] Add backend create-then-read persistence flow test in backend/src/test/java/com/vits/booking/booking/BookingPersistenceFlowTest.java"
Task: "T039 [P] [US3] Add frontend test for returned persisted bookings in frontend/src/booking/SeatBookingPage.test.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup.
2. Complete Phase 2: Foundational.
3. Complete Phase 3: US1 availability read flow.
4. Stop and validate US1 independently with backend and frontend tests.

### Incremental Delivery

1. Setup + Foundational -> schema, entities, API/error/test scaffolding ready.
2. US1 -> day-scoped availability can be viewed and tested.
3. US2 -> seats can be booked and conflicts are handled.
4. US3 -> persistence confidence and refresh/later-visit behavior are verified.
5. Polish -> quality gates, docs, contract notes, and manual acceptance.

### Suggested MVP Scope

Deliver **US1 - View Seat Availability For A Day** first. It proves date selection, day-scoped read models, backend validation, frontend loading/failure states, and the seat map before adding write-path conflict complexity.

## Final Extension Hooks

**Optional Hook**: git
Command: `/speckit.git.commit`
Description: Auto-commit after task generation

Prompt: Commit task changes?
To execute: `/speckit.git.commit`
