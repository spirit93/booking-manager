import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import { renderBooking } from '../../../test/render';
import { BookingForm } from './BookingForm';

describe('BookingForm', () => {
  it('requires a selected seat and valid customer UUID', async () => {
    const onSubmit = vi.fn();
    renderBooking(<BookingForm selectedSeat={{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }} selectedDay="2026-05-02" isPending={false} onSubmit={onSubmit} />);

    await userEvent.type(screen.getByLabelText('Customer ID'), 'not-a-uuid');
    await userEvent.click(screen.getByRole('button', { name: 'Confirm booking' }));

    expect(screen.getByText('Enter a valid customer UUID.')).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it('submits a valid customer identifier for the selected seat', async () => {
    const onSubmit = vi.fn().mockResolvedValue(undefined);
    renderBooking(<BookingForm selectedSeat={{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }} selectedDay="2026-05-02" isPending={false} onSubmit={onSubmit} />);

    await userEvent.type(screen.getByLabelText('Customer ID'), '018f6ff5-9055-7c82-b0de-83cfd0bd9910');
    await userEvent.click(screen.getByRole('button', { name: 'Confirm booking' }));

    expect(onSubmit).toHaveBeenCalledWith('018f6ff5-9055-7c82-b0de-83cfd0bd9910');
  });

  it('shows the selected day in the form context', () => {
    renderBooking(<BookingForm selectedSeat={{ id: 'seat-1', label: 'A1', status: 'AVAILABLE' }} selectedDay="2026-05-02" isPending={false} onSubmit={vi.fn()} />);

    expect(screen.getByText('Selected seat A1 for 2026-05-02')).toBeInTheDocument();
  });
});
