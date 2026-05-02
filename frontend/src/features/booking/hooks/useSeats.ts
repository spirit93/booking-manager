import { useCallback, useEffect, useState } from 'react';
import { listSeatAvailability } from '../api/bookingApi';
import type { Seat } from '../types';
import { getDefaultBookingDay } from './useBookingDay';

export interface UseSeatsResult {
  seats: Seat[];
  selectedDay: string;
  setSelectedDay: (day: string) => void;
  isLoading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
}

export function useSeats(): UseSeatsResult {
  const [selectedDay, setSelectedDay] = useState(() => getDefaultBookingDay());
  const [seats, setSeats] = useState<Seat[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const availability = await listSeatAvailability(selectedDay);
      setSeats(availability.seats);
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : 'Unable to load seat availability.');
      setSeats([]);
    } finally {
      setIsLoading(false);
    }
  }, [selectedDay]);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  return { seats, selectedDay, setSelectedDay, isLoading, error, refresh };
}
