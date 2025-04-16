'use client';

import { useSearchParams } from 'next/navigation';
import React, {ReactElement, useEffect, useState} from 'react';

import '@/styles/colors.css';

import { storeToken } from "@/lib/helper";
import { retrieveNonExpiredToken, validateToken } from '@/lib/token';

import InvalidToken from "@/components/common/InvalidToken";

export default function ConfigureContent({ children }: { children: React.ReactNode }): ReactElement {
  const searchParams = useSearchParams();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const NO_TOKEN_ERROR = 'No configuration token found! Please use the /configure command in your Telegram Bot to get a valid configuration link.';

  useEffect(() => {
    // Try using token from searchParams first; if none is found, check localStorage
    const token = searchParams.get('token') || retrieveNonExpiredToken();
    if (!token) {
      setError(NO_TOKEN_ERROR);
      setIsLoading(false);
      return;
    }

    // Validate token against backend
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

  // Show loading state
  if (isLoading) {
    return (
        <div className='min-h-screen bg-gradient-to-b from-white to-indigo-100 flex items-center justify-center p-6'>
          <div className='mb-6 p-4 bg-blue-50 text-blue-800 rounded-lg border border-blue-100 flex items-center'>
            <div className='animate-spin mr-3 h-4 w-4 border-2 border-blue-600 border-t-transparent rounded-full'></div>
            <span>Loading your configuration...</span>
          </div>
        </div>
    );
  }

  // Show error state
  if (error || !isAuthenticated) {
    return (
        <div>
          <InvalidToken />
        </div>
    );
  }

  return children as ReactElement;
}