import { act, renderHook, waitFor } from '@testing-library/react';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { useCreateBooking } from './useCreateBooking';

describe('useCreateBooking', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('creates a booking and invokes refresh callback', async () => {
    const refresh = vi.fn();
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        json({
          id: 'booking-1',
          seatId: 'seat-1',
          customerEmail: 'customer@example.com',
          bookedDay: '2026-05-02',
          status: 'ACTIVE',
          createdAt: '2026-05-02T00:00:00Z'
        }, 201)
      )
    );

    const { result } = renderHook(() => useCreateBooking(refresh));

    await act(() => result.current.submit('seat-1', 'customer@example.com', '2026-05-02'));

    await waitFor(() => expect(result.current.booking?.id).toBe('booking-1'));
    expect(refresh).toHaveBeenCalled();
  });

  it('refreshes selected-day availability after a conflict', async () => {
    const refresh = vi.fn();
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        json({
          code: 'BOOKING_CONFLICT',
          message: 'Selected seat is no longer available for 2026-05-02.',
          details: { bookedDay: '2026-05-02' }
        }, 409)
      )
    );

    const { result } = renderHook(() => useCreateBooking(refresh));

    await act(() => result.current.submit('seat-1', 'customer@example.com', '2026-05-02'));

    await waitFor(() => expect(result.current.error?.message).toContain('2026-05-02'));
    expect(refresh).toHaveBeenCalled();
  });
});

function json(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' }
  });
}
