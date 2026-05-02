import { cleanup, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, beforeEach, describe, expect, test, vi } from 'vitest';
import { SeatBookingPage } from './SeatBookingPage';

const availability = (day: string, seats = [
  { seatId: 'A1', label: 'A1', status: 'AVAILABLE' },
  { seatId: 'A2', label: 'A2', status: 'OCCUPIED', bookingId: 'booking-a2' },
]) => ({
  day,
  seats,
});

function jsonResponse(body: unknown, init: ResponseInit = {}) {
  return new Response(JSON.stringify(body), {
    status: init.status ?? 200,
    headers: { 'Content-Type': 'application/json' },
  });
}

describe('SeatBookingPage', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
      const url = String(input);
      if (url.startsWith('/api/seats/availability')) {
        const day = new URL(`http://test${url}`).searchParams.get('day') ?? '2026-05-02';
        return jsonResponse(availability(day));
      }
      if (url === '/api/bookings' && init?.method === 'POST') {
        return jsonResponse({
          bookingId: 'booking-a1',
          seatId: 'A1',
          day: '2026-05-02',
          status: 'ACTIVE',
          createdAt: '2026-05-02T12:00:00Z',
        }, { status: 201 });
      }
      return jsonResponse({ code: 'NOT_FOUND', message: 'Missing mock' }, { status: 404 });
    }));
  });

  afterEach(() => {
    cleanup();
    vi.unstubAllGlobals();
  });

  test('loads availability and switches days', async () => {
    const user = userEvent.setup();
    render(<SeatBookingPage />);

    expect(screen.getByRole('status')).toHaveTextContent('Loading availability');
    expect(await screen.findByRole('button', { name: /A1 available/i })).toBeEnabled();
    expect(screen.getByRole('button', { name: /A2 occupied/i })).toBeDisabled();

    await user.clear(screen.getByLabelText(/booking day/i));
    await user.type(screen.getByLabelText(/booking day/i), '2026-05-03');

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('/api/seats/availability?day=2026-05-03', expect.any(Object));
    });
  });

  test('shows empty and failure states', async () => {
    vi.mocked(fetch).mockResolvedValueOnce(jsonResponse(availability('2026-05-02', [])));
    const { unmount } = render(<SeatBookingPage />);

    expect(await screen.findByText(/No seats are available/i)).toBeInTheDocument();
    unmount();

    vi.mocked(fetch).mockResolvedValueOnce(jsonResponse({ code: 'BROKEN', message: 'Service unavailable.' }, { status: 500 }));
    render(<SeatBookingPage />);

    expect(await screen.findByRole('alert')).toHaveTextContent('Service unavailable.');
  });

  test('books selected seat and refreshes availability', async () => {
    const user = userEvent.setup();
    render(<SeatBookingPage />);

    await user.click(await screen.findByRole('button', { name: /A1 available/i }));
    await user.click(screen.getByRole('button', { name: /book selected seat/i }));

    expect(await screen.findByText(/Seat A1 booked for 2026-05-02/i)).toBeInTheDocument();
    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith('/api/bookings', expect.objectContaining({ method: 'POST' }));
      expect(vi.mocked(fetch).mock.calls.filter(([url]) => String(url).startsWith('/api/seats/availability'))).toHaveLength(2);
    });
  });

  test('shows conflict and refreshes availability after duplicate booking', async () => {
    vi.mocked(fetch).mockImplementation(async (input: RequestInfo | URL, init?: RequestInit) => {
      const url = String(input);
      if (url.startsWith('/api/seats/availability')) {
        return jsonResponse(availability('2026-05-02'));
      }
      if (url === '/api/bookings' && init?.method === 'POST') {
        return jsonResponse({ code: 'SEAT_ALREADY_BOOKED', message: 'Seat is already occupied for this day.' }, { status: 409 });
      }
      return jsonResponse({}, { status: 404 });
    });

    const user = userEvent.setup();
    render(<SeatBookingPage />);

    await user.click(await screen.findByRole('button', { name: /A1 available/i }));
    await user.click(screen.getByRole('button', { name: /book selected seat/i }));

    expect(await screen.findByRole('alert')).toHaveTextContent('already been booked');
    await waitFor(() => {
      expect(vi.mocked(fetch).mock.calls.filter(([url]) => String(url).startsWith('/api/seats/availability'))).toHaveLength(2);
    });
  });

  test('renders persisted occupied seats returned by the API', async () => {
    render(<SeatBookingPage />);

    expect(await screen.findByRole('button', { name: /A2 occupied/i })).toBeDisabled();
  });
});
