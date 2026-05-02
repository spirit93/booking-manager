import { renderHook, waitFor } from '@testing-library/react';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { useSeats } from './useSeats';

describe('useSeats', () => {
  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('exposes loading, success, empty, and failure states', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(json([{ id: '1', label: 'A1', status: 'AVAILABLE' }])));

    const { result } = renderHook(() => useSeats());
    expect(result.current.isLoading).toBe(true);

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.seats).toHaveLength(1);
    expect(result.current.error).toBeNull();
  });

  it('stores a service failure message', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('offline')));

    const { result } = renderHook(() => useSeats());

    await waitFor(() => expect(result.current.isLoading).toBe(false));
    expect(result.current.seats).toEqual([]);
    expect(result.current.error).toContain('Booking service is temporarily unavailable');
  });
});

function json(body: unknown) {
  return new Response(JSON.stringify(body), {
    headers: { 'Content-Type': 'application/json' }
  });
}
