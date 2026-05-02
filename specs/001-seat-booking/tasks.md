# Tasks: Seat Booking

**Input**: Design documents from `/specs/001-seat-booking/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/openapi.yaml, quickstart.md

**Tests**: Automated tests are required by the constitution. Backend tasks include unit, MVC, and persistence/integration tests; frontend tasks include component, hook, and interaction tests.

**Organization**: Tasks are grouped by user story so each story can be implemented, tested, and validated independently.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel because it touches different files and has no dependency on incomplete tasks
- **[Story]**: User-story label for story phases only
- Every task includes exact repository paths

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Create the separated React frontend and Spring Boot backend project skeletons.

- [X] T001 Create backend Gradle settings and wrapper configuration in `backend/settings.gradle`, `backend/build.gradle`, and `backend/gradle/wrapper/gradle-wrapper.properties`
- [X] T002 Create backend application entry point in `backend/src/main/java/com/vits/booking/BookingManagerApplication.java`
- [X] T003 [P] Create backend environment-driven configuration in `backend/src/main/resources/application.yml`
- [X] T004 Create frontend Vite React TypeScript project metadata in `frontend/package.json`, `frontend/vite.config.ts`, and `frontend/tsconfig.json`
- [X] T005 [P] Create frontend app shell entry files in `frontend/src/main.tsx`, `frontend/src/app/App.tsx`, and `frontend/src/app/App.css`
- [X] T006 [P] Create shared test setup files in `frontend/src/test/setup.ts` and `backend/src/test/resources/application-test.yml`
- [X] T007 [P] Document local setup commands and required environment variables in `README.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish persistence, contracts, validation, errors, logging, and API/client boundaries required by all user stories.

**CRITICAL**: No user story work can begin until this phase is complete.

- [X] T008 Create Liquibase master changelog in `backend/src/main/resources/db/changelog/db.changelog-master.yaml`
- [X] T009 Create initial seats and bookings schema migration with a partial unique active-booking index in `backend/src/main/resources/db/changelog/changes/001-create-seat-booking-schema.yaml`
- [X] T010 [P] Create backend package structure placeholders in `backend/src/main/java/com/vits/booking/seat`, `backend/src/main/java/com/vits/booking/booking`, `backend/src/main/java/com/vits/booking/customer`, and `backend/src/main/java/com/vits/booking/common`
- [X] T011 [P] Create common API error DTOs in `backend/src/main/java/com/vits/booking/common/api/ErrorResponse.java` and `backend/src/main/java/com/vits/booking/common/api/ValidationErrorResponse.java`
- [X] T012 Create global exception handling for validation, not-found, conflict, and service failures in `backend/src/main/java/com/vits/booking/common/api/GlobalExceptionHandler.java`
- [X] T013 [P] Create domain exception classes in `backend/src/main/java/com/vits/booking/common/domain/ResourceNotFoundException.java`, `backend/src/main/java/com/vits/booking/common/domain/BookingConflictException.java`, and `backend/src/main/java/com/vits/booking/common/domain/BookingServiceUnavailableException.java`
- [X] T014 [P] Create frontend API error and domain types in `frontend/src/features/booking/types.ts`
- [X] T015 [P] Create frontend API client foundation in `frontend/src/features/booking/api/httpClient.ts`
- [X] T016 [P] Add backend test utilities for MVC and repository tests in `backend/src/test/java/com/vits/booking/support`
- [X] T017 [P] Add frontend test utilities for rendering booking components in `frontend/src/test/render.tsx`

**Checkpoint**: Foundation ready. User-story implementation can now begin.

---

## Phase 3: User Story 1 - View Seat Availability (Priority: P1) MVP

**Goal**: Users can open the booking page and clearly distinguish available and occupied seats.

**Independent Test**: Seed available and occupied seats, open the booking page, and verify every seat displays the correct status, occupied seats are disabled, and loading/empty/failure states are visible when applicable.

### Tests for User Story 1

- [X] T018 [P] [US1] Add MVC contract tests for `GET /api/seats` success and 503 responses in `backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java`
- [X] T019 [P] [US1] Add repository test for deriving occupied seats from active bookings in `backend/src/test/java/com/vits/booking/seat/SeatRepositoryTest.java`
- [X] T020 [P] [US1] Add React API hook tests for loading, empty, success, and service failure states in `frontend/src/features/booking/hooks/useSeats.test.tsx`
- [X] T021 [P] [US1] Add seat grid component tests for available/occupied labels and disabled occupied seats in `frontend/src/features/booking/components/SeatGrid.test.tsx`
- [X] T022 [P] [US1] Add booking page integration test for rendering availability from the API in `frontend/src/features/booking/components/BookingPage.test.tsx`

