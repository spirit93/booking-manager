export type SeatStatus = 'AVAILABLE' | 'OCCUPIED';
export type BookingStatus = 'ACTIVE';

export interface Seat {
  id: string;
  label: string;
  status: SeatStatus;
}

export interface SeatAvailability {
  day: string;
  seats: Seat[];
}

export interface CreateBookingRequest {
  seatId: string;
  customerEmail: string;
  bookedDay: string;
}

export interface Booking {
  id: string;
  seatId: string;
  customerEmail: string;
  bookedDay: string;
  status: BookingStatus;
  createdAt: string;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ApiErrorBody {
  code: string;
  message: string;
  details?: Record<string, unknown>;
  fieldErrors?: FieldError[];
}

export class BookingApiError extends Error {
  readonly status: number;
  readonly code: string;
  readonly details: Record<string, unknown>;
  readonly fieldErrors: FieldError[];

  constructor(status: number, body: ApiErrorBody) {
    super(body.message);
    this.name = 'BookingApiError';
    this.status = status;
    this.code = body.code;
    this.details = body.details ?? {};
    this.fieldErrors = body.fieldErrors ?? [];
  }
}
