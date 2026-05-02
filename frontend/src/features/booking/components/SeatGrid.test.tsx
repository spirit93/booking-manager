import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import { renderBooking } from '../../../test/render';
import { SeatGrid } from './SeatGrid';

describe('SeatGrid', () => {
  it('labels available and occupied seats and disables occupied seats', async () => {
    const onSelectSeat = vi.fn();
    renderBooking(
      <SeatGrid
        seats={[
          { id: '1', label: 'A1', status: 'AVAILABLE' },
          { id: '2', label: 'A2', status: 'OCCUPIED' }
        ]}
        selectedDay="2026-05-02"
        selectedSeatId={null}
        onSelectSeat={onSelectSeat}
      />
    );

    expect(screen.getByRole('button', { name: 'A1, available' })).toBeEnabled();
    const occupiedSeat = screen.getByRole('button', { name: 'A2, occupied' });
    expect(occupiedSeat).toBeDisabled();
    expect(occupiedSeat).toHaveAttribute('data-status', 'OCCUPIED');

    await userEvent.click(screen.getByRole('button', { name: 'A1, available' }));
    expect(onSelectSeat).toHaveBeenCalledWith({ id: '1', label: 'A1', status: 'AVAILABLE' });
  });
});
