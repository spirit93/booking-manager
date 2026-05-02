import { requestJson } from './httpClient';
import type { Booking, CreateBookingRequest, Seat } from '../types';

export function listSeats(): Promise<Seat[]> {
  return requestJson<Seat[]>('/api/seats');
}

export function createBooking(request: CreateBookingRequest): Promise<Booking> {
  return requestJson<Booking>('/api/bookings', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}
