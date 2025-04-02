'use client';

import { BellIcon, ChartAreaIcon } from 'lucide-react';
import Link from 'next/link';
import { useSearchParams } from 'next/navigation';
import React, { useEffect, useState } from 'react';

import { storeToken } from '@/lib/helper';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

import { siteConfig } from '@/constant/config';

export default function ConfigurePage() {
  const searchParams = useSearchParams();
  const [isLoading, setIsLoading] = useState(true);
  const [_token, setToken] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    const validateToken = async (token: string) => {
      const result = await fetch(`/api/v1/session/validate`, {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + token,
        },
      });
      if (!result.ok) {
        throw new Error('Invalid token');
      }
      return result;
    };

    const urlToken = searchParams.get('token');
    if (!urlToken) {
      setError(
        'No valid session token provided. Please head back to the bot and create a session using the /configure command.'
      );
      return;
    }
    validateToken(urlToken)
      .then(() => {
        storeToken(urlToken);
        setToken(urlToken);
        setIsLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        localStorage.removeItem('authToken');
        setToken('');
        setIsLoading(false);
      });
  }, [searchParams]);

  return (
    <div className='min-h-screen bg-gradient-to-b from-white to-indigo-100 flex items-center justify-center p-6'>
      <Card className='w-full max-w-xl shadow-xl border rounded-lg bg-white transition-all duration-300 hover:shadow-2xl'>
        <CardHeader className='border-b border-gray-100'>
          <CardTitle className='flex items-center text-xl font-bold text-gray-800'>
            <span className='mr-2 text-2xl'>⚙</span> Bot Configuration
          </CardTitle>
          <p className='text-gray-500 text-sm mt-1'>Manage your bot settings and preferences</p>
        </CardHeader>

        <CardContent className='p-6'>
          {isLoading && (
            <div className='mb-6 p-4 bg-blue-50 text-blue-800 rounded-lg border border-blue-100 flex items-center'>
              <div className='animate-spin mr-3 h-4 w-4 border-2 border-blue-600 border-t-transparent rounded-full'></div>
              <span>Loading your configuration...</span>
            </div>
          )}

          {!error && !isLoading && (
            <div>
              <p className='text-gray-600 mb-6 leading-relaxed'>
                Configure your bot preferences below to customize your experience.
              </p>

              <div className='flex flex-col space-y-4'>
                <Button
                  className='bg-gradient-to-r from-primary-500 to-primary-600 hover:from-primary-600 hover:to-primary-700 text-white py-3 flex items-center justify-center transition-all duration-200 shadow-md hover:shadow-lg'
                  asChild
                >
                  <Link href='/configure/metrics'>
                    <ChartAreaIcon />
                    Metrics
                  </Link>
                </Button>

                <Button
                  className='bg-gradient-to-r from-primary-500 to-primary-600 hover:from-primary-600 hover:to-primary-700 text-white py-3 flex items-center justify-center transition-all duration-200 shadow-md hover:shadow-lg'
                  asChild
                >
                  <Link href='/configure/notifications'>
                    <BellIcon />
                    Notification Settings
                  </Link>
                </Button>

                <div className='mt-4 p-4 bg-gray-50 rounded-lg border border-gray-100'>
                  <h3 className='text-sm font-medium text-gray-700 mb-2'>Advanced Settings</h3>
                  <p className='text-xs text-gray-500'>
                    Configure advanced options and integrations for your bot
                  </p>
                  <Button
                    variant='outline'
                    className='mt-3 w-full text-gray-700 border-gray-300 hover:bg-gray-100'
                  >
                    Open Advanced Settings
                  </Button>
                </div>
              </div>
            </div>
          )}

          {error && (
            <div className='mb-6 p-5 bg-red-50 text-red-600 rounded-lg border border-red-100'>
              <div className='flex items-center mb-3'>
                <svg
                  className='w-5 h-5 mr-2 text-red-500'
                  fill='none'
                  stroke='currentColor'
                  viewBox='0 0 24 24'
                  xmlns='http://www.w3.org/2000/svg'
                >
                  <path
                    strokeLinecap='round'
                    strokeLinejoin='round'
                    strokeWidth={2}
                    d='M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z'
                  />
                </svg>
                <span className='font-medium'>No configuration token found!</span>
              </div>
              <p className='mt-2 text-red-600 text-sm'>
                Please use the{' '}
                <code className='bg-red-100 px-1 py-0.5 rounded text-red-700'>/configure</code>{' '}
                command in your
                <a
                  className='text-red-700 font-medium underline ml-1 hover:text-red-800 transition-colors'
                  href={`https://t.me/${siteConfig.telegramBotName}`}
                >
                  Telegram Bot{' '}
                </a>
                to get a valid configuration link.
              </p>
            </div>
          )}

          <div className='mt-6 pt-4 border-t border-gray-100 text-center text-xs text-gray-400'>
            Need help? Visit our{' '}
            <a href='#' className='text-primary-500 hover:underline'>
              support page
            </a>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
