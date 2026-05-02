# Quickstart: Day-Based Seat Booking

## Prerequisites

- JDK 21 available to Gradle.
- Node.js and npm available for the frontend.
- PostgreSQL available locally. The repository includes `compose.yaml` for a local Docker-backed database.

## Database

Start local PostgreSQL from the repository root:

```powershell
docker compose up -d postgres
```

Default backend database settings:

```text
BOOKING_DB_URL=jdbc:postgresql://localhost:5432/booking_manager
BOOKING_DB_USERNAME=booking_manager
BOOKING_DB_PASSWORD=booking_manager
```

Liquibase creates and updates the runtime schema when the backend starts. Automated tests use in-memory H2 in PostgreSQL compatibility mode.

## Backend

From the repository root:

```powershell
cd backend
.\gradlew test
.\gradlew bootRun
```

Expected local API base URL:

```text
http://localhost:8080
```

Useful checks:

```powershell
Invoke-RestMethod "http://localhost:8080/api/seats/availability?day=2026-05-02"
Invoke-RestMethod "http://localhost:8080/api/bookings" -Method Post -ContentType "application/json" -Body '{"seatId":"A1","day":"2026-05-02"}'
```

## Frontend

From the repository root:

```powershell
cd frontend
npm test
npm run dev
```

Expected local frontend URL:

```text
http://localhost:5173
```

## Manual Acceptance Flow

1. Open the frontend.
2. Select `2026-05-02`.
3. Confirm that the seat map shows availability for that day.
4. Book an available seat.
5. Confirm that the seat becomes occupied for `2026-05-02`.
6. Switch to `2026-05-03` and confirm the same seat is available unless separately booked.
7. Try booking the occupied seat again for `2026-05-02` and confirm the UI shows a clear conflict message.

## Required Quality Gates

Before review:

```powershell
cd backend
.\gradlew test
cd ..\frontend
npm test
```

Backend tests must cover service rules, controller validation, repository persistence, and duplicate active booking prevention. Frontend tests must cover date selection, loading and failure states, successful booking refresh, occupied-seat blocking, and accessible form/seat controls.

## Verification Notes

Validated on 2026-05-02:

- Backend: Gradle `test` passed from `backend/` using Java 21 and the local Gradle 9.4.0 distribution.
- Frontend: `npm test` passed from `frontend/`.
- Frontend build: `npm run build` passed after allowing Vite/esbuild to spawn its build helper outside the sandbox.
- Manual API acceptance: attempted by starting `bootRun` and probing `http://localhost:8080`, but the local PowerShell background job timed out before returning usable output. No backend server was responding on port 8080 after the timeout. The automated backend persistence/controller tests and frontend interaction tests cover the same `2026-05-02`/`2026-05-03` booking-day behavior.

Updated database target after implementation:

- Backend runtime now requires PostgreSQL instead of H2.
- Backend tests intentionally remain on in-memory H2 for fast, isolated automated runs.
- Use `docker compose up -d postgres` before running `bootRun` locally when PostgreSQL is not already available.
