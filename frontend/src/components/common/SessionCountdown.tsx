'use client';

import { jwtDecode } from 'jwt-decode';
import { useEffect, useState } from 'react';

import { getToken } from '@/lib/helper';

interface JWTPayload {
  exp: number;
}

function getRemainingTime(exp: number) {
  const now = Math.floor(Date.now() / 1000);
  const diff = exp - now;

  if (diff <= 0) return 'Session expired';

  const minutes = Math.floor(diff / 60);
  const seconds = diff % 60;

  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

export default function SessionCountdown() {
  const [countdown, setCountdown] = useState<string>('Loading...');
  const [expired, setExpired] = useState<boolean>(false);
  const [invalid, setInvalid] = useState<boolean>(false);

  useEffect(() => {
    const token = getToken();
    if (!token) {
      setCountdown('No session found');
      return;
    }

    let decoded: JWTPayload;
    try {
      decoded = jwtDecode(token);
    } catch (err) {
      setCountdown('Invalid token');
      setInvalid(true);
      return;
    }

    const updateCountdown = () => {
      const remaining = decoded.exp - Math.floor(Date.now() / 1000);
      if (remaining <= 0) {
        setCountdown('Session expired');
        setExpired(true);
      } else {
        setCountdown(getRemainingTime(decoded.exp));
      }
    };

    updateCountdown(); // Call once immediately
    const interval = setInterval(updateCountdown, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className='fixed top-4 right-4 z-50'>
      <div
        className={`text-sm font-semibold px-4 py-2 rounded-xl shadow-lg transition-colors duration-300 ${
          expired || invalid
            ? 'bg-red-100 text-red-600 border border-red-300'
            : 'bg-gray-100 text-gray-700 border border-black'
        }`}
      >
        Session: {countdown}
      </div>
    </div>
  );
}
