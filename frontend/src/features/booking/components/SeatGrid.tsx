import type { Seat } from '../types';

interface SeatGridProps {
  seats: Seat[];
  selectedSeatId: string | null;
  onSelectSeat: (seat: Seat) => void;
}

export function SeatGrid({ seats, selectedSeatId, onSelectSeat }: SeatGridProps) {
  return (
    <div className="seat-grid" role="list" aria-label="Bookable seats">
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
