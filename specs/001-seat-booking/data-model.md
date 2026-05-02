# Data Model: Seat Booking

## Seat

Represents a bookable place shown to users.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | UUID | Yes | Stable seat identifier |
| `label` | String | Yes | Human-readable label such as `A1` |
| `status` | Enum | Yes | Derived as `AVAILABLE` or `OCCUPIED` for API responses |
| `createdAt` | Instant | Yes | Audit timestamp |
| `updatedAt` | Instant | Yes | Audit timestamp |

### Validation Rules

- `label` must be unique and non-blank.
- A seat is occupied when it has an active booking.
- Occupied seats cannot be selected for a new booking.

## Booking

Represents a reservation of one seat for one customer.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | UUID | Yes | Stable booking identifier |
| `seatId` | UUID | Yes | References `Seat.id` |
| `customerId` | UUID | Yes | References the surrounding application's customer/user identity |
| `status` | Enum | Yes | Initial scope uses `ACTIVE`; future cancellation/expiry can add states |
| `createdAt` | Instant | Yes | Booking creation time |
| `updatedAt` | Instant | Yes | Audit timestamp |

### Validation Rules

- `seatId` is required and must refer to an existing seat.
- `customerId` is required.
- Only one active booking may exist for a seat.
- Booking creation must be transactional.

### State Transitions

```text
Requested -> ACTIVE
Requested -> Rejected: seat is missing, occupied, invalid, or temporarily unavailable
```

Cancellation and expiry are outside the initial scope, so no transition out of `ACTIVE` is implemented in this feature.

## Customer

Represents the user or customer associated with bookings. Authentication and profile management are outside the initial scope.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | UUID | Yes | Supplied by surrounding application/session |

## Relationships

- `Seat` has zero or one active `Booking`.
- `Booking` belongs to exactly one `Seat`.
- `Booking` belongs to exactly one `Customer` reference.

## Persistence Notes

- `seats` table stores stable seat records.
- `bookings` table stores booking records.
- A PostgreSQL partial unique index on `bookings(seat_id)` where `status = 'ACTIVE'` prevents duplicate active bookings.
- Liquibase changelogs create tables, enums or constrained text fields, indexes, and initial seat seed data if required by implementation tasks.
