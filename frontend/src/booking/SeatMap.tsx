import type { SeatAvailabilityItem } from './types';

type SeatMapProps = {
  seats: SeatAvailabilityItem[];
  selectedSeatId: string | null;
  onSelectSeat: (seatId: string) => void;
};

export function SeatMap({ seats, selectedSeatId, onSelectSeat }: SeatMapProps) {
  if (seats.length === 0) {
    return <p className="empty-state">No seats are available for display.</p>;
  }

  return (
    <div className="seat-map" aria-label="Seat map">
      {seats.map((seat) => {
        const occupied = seat.status === 'OCCUPIED';
        const selected = selectedSeatId === seat.seatId;
        return (
          <button
            className={`seat-button ${occupied ? 'occupied' : 'available'} ${selected ? 'selected' : ''}`}
            type="button"
            key={seat.seatId}
            disabled={occupied}
            aria-pressed={selected}
            aria-label={`${seat.label} ${occupied ? 'occupied' : 'available'}`}
            onClick={() => onSelectSeat(seat.seatId)}
          >
            <span>{seat.label}</span>
            <small>{occupied ? 'Occupied' : 'Free'}</small>
          </button>
        );
      })}
    </div>
  );
}
