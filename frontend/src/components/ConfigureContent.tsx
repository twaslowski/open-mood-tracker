'use client';

import React, { ReactElement } from 'react';

import { useConfigAuth } from '@/hooks/useConfigAuth';

import InvalidToken from '@/components/common/InvalidToken';

export default function ConfigureContent({
  children,
}: {
  children: React.ReactNode;
}): ReactElement {
  const { error, isAuthenticated } = useConfigAuth();

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
