# TinySpec: Email-Based Booking

**Branch**: feature/book-via-email
**Date**: 2026-05-02
**Status**: done
**Complexity**: medium

## What

Bookings should be created with a customer email instead of a customer UUID. This makes the booking flow usable by customers identified through email while preserving the existing seat/day conflict rules.

## Scope Note

This crosses frontend, backend, contract, persistence, and tests. If implementation expands beyond a field replacement and migration, upgrade to the full Spec Kit workflow.

## Context

| File | Role |
|------|------|
| `frontend/src/features/booking/components/BookingForm.tsx` | Will be modified - collect and validate customer email |
| `frontend/src/features/booking/components/BookingPage.tsx` | Will be modified - submit/display email instead of UUID |
| `frontend/src/features/booking/types.ts` | Will be modified - replace `customerId` with `customerEmail` in booking types |
| `backend/src/main/java/com/vits/booking/booking/CreateBookingRequest.java` | Will be modified - accept validated email |
| `backend/src/main/java/com/vits/booking/booking/BookingEntity.java` | Will be modified - persist customer email |
| `backend/src/main/java/com/vits/booking/booking/BookingResponse.java` | Will be modified - return customer email |
| `backend/src/main/resources/db/changelog/changes/003-use-customer-email-for-bookings.yaml` | Will be added - migrate booking customer field |
| `specs/003-day-seat-booking/contracts/openapi.yaml` | Will be modified - document email request/response field |

## Requirements

1. Booking creation must require `customerEmail` and reject missing or invalid email values with validation errors.
2. The frontend form must ask for email, validate it deterministically, and send `customerEmail` with `seatId` and `bookedDay`.
3. Booking responses and success messages must show the customer email, not a UUID.
4. Existing day-specific seat availability and same-day conflict behavior must remain unchanged.
5. Database changes must be represented by Liquibase and keep `customerEmail` non-null for new bookings.

## Plan

1. Replace customer UUID request/response fields with email fields in frontend types, form state, submit flow, and tests.
2. Replace `customerId` with `customerEmail` in backend DTO/entity/response mapping and related tests.
3. Add a Liquibase migration for the bookings customer email column, with safe handling for existing local data.
4. Update OpenAPI contract examples/schema and run frontend/backend tests.

## Tasks

- [x] Update booking form label, input type, validation, placeholder, and tests for email.
- [x] Update frontend booking types, API tests, hook/page tests, and success copy for `customerEmail`.
- [x] Update backend request validation with `@Email` and non-blank checks.
- [x] Update booking entity, service construction, response mapping, and backend tests.
- [x] Add Liquibase migration replacing or adding the persisted customer email column.
- [x] Update OpenAPI booking request/response schemas.
- [x] Run frontend tests.
- [x] Run backend tests.

## Done When

- [x] All tasks checked off
- [x] Frontend and backend tests pass
- [x] OpenAPI contract and validation behavior match implementation