### Implementation for User Story 1

- [X] T023 [P] [US1] Create seat status enum and JPA entity in `backend/src/main/java/com/vits/booking/seat/SeatStatus.java` and `backend/src/main/java/com/vits/booking/seat/SeatEntity.java`
- [X] T024 [P] [US1] Create booking status enum and booking entity needed for availability joins in `backend/src/main/java/com/vits/booking/booking/BookingStatus.java` and `backend/src/main/java/com/vits/booking/booking/BookingEntity.java`
- [X] T025 [US1] Create seat repository query for availability projection in `backend/src/main/java/com/vits/booking/seat/SeatRepository.java`
- [X] T026 [P] [US1] Create seat response DTO and mapper in `backend/src/main/java/com/vits/booking/seat/SeatResponse.java` and `backend/src/main/java/com/vits/booking/seat/SeatMapper.java`
- [X] T027 [US1] Implement seat availability service in `backend/src/main/java/com/vits/booking/seat/SeatService.java`
- [X] T028 [US1] Implement `GET /api/seats` controller in `backend/src/main/java/com/vits/booking/seat/SeatController.java`
- [X] T029 [P] [US1] Implement frontend `listSeats` API call in `frontend/src/features/booking/api/bookingApi.ts`
- [X] T030 [US1] Implement `useSeats` hook with refresh, loading, empty, and failure states in `frontend/src/features/booking/hooks/useSeats.ts`
- [X] T031 [P] [US1] Implement accessible seat grid rendering in `frontend/src/features/booking/components/SeatGrid.tsx`
- [X] T032 [US1] Implement booking page availability view in `frontend/src/features/booking/components/BookingPage.tsx`
- [X] T033 [US1] Wire booking page into the app shell in `frontend/src/app/App.tsx`

**Checkpoint**: User Story 1 is fully functional and testable independently.

---

## Phase 4: User Story 2 - Book an Available Seat (Priority: P2)

**Goal**: Users can select one available seat, submit a booking request, receive confirmation, and see the booked seat become occupied.

**Independent Test**: Select an available seat, submit a valid `customerId`, verify a persistent booking is created, and confirm refreshed availability marks the seat as occupied.

### Tests for User Story 2

- [X] T034 [P] [US2] Add service unit tests for successful booking creation and seat status refresh behavior in `backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java`
- [X] T035 [P] [US2] Add MVC contract tests for `POST /api/bookings` 201 response shape in `backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java`
- [X] T036 [P] [US2] Add repository integration test for persisting active bookings in `backend/src/test/java/com/vits/booking/booking/BookingRepositoryTest.java`
- [X] T037 [P] [US2] Add React booking form tests for selecting a free seat, submitting, and showing success in `frontend/src/features/booking/components/BookingForm.test.tsx`
- [X] T038 [P] [US2] Add booking API hook tests for success and refresh after booking in `frontend/src/features/booking/hooks/useCreateBooking.test.tsx`

### Implementation for User Story 2

- [X] T039 [P] [US2] Create booking request and response DTOs with Bean Validation in `backend/src/main/java/com/vits/booking/booking/CreateBookingRequest.java` and `backend/src/main/java/com/vits/booking/booking/BookingResponse.java`
- [X] T040 [P] [US2] Create booking repository in `backend/src/main/java/com/vits/booking/booking/BookingRepository.java`
- [X] T041 [US2] Implement transactional booking creation rules in `backend/src/main/java/com/vits/booking/booking/BookingService.java`
- [X] T042 [US2] Implement `POST /api/bookings` controller in `backend/src/main/java/com/vits/booking/booking/BookingController.java`
- [X] T043 [P] [US2] Implement frontend `createBooking` API call in `frontend/src/features/booking/api/bookingApi.ts`
- [X] T044 [US2] Implement `useCreateBooking` hook with pending, success, and refresh callbacks in `frontend/src/features/booking/hooks/useCreateBooking.ts`
- [X] T045 [US2] Implement accessible booking form with customer identifier validation in `frontend/src/features/booking/components/BookingForm.tsx`
- [X] T046 [US2] Integrate seat selection, booking submission, success feedback, and availability refresh in `frontend/src/features/booking/components/BookingPage.tsx`

