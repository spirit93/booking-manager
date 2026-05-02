import { requestJson } from '../shared/request';
import type { BookingResponse, SeatAvailabilityResponse } from './types';

export function getSeatAvailability(day: string, signal?: AbortSignal) {
  return requestJson<SeatAvailabilityResponse>(`/api/seats/availability?day=${encodeURIComponent(day)}`, { signal });
}

export function createBooking(seatId: string, day: string) {
  return requestJson<BookingResponse>('/api/bookings', {
    method: 'POST',
    body: JSON.stringify({ seatId, day }),
  });
}
