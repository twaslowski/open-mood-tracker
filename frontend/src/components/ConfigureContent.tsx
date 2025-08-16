'use client';

import React, { ReactElement } from 'react';

import { useConfigAuth } from '@/hooks/useConfigAuth';

import InvalidToken from '@/components/common/InvalidToken';

export default function ConfigureContent({
  children,
}: {
  children: React.ReactNode;
}): ReactElement {
  const { error, isAuthenticated, isLoading } = useConfigAuth();

  // Show loading state while authentication is in progress
  if (isLoading) {
    return (
      <div className='min-h-screen bg-gradient-to-b from-white to-indigo-100 flex items-center justify-center p-6'>
        <div className='mb-6 p-4 bg-blue-50 text-blue-800 rounded-lg border border-blue-100 flex items-center'>
          <div className='animate-spin mr-3 h-4 w-4 border-2 border-blue-600 border-t-transparent rounded-full'></div>
          <span>Verifying authentication...</span>
        </div>
      </div>
    );
  }

  if (error || !isAuthenticated) {
    return (
      <div>
        <InvalidToken />
      </div>
    );
  }

  return children as ReactElement;
}
