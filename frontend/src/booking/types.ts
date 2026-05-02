export type SeatStatus = 'AVAILABLE' | 'OCCUPIED';

export type SeatAvailabilityItem = {
  seatId: string;
  label: string;
  status: SeatStatus;
  bookingId?: string | null;
};

export type SeatAvailabilityResponse = {
  day: string;
  seats: SeatAvailabilityItem[];
};

export type BookingResponse = {
  bookingId: string;
  seatId: string;
  day: string;
  status: 'ACTIVE';
  createdAt: string;
};
