export class ApiRequestError extends Error {
  readonly status: number;
  readonly code: string;
  readonly details: Record<string, string>;

  constructor(status: number, code: string, message: string, details: Record<string, string> = {}) {
    super(message);
    this.name = 'ApiRequestError';
    this.status = status;
    this.code = code;
    this.details = details;
  }
}

type ApiErrorBody = {
  code?: string;
  message?: string;
  details?: Record<string, string>;
};

export async function requestJson<T>(input: RequestInfo | URL, init?: RequestInit): Promise<T> {
  const response = await fetch(input, {
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
      ...init?.headers,
    },
    ...init,
  });

  if (!response.ok) {
    let body: ApiErrorBody = {};
    try {
      body = (await response.json()) as ApiErrorBody;
    } catch {
      body = {};
    }
    throw new ApiRequestError(
      response.status,
      body.code ?? 'REQUEST_FAILED',
      body.message ?? 'Request failed.',
      body.details ?? {},
    );
  }

  return (await response.json()) as T;
}
