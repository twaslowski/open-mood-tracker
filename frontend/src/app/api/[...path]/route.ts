import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest, { params }: { params: { path: string[] } }) {
  return proxyRequest(request, params.path);
}

export async function POST(request: NextRequest, { params }: { params: { path: string[] } }) {
  return proxyRequest(request, params.path);
}

export async function PUT(request: NextRequest, { params }: { params: { path: string[] } }) {
  return proxyRequest(request, params.path);
}

export async function DELETE(request: NextRequest, { params }: { params: { path: string[] } }) {
  return proxyRequest(request, params.path);
}

export async function PATCH(request: NextRequest, { params }: { params: { path: string[] } }) {
  return proxyRequest(request, params.path);
}

async function proxyRequest(request: NextRequest, path: string[]) {
  const serverHost = process.env.BACKEND_HOST;

  // Explicit error handling for missing BACKEND_HOST
  if (!serverHost) {
    const errorMessage =
      'BACKEND_HOST environment variable is not configured. Please set BACKEND_HOST when starting the container.';

    return NextResponse.json(
      {
        error: 'Backend server not configured',
        message: errorMessage,
        help: 'Set the BACKEND_HOST environment variable to your backend server URL',
      },
      { status: 500 },
    );
  }

  // Validate BACKEND_HOST format
  try {
    new URL(serverHost);
  } catch (error) {
    const errorMessage = `Invalid BACKEND_HOST format: "${serverHost}". Must be a valid URL (e.g., http://localhost:8080)`;

    return NextResponse.json(
      {
        error: 'Invalid backend server configuration',
        message: errorMessage,
        received: serverHost,
      },
      { status: 500 },
    );
  }

  try {
    const targetUrl = `${serverHost}/api/${path.join('/')}`;
    const url = new URL(targetUrl);

    // Copy query parameters
    const searchParams = request.nextUrl.searchParams;
    searchParams.forEach((value, key) => {
      url.searchParams.append(key, value);
    });

    // Prepare headers for the proxied request
    const headers = new Headers();

    // Copy relevant headers from the original request
    const headersToProxy = [
      'authorization',
      'content-type',
      'accept',
      'user-agent',
      'x-requested-with',
    ];

    headersToProxy.forEach((headerName) => {
      const headerValue = request.headers.get(headerName);
      if (headerValue) {
        headers.set(headerName, headerValue);
      }
    });

    // Prepare the request body for non-GET requests
    let body: string | undefined;
    if (request.method !== 'GET' && request.method !== 'HEAD') {
      try {
        body = await request.text();
      } catch (error) {
        // If body is empty or can't be read, that's okay
        body = undefined;
      }
    }

    // Make the proxied request
    const response = await fetch(url.toString(), {
      method: request.method,
      headers,
      body,
    });

    // Get the response data
    const responseText = await response.text();

    // Create the response with the same status and headers
    const proxyResponse = new NextResponse(responseText, {
      status: response.status,
      statusText: response.statusText,
    });

    // Copy relevant response headers
    const responseHeadersToProxy = ['content-type', 'cache-control', 'etag', 'last-modified'];

    responseHeadersToProxy.forEach((headerName) => {
      const headerValue = response.headers.get(headerName);
      if (headerValue) {
        proxyResponse.headers.set(headerName, headerValue);
      }
    });

    return proxyResponse;
  } catch (error) {
    const errorMessage = `Failed to proxy request to backend server at ${serverHost}`;

    return NextResponse.json(
      {
        error: 'Backend server unreachable',
        message: errorMessage,
        serverHost,
        details: error instanceof Error ? error.message : 'Unknown error',
      },
      { status: 502 },
    );
  }
}
