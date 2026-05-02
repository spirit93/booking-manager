import { useState, type FormEvent } from 'react';
import type { Seat } from '../types';

interface BookingFormProps {
  selectedSeat: Seat | null;
  selectedDay: string;
  isPending: boolean;
  onSubmit: (customerEmail: string) => Promise<void>;
}

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function BookingForm({ selectedSeat, selectedDay, isPending, onSubmit }: BookingFormProps) {
  const [customerEmail, setCustomerEmail] = useState('');
  const [validationError, setValidationError] = useState<string | null>(null);
  const canSubmit = Boolean(selectedSeat) && !isPending;

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedSeat) {
      setValidationError('Select an available seat first.');
      return;
    }
    if (!emailPattern.test(customerEmail.trim())) {
      setValidationError('Enter a valid customer email.');
      return;
    }
    setValidationError(null);
    await onSubmit(customerEmail.trim());
  }

  return (
    <form className="booking-form" onSubmit={handleSubmit} noValidate>
      <h2>Book a seat</h2>
      <p className="status-copy" aria-live="polite">
        {selectedSeat ? `Selected seat ${selectedSeat.label} for ${selectedDay}` : `No seat selected for ${selectedDay}`}
      </p>
      <div className="field">
        <label htmlFor="customerEmail">Customer email</label>
        <input
          id="customerEmail"
          name="customerEmail"
          type="email"
          value={customerEmail}
          onChange={(event) => setCustomerEmail(event.target.value)}
          placeholder="customer@example.com"
          aria-invalid={Boolean(validationError)}
          aria-describedby={validationError ? 'customerEmail-error' : undefined}
        />
        {validationError ? (
          <span id="customerEmail-error" className="alert alert-error">
            {validationError}
          </span>
        ) : null}
      </div>
      <div className="action-row">
        <button className="primary-button" type="submit" disabled={!canSubmit}>
          {isPending ? 'Booking...' : 'Confirm booking'}
        </button>
      </div>
    </form>
  );
}
