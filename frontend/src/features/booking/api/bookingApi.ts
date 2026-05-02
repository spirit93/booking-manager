import { requestJson } from './httpClient';
import type { Booking, CreateBookingRequest, SeatAvailability } from '../types';

export function listSeatAvailability(day: string): Promise<SeatAvailability> {
  return requestJson<SeatAvailability>(`/api/seats/availability?day=${encodeURIComponent(day)}`);
}

export function createBooking(request: CreateBookingRequest): Promise<Booking> {
  return requestJson<Booking>('/api/bookings', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}
