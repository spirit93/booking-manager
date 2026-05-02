# Feature Specification: Day-Based Seat Booking

**Feature Branch**: `002-day-seat-booking`  
**Created**: 2026-05-02  
**Status**: Draft  
**Input**: User description: "бронирование места должно происходить по дням"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - View Seat Availability For A Day (Priority: P1)

A visitor selects a calendar day and sees which seats are free or occupied for that specific day, so they can choose an available seat without guessing whether the seat is available on another date.

**Why this priority**: Day-specific availability is the foundation of the booking flow; without it, users cannot make a correct booking decision.

**Independent Test**: Can be fully tested by selecting two different days with different bookings and verifying that the same seat can appear occupied on one day and free on another.

**Acceptance Scenarios**:

1. **Given** bookings exist for a selected day, **When** the visitor opens the seat map for that day, **Then** occupied seats for that day are shown as unavailable and free seats are shown as available.
2. **Given** a seat is booked for one day but not another, **When** the visitor switches between those days, **Then** the seat availability updates according to the selected day.

---

### User Story 2 - Book A Seat For A Selected Day (Priority: P1)

A visitor chooses an available seat and confirms a booking for the selected day, so the seat becomes unavailable only for that day.

**Why this priority**: The core business value is allowing users to reserve seats while preserving availability on other days.

**Independent Test**: Can be fully tested by selecting a day, booking a free seat, and verifying that the seat becomes occupied on that day while remaining available on other days without a booking.

**Acceptance Scenarios**:

1. **Given** a visitor selected a day and an available seat, **When** they confirm the booking, **Then** the system records the booking for that seat and day and shows the seat as occupied for that day.
2. **Given** a seat is already occupied for the selected day, **When** a visitor attempts to book it, **Then** the booking is rejected and the visitor is informed that the seat is no longer available for that day.

---

### User Story 3 - Preserve Booking Information (Priority: P2)

An operator or returning visitor can rely on previously created bookings being retained, so day-based availability remains accurate after page refreshes or later visits.

**Why this priority**: Stored booking data prevents double-booking and makes the service useful beyond a single session.

**Independent Test**: Can be fully tested by creating a booking for a day, leaving the page, returning to that same day, and verifying that the seat is still occupied.

**Acceptance Scenarios**:

1. **Given** a booking was confirmed for a seat and day, **When** availability is viewed later for that same day, **Then** the seat is still shown as occupied.
2. **Given** bookings exist for multiple days, **When** availability is viewed for each day, **Then** only bookings belonging to the selected day affect the seat map.

### Edge Cases

- The selected day has no bookings yet; all seats with no day-specific booking are shown as available.
- A visitor changes the selected day after choosing a seat; the chosen seat is cleared unless it is available on the newly selected day and the visitor confirms it again.
- Two visitors attempt to book the same seat for the same day at nearly the same time; only one booking succeeds.
- A visitor attempts to book without selecting a day; the system requires a valid day before booking can be confirmed.
- The booking list cannot be loaded; the visitor sees a failure state and cannot confirm a booking until availability is known.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Users MUST be able to select a calendar day before viewing or creating seat bookings.
- **FR-002**: The system MUST display each seat as free or occupied based on bookings for the selected day only.
- **FR-003**: Users MUST be able to book a free seat for the selected day.
- **FR-004**: The system MUST prevent more than one active booking for the same seat on the same day.
- **FR-005**: A seat booked for one day MUST remain available on other days unless it also has a booking for those days.
- **FR-006**: The system MUST persist each booking with at least the booked seat and booking day.
- **FR-007**: The system MUST refresh or otherwise update visible availability after a booking is confirmed.
- **FR-008**: The system MUST reject booking attempts when the selected seat is already occupied for the selected day.
- **FR-009**: The system MUST show clear empty, loading, success, and failure states for day-based availability and booking actions.
- **FR-010**: The system MUST validate that a booking request includes a valid day and a valid seat.

### Quality Requirements *(mandatory when applicable)*

- **QR-001**: React UI changes MUST define visible loading, empty, success, and failure states.
- **QR-002**: React forms and user inputs MUST include accessible labels and validation behavior.
- **QR-003**: Spring Boot API changes MUST define request validation, response DTOs, and error behavior.
- **QR-004**: Contract changes MUST identify backward compatibility impact and required frontend/backend updates.
- **QR-005**: Behavior changes MUST identify required automated tests or document a reviewed exception.

### Key Entities *(include if feature involves data)*

- **Seat**: A bookable place shown to users, identified by a stable seat label or number and current display state for the selected day.
- **Booking**: A reservation connecting one seat to one calendar day, with status information needed to determine whether the booking occupies that seat for that day.
- **Booking Day**: The calendar date for which availability is viewed and bookings are made.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 95% of users can select a day, identify an available seat, and complete a booking in under 2 minutes.
- **SC-002**: After a successful booking, the selected seat is shown as occupied for that day in 100% of subsequent availability views.
- **SC-003**: Seats booked for one day remain available on days without a booking in 100% of tested day-switching scenarios.
- **SC-004**: Concurrent attempts to book the same seat for the same day result in no more than one successful booking.
- **SC-005**: 90% of users understand from the interface which day they are booking for before confirming.

## Assumptions

- A booking is for a full calendar day, not a time range within the day.
- The initial feature covers creating and viewing bookings; cancellation and editing are outside this specification unless added later.
- Users do not need to sign in for the initial booking flow unless a later requirement introduces identity or permissions.
- The system has a predefined set of seats to display.
