import { useEffect, useState } from 'react';
import { getSeatAvailability } from './api';
import type { SeatAvailabilityResponse } from './types';

type AvailabilityState = {
  data: SeatAvailabilityResponse | null;
  loading: boolean;
  error: string | null;
  reload: () => void;
};

export function useSeatAvailability(day: string): AvailabilityState {
  const [data, setData] = useState<SeatAvailabilityResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [version, setVersion] = useState(0);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError(null);

    getSeatAvailability(day, controller.signal)
      .then((response) => setData(response))
      .catch((reason: unknown) => {
        if (controller.signal.aborted) {
          return;
        }
        setData(null);
        setError(reason instanceof Error ? reason.message : 'Unable to load availability.');
      })
      .finally(() => {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      });

    return () => controller.abort();
  }, [day, version]);

  return {
    data,
    loading,
    error,
    reload: () => setVersion((current) => current + 1),
  };
}
