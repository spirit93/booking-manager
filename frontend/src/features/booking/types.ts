export type SeatStatus = 'AVAILABLE' | 'OCCUPIED';
export type BookingStatus = 'ACTIVE';

export interface Seat {
  id: string;
  label: string;
  status: SeatStatus;
}

export interface CreateBookingRequest {
  seatId: string;
  customerId: string;
}

export interface Booking {
  id: string;
  seatId: string;
  customerId: string;
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
  readonly fieldErrors: FieldError[];

  constructor(status: number, body: ApiErrorBody) {
    super(body.message);
    this.name = 'BookingApiError';
    this.status = status;
    this.code = body.code;
    this.fieldErrors = body.fieldErrors ?? [];
  }
}
