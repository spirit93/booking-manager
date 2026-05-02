# Data Model: Day-Based Seat Booking

## Seat

Represents a bookable place shown on the booking page.

**Fields**

- `id` (UUID): Stable seat identifier.
- `label` (string): Human-readable label, for example `A1`.
- `status` (SeatStatus, response-only): Availability for the requested day, either `AVAILABLE` or `OCCUPIED`.

**Relationships**

- One seat can have many bookings across different days.
- One seat can have at most one active booking for a given booked day.

**Validation Rules**

- `id` must reference an existing seat when creating a booking.
- `label` must be present and unique enough for users to distinguish seats.

## Booking

Represents a confirmed reservation for one seat, one customer, and one calendar day.

**Fields**

- `id` (UUID): Stable booking identifier.
- `seatId` (UUID): Seat being reserved.
- `customerId` (UUID): Customer or user associated with the booking.
- `bookedDay` (date): Calendar day reserved, formatted as `YYYY-MM-DD`.
- `status` (BookingStatus): Booking lifecycle status. Initial scope uses `ACTIVE`.
- `createdAt` (instant): Timestamp when the booking was created.

**Relationships**

- Each booking belongs to one seat.
- Each booking belongs to one customer/user identity.

**Validation Rules**

- `seatId` is required and must exist.
- `customerId` is required.
- `bookedDay` is required, must parse as an ISO calendar date, and must be inside the supported booking range.
- Only one active booking may exist for the same `seatId` and `bookedDay`.

**State Transitions**

```text
Create request -> ACTIVE
```

Cancellation, expiry, and non-active states are outside this feature's scope. If future statuses are added, the active uniqueness rule must apply only to statuses that reserve inventory.

## Booking Day

Represents the calendar date used to evaluate availability and create bookings.

**Fields**

- `day` / `bookedDay` (date): ISO `YYYY-MM-DD` value.

**Validation Rules**

- Must be present for availability queries and booking requests.
- Must be a valid calendar date.
- Must be inside the supported booking range.
- Defaults to the user's current local date on first frontend page load.

## User Or Customer

Represents the person associated with a booking.

**Fields**

- `customerId` (UUID): Identifier sent with booking requests.

**Relationships**

- One customer/user can have many bookings.

**Validation Rules**

- `customerId` must be present when creating a booking.
- This feature does not add customer profile management.
