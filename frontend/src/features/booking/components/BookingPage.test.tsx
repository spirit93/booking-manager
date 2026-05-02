import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { renderBooking } from '../../../test/render';
import { BookingPage } from './BookingPage';

describe('BookingPage', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('renders availability from the API and books an available seat', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn()
        .mockResolvedValueOnce(json([{ id: '018f6ff5-9055-7c82-b0de-83cfd0bd9901', label: 'A1', status: 'AVAILABLE' }]))
        .mockResolvedValueOnce(json({
          id: 'booking-1',
          seatId: '018f6ff5-9055-7c82-b0de-83cfd0bd9901',
          customerId: '018f6ff5-9055-7c82-b0de-83cfd0bd9910',
          status: 'ACTIVE',
          createdAt: '2026-05-02T00:00:00Z'
        }, 201))
        .mockResolvedValueOnce(json([{ id: '018f6ff5-9055-7c82-b0de-83cfd0bd9901', label: 'A1', status: 'OCCUPIED' }]))
    );

    renderBooking(<BookingPage />);

    await userEvent.click(await screen.findByRole('button', { name: 'A1, available' }));
    await userEvent.type(screen.getByLabelText('Customer ID'), '018f6ff5-9055-7c82-b0de-83cfd0bd9910');
    await userEvent.click(screen.getByRole('button', { name: 'Confirm booking' }));

    await waitFor(() => expect(screen.getByText(/Booking confirmed/)).toBeInTheDocument());
    expect(await screen.findByRole('button', { name: 'A1, occupied' })).toBeDisabled();
  });
});

function json(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'Content-Type': 'application/json' }
  });
}
