import { afterEach, describe, expect, it, vi } from 'vitest';
import { createBooking, listSeats } from './bookingApi';
import { BookingApiError } from '../types';

describe('bookingApi', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('loads seats from the REST contract', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(response([{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }])));

    await expect(listSeats()).resolves.toEqual([{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }]);
    expect(fetch).toHaveBeenCalledWith('/api/seats', expect.any(Object));
  });

  it('parses validation error payloads', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue(
        response(
          {
            code: 'VALIDATION_ERROR',
            message: 'Request contains invalid fields.',
            fieldErrors: [{ field: 'customerId', message: 'Customer identifier is required.' }]
          },
          400
        )
      )
    );

    await expect(createBooking({ seatId: 'seat-1', customerId: '' })).rejects.toMatchObject({
      status: 400,
      code: 'VALIDATION_ERROR',
      fieldErrors: [{ field: 'customerId', message: 'Customer identifier is required.' }]
    } satisfies Partial<BookingApiError>);
  });

  it('normalizes network failures as service unavailable', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('offline')));

    await expect(listSeats()).rejects.toMatchObject({
      status: 503,
      code: 'SERVICE_UNAVAILABLE'
    });
  });
});

function response(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' }
  });
}
