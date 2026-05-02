# booking-manager

Seat booking service with a React/Vite frontend and a Spring Boot backend.

## Requirements

- Java 21
- Node.js LTS
- PostgreSQL for local backend runtime

## Backend

Configuration is environment-driven:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/booking_manager"
$env:SPRING_DATASOURCE_USERNAME="booking_manager"
$env:SPRING_DATASOURCE_PASSWORD="booking_manager"
```

From `backend/`:

```powershell
.\gradlew test
.\gradlew bootRun
```

Liquibase applies `backend/src/main/resources/db/changelog/db.changelog-master.yaml` on startup.

Seat availability is day-scoped:

```powershell
Invoke-RestMethod "http://localhost:8080/api/seats/availability?day=2026-05-02"
```

Bookings require a `bookedDay` value and allow the same seat on different days while rejecting duplicate active bookings for the same seat and day:

```powershell
Invoke-RestMethod "http://localhost:8080/api/bookings" -Method Post -ContentType "application/json" -Body '{"seatId":"018f6ff5-9055-7c82-b0de-83cfd0bd9901","customerId":"018f6ff5-9055-7c82-b0de-83cfd0bd9910","bookedDay":"2026-05-02"}'
```

## Frontend

From `frontend/`:

```powershell
npm install
npm test
npm run build
npm run dev
```

The Vite dev server proxies `/api` to `http://localhost:8080`.