**Checkpoint**: User Stories 1 and 2 both work and can be validated independently.

---

## Phase 5: User Story 3 - Handle Booking Conflicts and Failures (Priority: P3)

**Goal**: Users receive clear feedback when booking fails because a seat is occupied, input is invalid, or the service is temporarily unavailable.

**Independent Test**: Attempt to book a seat that became occupied after page load, submit invalid booking data, and simulate service failure; verify no duplicate booking is created, messages are actionable, and availability refreshes after conflict.

### Tests for User Story 3

- [X] T047 [P] [US3] Add backend conflict regression tests for duplicate active booking prevention in `backend/src/test/java/com/vits/booking/booking/BookingConflictTest.java`
- [X] T048 [P] [US3] Add MVC tests for 400, 404, 409, and 503 booking error payloads in `backend/src/test/java/com/vits/booking/booking/BookingControllerErrorTest.java`
- [X] T049 [P] [US3] Add repository integration test for PostgreSQL-compatible unique active booking enforcement in `backend/src/test/java/com/vits/booking/booking/ActiveBookingUniquenessTest.java`
- [X] T050 [P] [US3] Add React interaction tests for conflict, validation, and service failure messages in `frontend/src/features/booking/components/BookingPageErrors.test.tsx`
- [X] T051 [P] [US3] Add API client error parsing tests in `frontend/src/features/booking/api/bookingApi.test.ts`

### Implementation for User Story 3

- [X] T052 [US3] Harden booking service conflict handling for occupied, missing, and concurrently booked seats in `backend/src/main/java/com/vits/booking/booking/BookingService.java`
- [X] T053 [US3] Map database uniqueness violations to `SEAT_UNAVAILABLE` conflict responses in `backend/src/main/java/com/vits/booking/common/api/GlobalExceptionHandler.java`
- [X] T054 [US3] Add structured backend logging for booking conflicts and temporary failures without customer personal data in `backend/src/main/java/com/vits/booking/booking/BookingService.java`
- [X] T055 [US3] Implement typed frontend error parsing for validation, not-found, conflict, and service failure responses in `frontend/src/features/booking/api/httpClient.ts`
- [X] T056 [US3] Add user-friendly error presentation and conflict-triggered availability refresh in `frontend/src/features/booking/components/BookingPage.tsx`
- [X] T057 [US3] Disable invalid or occupied booking actions and preserve keyboard-accessible recovery flows in `frontend/src/features/booking/components/BookingForm.tsx`

**Checkpoint**: All user stories are independently functional and conflict/failure behavior is covered.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final quality gates, documentation, contract verification, and cleanup across the full feature.

