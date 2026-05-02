import { useMemo, useState } from 'react';
import { BookingApiError, type Seat } from '../types';
import { useCreateBooking } from '../hooks/useCreateBooking';
import { useSeats } from '../hooks/useSeats';
import { BookingForm } from './BookingForm';
import { SeatGrid } from './SeatGrid';

export function BookingPage() {
  const { seats, selectedDay, setSelectedDay, isLoading, error, refresh } = useSeats();
  const [selectedSeatId, setSelectedSeatId] = useState<string | null>(null);
  const booking = useCreateBooking(refresh);

  const selectedSeat = useMemo(
    () => seats.find((seat) => seat.id === selectedSeatId && seat.status === 'AVAILABLE') ?? null,
    [seats, selectedSeatId]
  );
  const hasSeats = seats.length > 0;
  const noAvailableSeats = hasSeats && seats.every((seat) => seat.status === 'OCCUPIED');

  function handleSelectSeat(seat: Seat) {
    if (seat.status === 'AVAILABLE') {
      booking.reset();
      setSelectedSeatId(seat.id);
    }
  }

  async function handleSubmit(customerEmail: string) {
    if (!selectedSeat) {
      return;
    }
    const created = await booking.submit(selectedSeat.id, customerEmail, selectedDay);
    if (created) {
      setSelectedSeatId(null);
    }
  }

  return (
    <main className="booking-page">
      <section className="booking-layout" aria-labelledby="booking-title">
        <header className="booking-header">
          <h1 id="booking-title">Seat booking</h1>
          <p>Choose an available seat and confirm the booking with a customer email.</p>
        </header>

        <div className="day-selector">
          <label htmlFor="booking-day">Booking day</label>
          <input
            id="booking-day"
            name="booking-day"
            type="date"
            value={selectedDay}
            onChange={(event) => {
              booking.reset();
              setSelectedSeatId(null);
              setSelectedDay(event.target.value);
            }}
          />
        </div>

        <div>
          {isLoading ? <p className="status-copy">Loading seat availability...</p> : null}
          {error ? (
            <div className="alert alert-error" role="alert">
              {error}
            </div>
          ) : null}
          {!isLoading && !error && !hasSeats ? <p className="status-copy">No seats are configured yet.</p> : null}
          {!isLoading && noAvailableSeats ? <p className="status-copy">All seats are occupied for {selectedDay}.</p> : null}
          {booking.error ? (
            <div className="alert alert-error" role="alert">
              {formatBookingError(booking.error)}
            </div>
          ) : null}
          {booking.booking ? (
            <div className="alert alert-success" role="status">
              Booking confirmed for customer {booking.booking.customerEmail} on {booking.booking.bookedDay}.
            </div>
          ) : null}
          {hasSeats ? (
            <SeatGrid seats={seats} selectedDay={selectedDay} selectedSeatId={selectedSeatId} onSelectSeat={handleSelectSeat} />
          ) : null}
        </div>

        <BookingForm selectedSeat={selectedSeat} selectedDay={selectedDay} isPending={booking.isPending} onSubmit={handleSubmit} />
      </section>
    </main>
  );
}

function formatBookingError(error: Error): string {
  if (error instanceof BookingApiError) {
    if (error.fieldErrors.length > 0) {
      return error.fieldErrors.map((fieldError) => fieldError.message).join(' ');
    }
    if (error.status === 409 && typeof error.details.bookedDay === 'string') {
      return `${error.message} Refreshing availability for ${error.details.bookedDay}.`;
    }
    return error.message;
  }
  return error.message;
}
