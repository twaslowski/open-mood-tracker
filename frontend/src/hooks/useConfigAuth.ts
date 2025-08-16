import { useSearchParams } from 'next/navigation';
import { useEffect, useState } from 'react';

import { storeToken } from '@/lib/helper';
import { retrieveNonExpiredToken, validateToken } from '@/lib/token';

export function useConfigAuth() {
  const searchParams = useSearchParams();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const NO_TOKEN_ERROR =
    'No configuration token found! Please use the /configure command in your Telegram Bot to get a valid configuration link.';

  useEffect(() => {
    const token = searchParams.get('token') || retrieveNonExpiredToken();
    if (!token) {
      setError(NO_TOKEN_ERROR);
      setIsLoading(false);
      return;
    }

    validateToken(token)
      .then((validToken) => {
        storeToken(validToken);
        setIsAuthenticated(true);
        setIsLoading(false);
      })
      .catch(() => {
        setError(NO_TOKEN_ERROR);
        localStorage.removeItem('authToken');
        setIsLoading(false);
      });
  }, [searchParams]);

  return { isLoading, error, isAuthenticated };
}
