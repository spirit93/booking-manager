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

## Frontend

From `frontend/`:

```powershell
npm install
npm test
npm run dev
```

The Vite dev server proxies `/api` to `http://localhost:8080`.
