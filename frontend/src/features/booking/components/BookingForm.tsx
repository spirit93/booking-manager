import { useState, type FormEvent } from 'react';
import type { Seat } from '../types';

interface BookingFormProps {
  selectedSeat: Seat | null;
  isPending: boolean;
  onSubmit: (customerId: string) => Promise<void>;
}

const uuidPattern = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

export function BookingForm({ selectedSeat, isPending, onSubmit }: BookingFormProps) {
  const [customerId, setCustomerId] = useState('');
  const [validationError, setValidationError] = useState<string | null>(null);
  const canSubmit = Boolean(selectedSeat) && !isPending;

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedSeat) {
      setValidationError('Select an available seat first.');
      return;
    }
    if (!uuidPattern.test(customerId)) {
      setValidationError('Enter a valid customer UUID.');
      return;
    }
    setValidationError(null);
    await onSubmit(customerId);
  }

  return (
    <form className="booking-form" onSubmit={handleSubmit} noValidate>
      <h2>Book a seat</h2>
      <p className="status-copy" aria-live="polite">
        {selectedSeat ? `Selected seat ${selectedSeat.label}` : 'No seat selected'}
      </p>
      <div className="field">
        <label htmlFor="customerId">Customer ID</label>
        <input
          id="customerId"
          name="customerId"
          value={customerId}
          onChange={(event) => setCustomerId(event.target.value)}
          placeholder="018f6ff5-9055-7c82-b0de-83cfd0bd9910"
          aria-invalid={Boolean(validationError)}
          aria-describedby={validationError ? 'customerId-error' : undefined}
        />
        {validationError ? (
          <span id="customerId-error" className="alert alert-error">
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
