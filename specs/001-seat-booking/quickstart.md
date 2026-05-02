# Quickstart: Seat Booking

## Prerequisites

- Java 21
- Gradle wrapper generated for the backend
- Node.js LTS for the React frontend
- PostgreSQL available locally or through Docker

## Backend

1. Configure PostgreSQL connection values through environment variables:

   ```powershell
   $env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/booking_manager"
   $env:SPRING_DATASOURCE_USERNAME="booking_manager"
   $env:SPRING_DATASOURCE_PASSWORD="booking_manager"
   ```

2. Run backend tests:

   ```powershell
   .\gradlew test
   ```

3. Start the backend from `backend/`:

   ```powershell
   .\gradlew bootRun
   ```

4. Confirm the API contract is available through implementation tests and matches `specs/001-seat-booking/contracts/openapi.yaml`.

## Frontend

1. Install dependencies from `frontend/`:

   ```powershell
   npm install
   ```

2. Run frontend tests:

   ```powershell
   npm test
   ```

3. Start the frontend:

   ```powershell
   npm run dev
   ```

4. Open the Vite URL and verify the booking page shows loading, empty, available, occupied, success, conflict, validation, and service failure states.

## Verification Notes

- `GET /api/seats` returns `id`, `label`, and `status` values matching `contracts/openapi.yaml`.
- `POST /api/bookings` returns `201 Created` with `id`, `seatId`, `customerId`, `status`, and `createdAt`.
- Validation, missing-seat, conflict, and temporary-failure responses use the shared `code` and `message` error format.
- The frontend refreshes availability after successful bookings and conflict responses.

## Manual Feature Check

1. Seed or create seats in PostgreSQL.
2. Open the booking page and verify all seats show either available or occupied status.
3. Select an available seat and submit a booking with a valid `customerId`.
4. Verify the API creates a booking and the selected seat becomes occupied.
5. Attempt to book the same seat again and verify the API returns a conflict response and the frontend refreshes availability.