- [X] T058 [P] Verify `specs/001-seat-booking/contracts/openapi.yaml` matches implemented request, response, status, and error shapes
- [X] T059 [P] Update quickstart verification notes in `specs/001-seat-booking/quickstart.md`
- [X] T060 Run frontend tests and static checks from `frontend/package.json`
- [X] T061 Run backend tests and Gradle checks from `backend/build.gradle`
- [X] T062 Validate Liquibase migrations against a PostgreSQL-compatible database using `backend/src/main/resources/db/changelog/db.changelog-master.yaml`
- [X] T063 [P] Review frontend responsive layout and keyboard accessibility in `frontend/src/features/booking/components/BookingPage.tsx`
- [X] T064 [P] Review backend configuration for environment-driven database settings and absence of committed secrets in `backend/src/main/resources/application.yml`
- [X] T065 Run the manual feature check from `specs/001-seat-booking/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion; blocks all user stories.
- **User Story 1 (Phase 3)**: Depends on Foundational completion; MVP scope.
- **User Story 2 (Phase 4)**: Depends on Foundational completion and benefits from US1 availability view integration.
- **User Story 3 (Phase 5)**: Depends on Foundational completion and exercises failure paths around US1/US2 behavior.
- **Polish (Phase 6)**: Depends on all desired user stories being complete.

### User Story Dependencies

- **US1 - View Seat Availability**: Can start after Phase 2; no dependency on other stories.
- **US2 - Book an Available Seat**: Can start after Phase 2; independently testable through API and form, with UI integration into US1 page.
- **US3 - Handle Booking Conflicts and Failures**: Can start after Phase 2; independently testable through backend error cases and frontend error states.

### Within Each User Story

- Write tests first and confirm they fail before implementation.
- Backend entities and repositories come before services.
- Services come before controllers.
- Frontend API clients and hooks come before components that consume them.
- Complete each story checkpoint before relying on it in the next priority story.

### Parallel Opportunities

- Setup tasks T003, T005, T006, and T007 can run in parallel after project roots are created.
- Foundational tasks T010, T011, T013, T014, T015, T016, and T017 can run in parallel after T008-T009 establish persistence direction.
- Tests within each story are marked [P] and can be authored in parallel.
- Backend DTO/component tasks marked [P] can proceed alongside frontend API/component tasks in the same story.
- After Phase 2, different developers can work on US1, US2, and US3 in parallel if they coordinate edits to shared files listed in each phase.

---

## Parallel Example: User Story 1

```text
Task: "T018 [P] [US1] Add MVC contract tests for GET /api/seats in backend/src/test/java/com/vits/booking/seat/SeatControllerTest.java"
Task: "T020 [P] [US1] Add React API hook tests in frontend/src/features/booking/hooks/useSeats.test.tsx"
Task: "T021 [P] [US1] Add seat grid component tests in frontend/src/features/booking/components/SeatGrid.test.tsx"
Task: "T023 [P] [US1] Create seat status enum and JPA entity in backend/src/main/java/com/vits/booking/seat/SeatStatus.java and backend/src/main/java/com/vits/booking/seat/SeatEntity.java"
Task: "T029 [P] [US1] Implement frontend listSeats API call in frontend/src/features/booking/api/bookingApi.ts"
```

## Parallel Example: User Story 2

```text
Task: "T034 [P] [US2] Add service unit tests in backend/src/test/java/com/vits/booking/booking/BookingServiceTest.java"
Task: "T035 [P] [US2] Add MVC contract tests in backend/src/test/java/com/vits/booking/booking/BookingControllerTest.java"
Task: "T037 [P] [US2] Add booking form tests in frontend/src/features/booking/components/BookingForm.test.tsx"
Task: "T039 [P] [US2] Create booking request and response DTOs in backend/src/main/java/com/vits/booking/booking/CreateBookingRequest.java and backend/src/main/java/com/vits/booking/booking/BookingResponse.java"
Task: "T043 [P] [US2] Implement frontend createBooking API call in frontend/src/features/booking/api/bookingApi.ts"
```

## Parallel Example: User Story 3

```text
Task: "T047 [P] [US3] Add backend conflict regression tests in backend/src/test/java/com/vits/booking/booking/BookingConflictTest.java"
Task: "T048 [P] [US3] Add MVC error tests in backend/src/test/java/com/vits/booking/booking/BookingControllerErrorTest.java"
Task: "T050 [P] [US3] Add React error interaction tests in frontend/src/features/booking/components/BookingPageErrors.test.tsx"
Task: "T051 [P] [US3] Add API client error parsing tests in frontend/src/features/booking/api/bookingApi.test.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 setup.
2. Complete Phase 2 foundational infrastructure.
3. Complete Phase 3 User Story 1.
4. Stop and validate the independent availability view test criteria.
5. Demo the booking page showing available, occupied, loading, empty, and failure states.

### Incremental Delivery

1. Deliver Setup + Foundation.
2. Deliver US1 availability view and validate independently.
3. Deliver US2 booking creation and validate independently.
4. Deliver US3 conflict/failure handling and validate independently.
5. Run Phase 6 quality gates before release.

### Parallel Team Strategy

1. Complete Setup and Foundational tasks together.
2. Split by story after Phase 2: one developer on US1, one on US2, one on US3.
3. Coordinate shared files: `frontend/src/features/booking/api/bookingApi.ts`, `frontend/src/features/booking/components/BookingPage.tsx`, `backend/src/main/java/com/vits/booking/booking/BookingService.java`, and `backend/src/main/java/com/vits/booking/common/api/GlobalExceptionHandler.java`.
4. Merge by priority order and validate each checkpoint.

---

## Notes

- [P] tasks are intentionally limited to different files or files safe to edit independently.
- Story labels map directly to the three user stories in `specs/001-seat-booking/spec.md`.
- Keep contract, tests, and implementation synchronized for every API behavior change.
- Commit after each task or logical group when the optional git hook workflow is used.

