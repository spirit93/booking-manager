# TinySpec: Booked Seat Red Highlight

**Branch**: main
**Date**: 2026-05-02
**Status**: done
**Complexity**: small

## What

Booked seats should be visually distinct with a subtle red highlight so users can quickly scan the grid and avoid trying to select unavailable seats. This changes only the frontend presentation of seats already reported as `OCCUPIED`.

## Context

| File | Role |
|------|------|
| `frontend/src/app/App.css` | Will be modified - update occupied seat colors to a soft red treatment |
| `frontend/src/features/booking/components/SeatGrid.tsx` | Context - renders seat buttons with `data-status` and disabled occupied seats |
| `frontend/src/features/booking/components/SeatGrid.test.tsx` | Will be modified - verify occupied seats expose the occupied status for styling |

## Requirements

1. Seats with status `OCCUPIED` must render with a slightly red visual treatment.
2. Occupied seats must remain disabled and announced as occupied.
3. Available and selected seat styling must remain unchanged.
4. The change must not require API, contract, or backend changes.

## Plan

1. Update `.seat-button[data-status="OCCUPIED"]` in `frontend/src/app/App.css` to use a subtle red background, border, and text color.
2. Keep `SeatGrid.tsx` behavior unchanged unless a test requires a more explicit styling hook.
3. Add or adjust a React Testing Library assertion that occupied seats retain `data-status="OCCUPIED"` and disabled behavior.
4. Run frontend tests for the seat grid or the full frontend test suite.

## Tasks

- [x] Change occupied seat CSS to a soft red highlight.
- [x] Preserve existing disabled and accessible occupied state behavior.
- [x] Update `SeatGrid.test.tsx` to cover the styling hook/state.
- [x] Run frontend tests.

## Done When

- [x] All tasks checked off
- [x] Tests pass
- [x] No lint errors or documented reason if lint is unavailable
