'use client';

import { ReactNode, Suspense } from 'react';

import ConfigureContent from '@/components/ConfigureContent';

function Loading() {
  return (
    <div className='min-h-screen bg-gradient-to-b from-white to-indigo-100 flex items-center justify-center p-6'>
      <div className='mb-6 p-4 bg-blue-50 text-blue-800 rounded-lg border border-blue-100 flex items-center'>
        <div className='animate-spin mr-3 h-4 w-4 border-2 border-blue-600 border-t-transparent rounded-full'></div>
        <span>Loading your configuration...</span>
      </div>
    </div>
  );
}

export default function ConfigureLayout({ children }: { children: ReactNode }) {
  return (
    <Suspense fallback={<Loading />}>
      <ConfigureContent>{children}</ConfigureContent>
    </Suspense>
  );
}
