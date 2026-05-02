import { useEffect, useMemo, useState } from 'react';
import { ApiRequestError } from '../shared/request';
import { createBooking } from './api';
import { SeatMap } from './SeatMap';
import { useSeatAvailability } from './useSeatAvailability';

const initialDay = '2026-05-02';

export function SeatBookingPage() {
  const [day, setDay] = useState(initialDay);
  const [selectedSeatId, setSelectedSeatId] = useState<string | null>(null);
  const [bookingMessage, setBookingMessage] = useState<string | null>(null);
  const [bookingError, setBookingError] = useState<string | null>(null);
  const [booking, setBooking] = useState(false);
  const availability = useSeatAvailability(day);

  const selectedSeat = useMemo(
    () => availability.data?.seats.find((seat) => seat.seatId === selectedSeatId) ?? null,
    [availability.data, selectedSeatId],
  );

  useEffect(() => {
    setSelectedSeatId(null);
    setBookingMessage(null);
    setBookingError(null);
  }, [day]);

  useEffect(() => {
    if (selectedSeat?.status === 'OCCUPIED') {
      setSelectedSeatId(null);
    }
  }, [selectedSeat]);

  async function handleCreateBooking() {
    if (!selectedSeatId) {
      setBookingError('Choose an available seat first.');
      return;
    }

    setBooking(true);
    setBookingError(null);
    setBookingMessage(null);

    try {
      const response = await createBooking(selectedSeatId, day);
      setBookingMessage(`Seat ${response.seatId} booked for ${response.day}.`);
      setSelectedSeatId(null);
      availability.reload();
    } catch (reason) {
      if (reason instanceof ApiRequestError && reason.status === 409) {
        setBookingError('That seat has already been booked for this day.');
        availability.reload();
      } else {
        setBookingError(reason instanceof Error ? reason.message : 'Unable to create booking.');
      }
    } finally {
      setBooking(false);
    }
  }

  return (
    <main className="booking-page">
      <section className="booking-toolbar" aria-labelledby="page-title">
        <div>
          <h1 id="page-title">Seat Booking</h1>
          <p>Reserve a seat for one calendar day.</p>
        </div>
        <label className="date-field">
          <span>Booking day</span>
          <input type="date" value={day} onChange={(event) => setDay(event.target.value)} />
        </label>
      </section>

      {availability.loading && <p role="status">Loading availability...</p>}
      {availability.error && <p role="alert" className="error-state">{availability.error}</p>}

      {availability.data && !availability.loading && !availability.error && (
        <section className="booking-workspace" aria-label={`Availability for ${availability.data.day}`}>
          <SeatMap
            seats={availability.data.seats}
            selectedSeatId={selectedSeatId}
            onSelectSeat={(seatId) => {
              setSelectedSeatId(seatId);
              setBookingError(null);
              setBookingMessage(null);
            }}
          />
          <aside className="booking-panel" aria-label="Booking confirmation">
            <h2>Confirm Booking</h2>
            <p>{selectedSeat ? `Selected seat: ${selectedSeat.label}` : 'No seat selected.'}</p>
            <button type="button" onClick={handleCreateBooking} disabled={booking || !selectedSeatId}>
              {booking ? 'Booking...' : 'Book selected seat'}
            </button>
            {bookingMessage && <p role="status" className="success-state">{bookingMessage}</p>}
            {bookingError && <p role="alert" className="error-state">{bookingError}</p>}
          </aside>
        </section>
      )}
    </main>
  );
}
