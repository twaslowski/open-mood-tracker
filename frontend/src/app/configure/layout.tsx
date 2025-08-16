'use client';

import { ReactNode } from 'react';

import ConfigureContent from '@/components/ConfigureContent';

export default function ConfigureLayout({ children }: { children: ReactNode }) {
  return <ConfigureContent>{children}</ConfigureContent>;
}
