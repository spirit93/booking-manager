import { afterEach, describe, expect, it, vi } from 'vitest';
import { createBooking, listSeatAvailability } from './bookingApi';
import { BookingApiError } from '../types';

describe('bookingApi', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('loads day-specific seat availability from the REST contract', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(response({ day: '2026-05-02', seats: [{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }] })));

    await expect(listSeatAvailability('2026-05-02')).resolves.toEqual({ day: '2026-05-02', seats: [{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }] });
    expect(fetch).toHaveBeenCalledWith('/api/seats/availability?day=2026-05-02', expect.any(Object));
  });

  it('parses validation error payloads', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        response(
          {
            code: 'VALIDATION_ERROR',
            message: 'Request contains invalid fields.',
            fieldErrors: [{ field: 'customerEmail', message: 'Customer email is required.' }]
          },
          400
        )
      )
    );

    await expect(createBooking({ seatId: 'seat-1', customerEmail: '', bookedDay: '2026-05-02' })).rejects.toMatchObject({
      status: 400,
      code: 'VALIDATION_ERROR',
      fieldErrors: [{ field: 'customerEmail', message: 'Customer email is required.' }]
    } satisfies Partial<BookingApiError>);
  });

  it('normalizes network failures as service unavailable', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('offline')));

    await expect(listSeatAvailability('2026-05-02')).rejects.toMatchObject({
      status: 503,
      code: 'SERVICE_UNAVAILABLE'
    });
  });

  it('sends bookedDay when creating a booking', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        response({
          id: 'booking-1',
          seatId: 'seat-1',
          customerEmail: 'customer@example.com',
          bookedDay: '2026-05-02',
          status: 'ACTIVE',
          createdAt: '2026-05-02T00:00:00Z'
        }, 201)
      )
    );

    await createBooking({ seatId: 'seat-1', customerEmail: 'customer@example.com', bookedDay: '2026-05-02' });

    expect(fetch).toHaveBeenCalledWith('/api/bookings', expect.objectContaining({
      body: JSON.stringify({ seatId: 'seat-1', customerEmail: 'customer@example.com', bookedDay: '2026-05-02' })
    }));
  });
});

function response(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' }
  });
}
