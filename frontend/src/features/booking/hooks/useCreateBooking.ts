import { useState } from 'react';
import { createBooking } from '../api/bookingApi';
import { BookingApiError, type Booking } from '../types';

export interface CreateBookingState {
  isPending: boolean;
  booking: Booking | null;
  error: BookingApiError | Error | null;
  submit: (seatId: string, customerId: string) => Promise<Booking | null>;
  reset: () => void;
}

export function useCreateBooking(onCreated?: () => Promise<void> | void): CreateBookingState {
  const [isPending, setIsPending] = useState(false);
  const [booking, setBooking] = useState<Booking | null>(null);
  const [error, setError] = useState<BookingApiError | Error | null>(null);

  async function submit(seatId: string, customerId: string) {
    setIsPending(true);
    setError(null);
    setBooking(null);
    try {
      const created = await createBooking({ seatId, customerId });
      setBooking(created);
      await onCreated?.();
      return created;
    } catch (caught) {
      const normalized = caught instanceof Error ? caught : new Error('Booking failed.');
      setError(normalized);
      if (caught instanceof BookingApiError && caught.status === 409) {
        await onCreated?.();
      }
      return null;
    } finally {
      setIsPending(false);
    }
  }

  return {
    isPending,
    booking,
    error,
    submit,
    reset: () => {
      setBooking(null);
      setError(null);
    }
  };
}
