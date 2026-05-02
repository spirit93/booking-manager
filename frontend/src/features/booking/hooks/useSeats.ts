import { useCallback, useEffect, useState } from 'react';
import { listSeats } from '../api/bookingApi';
import type { Seat } from '../types';

export interface UseSeatsResult {
  seats: Seat[];
  isLoading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
}

export function useSeats(): UseSeatsResult {
  const [seats, setSeats] = useState<Seat[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      setSeats(await listSeats());
    } catch (caught) {
      setError(caught instanceof Error ? caught.message : 'Unable to load seat availability.');
      setSeats([]);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void refresh();
  }, [refresh]);

  return { seats, isLoading, error, refresh };
}
