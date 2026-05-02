import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { renderBooking } from '../../../test/render';
import { BookingPage } from './BookingPage';

describe('BookingPage error flows', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('shows service failures while loading seats', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(errorJson('SERVICE_UNAVAILABLE', 'Booking service is temporarily unavailable. Try again later.', 503)));

    renderBooking(<BookingPage />);

    expect(await screen.findByRole('alert')).toHaveTextContent('Booking service is temporarily unavailable');
  });

  it('shows conflicts and refreshes availability', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn()
        .mockResolvedValueOnce(json([{ id: '018f6ff5-9055-7c82-b0de-83cfd0bd9901', label: 'A1', status: 'AVAILABLE' }]))
        .mockResolvedValueOnce(errorJson('SEAT_UNAVAILABLE', 'Selected seat is no longer available.', 409))
        .mockResolvedValueOnce(json([{ id: '018f6ff5-9055-7c82-b0de-83cfd0bd9901', label: 'A1', status: 'OCCUPIED' }]))
    );

    renderBooking(<BookingPage />);

    await userEvent.click(await screen.findByRole('button', { name: 'A1, available' }));
    await userEvent.type(screen.getByLabelText('Customer ID'), '018f6ff5-9055-7c82-b0de-83cfd0bd9910');
    await userEvent.click(screen.getByRole('button', { name: 'Confirm booking' }));

    expect(await screen.findByRole('alert')).toHaveTextContent('Selected seat is no longer available.');
    expect(await screen.findByRole('button', { name: 'A1, occupied' })).toBeDisabled();
  });
});

function json(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' }
  });
}

function errorJson(code: string, message: string, status: number) {
  return json({ code, message }, status);
}
