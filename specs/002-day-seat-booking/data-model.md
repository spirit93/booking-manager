# Data Model: Day-Based Seat Booking

## Seat

Represents a predefined bookable place displayed in the seat map.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | UUID or Long | Yes | Stable backend identifier |
| `label` | String | Yes | User-visible seat number or label, unique |
| `rowLabel` | String | No | Optional display grouping |
| `position` | Integer | No | Optional deterministic ordering within the map |
| `enabled` | Boolean | Yes | Disabled seats are not bookable |

Validation rules:

- `label` must be unique and non-blank.
- Only enabled seats can be booked.
- Seat identifiers in booking requests must refer to an existing enabled seat.

## Booking

Represents a reservation connecting one seat to one full calendar day.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | UUID or Long | Yes | Stable booking identifier |
| `seatId` | UUID or Long | Yes | References `Seat` |
| `bookingDay` | LocalDate | Yes | ISO calendar date being reserved |
| `status` | Enum | Yes | Initial feature uses `ACTIVE` |
| `createdAt` | Instant | Yes | Server creation timestamp |

Validation rules:

- `bookingDay` must be a valid ISO calendar date.
- `seatId` must be present and point to an enabled seat.
- At most one `ACTIVE` booking may exist for the same `seatId` and `bookingDay`.

State transitions:

```text
requested -> ACTIVE
```

Cancellation/editing are outside this feature, so no additional user-driven transitions are planned.

## Booking Day

Represents the date selected by a visitor for viewing availability and creating a booking.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `day` | LocalDate | Yes | Passed as query parameter or request field |

Validation rules:

- Must be provided before availability is loaded or booking is confirmed.
- Must parse as `YYYY-MM-DD`.

## Read Models

### Seat Availability

Returned to the frontend for a selected booking day.

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `day` | LocalDate | Yes | Date used for availability calculation |
| `seats` | SeatAvailabilityItem[] | Yes | All displayable seats |

### Seat Availability Item

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `seatId` | UUID or Long | Yes | Seat identifier |
| `label` | String | Yes | Display label |
| `status` | Enum | Yes | `AVAILABLE` or `OCCUPIED` |
| `bookingId` | UUID or Long | No | Present only when occupied, if safe to expose |

Relationships:

- A `Seat` has many `Booking` records over time.
- A `Booking` belongs to exactly one `Seat`.
- A `Booking Day` scopes availability across all seats.
