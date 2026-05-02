# Quickstart: Day-Based Seat Booking

## Prerequisites

- Java 21 available for the backend.
- Node.js and npm available for the frontend.
- PostgreSQL connection configured through environment or local application configuration.

## Run Backend Tests

```powershell
cd backend
./gradlew test
```

Expected coverage includes:

- Booking service tests for same-seat same-day conflicts.
- Booking service tests allowing the same seat on different days.
- MVC tests for missing, malformed, and out-of-range day validation.
- Repository/integration tests for active booking uniqueness by seat/day.

## Run Frontend Tests

```powershell
cd frontend
npm test
npm run build
```

Expected coverage includes:

- Day selector default and change behavior.
- Availability reloads for the selected day.
- Booking request payload includes the selected day and customer email.
- Success, validation, conflict, loading, empty, and temporary failure states.

## Manual Verification

1. Start the backend.
2. Start the frontend.
3. Open the booking page.
4. Confirm today's date is visible as the default selected day.
5. Book seat `A1` for `2026-05-02`.
6. Verify `A1` is occupied for `2026-05-02`.
7. Change the selected day to `2026-05-03`.
8. Verify `A1` is available for `2026-05-03`.
9. Attempt a duplicate booking for `A1` on `2026-05-02`.
10. Verify the UI shows a conflict message that names the day and refreshes availability for `2026-05-02`.

## API Smoke Checks

```powershell
Invoke-RestMethod "http://localhost:8080/api/seats/availability?day=2026-05-02"
Invoke-RestMethod "http://localhost:8080/api/bookings" -Method Post -ContentType "application/json" -Body '{"seatId":"018f6ff5-9055-7c82-b0de-83cfd0bd9901","customerEmail":"customer@example.com","bookedDay":"2026-05-02"}'
Invoke-RestMethod "http://localhost:8080/api/seats/availability?day=2026-05-03"
```

The sample `seatId` values are from the default Liquibase seed data. Replace them if your local database uses different seed rows.
