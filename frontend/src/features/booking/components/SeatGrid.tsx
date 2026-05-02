import type { Seat } from '../types';

interface SeatGridProps {
  seats: Seat[];
  selectedDay: string;
  selectedSeatId: string | null;
  onSelectSeat: (seat: Seat) => void;
}

export function SeatGrid({ seats, selectedDay, selectedSeatId, onSelectSeat }: SeatGridProps) {
  return (
    <div className="seat-grid" role="list" aria-label={`Bookable seats for ${selectedDay}`}>
      {seats.map((seat) => {
        const occupied = seat.status === 'OCCUPIED';
        return (
          <button
            key={seat.id}
            type="button"
            className="seat-button"
            data-status={seat.status}
            disabled={occupied}
            aria-pressed={selectedSeatId === seat.id}
            aria-label={`${seat.label}, ${occupied ? 'occupied' : 'available'}`}
            onClick={() => onSelectSeat(seat)}
          >
            <span className="seat-label">{seat.label}</span>
            <span className="seat-status">{occupied ? 'Occupied' : 'Available'}</span>
          </button>
        );
      })}
    </div>
  );
}
