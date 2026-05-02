import { BookingApiError, type ApiErrorBody } from '../types';

const defaultError: ApiErrorBody = {
  code: 'SERVICE_UNAVAILABLE',
  message: 'Booking service is temporarily unavailable. Try again later.'
};

export async function requestJson<T>(input: RequestInfo | URL, init?: RequestInit): Promise<T> {
  let response: Response;
  try {
    response = await fetch(input, {
      headers: {
        'Content-Type': 'application/json',
        ...(init?.headers ?? {})
      },
      ...init
    });
  } catch {
    throw new BookingApiError(503, defaultError);
  }

  if (!response.ok) {
    const body = await parseError(response);
    throw new BookingApiError(response.status, body);
  }

  return response.json() as Promise<T>;
}

async function parseError(response: Response): Promise<ApiErrorBody> {
  try {
    const body = (await response.json()) as Partial<ApiErrorBody>;
    return {
      code: body.code ?? defaultError.code,
      message: body.message ?? defaultError.message,
      details: body.details,
      fieldErrors: body.fieldErrors
    };
  } catch {
    return defaultError;
  }
}
