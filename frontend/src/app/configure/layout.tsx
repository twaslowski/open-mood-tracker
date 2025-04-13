import * as React from 'react';

import '@/styles/colors.css';

import SessionCountdown from "@/components/common/SessionCountdown";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
      <div>
        <SessionCountdown/>
        <main>{children}</main>
      </div>
  );
}
