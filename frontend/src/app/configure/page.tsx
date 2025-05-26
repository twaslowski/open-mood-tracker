import { BellIcon, ChartAreaIcon } from 'lucide-react';
import Link from 'next/link';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

export default function ConfigurePage() {
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
                disabled={true}
                className='bg-gradient-to-r from-primary-500 to-primary-600 hover:from-primary-600 hover:to-primary-700 text-white py-3 flex items-center justify-center transition-all duration-200 shadow-md hover:shadow-lg'
                asChild
              >
                <Link href='/configure/notifications'>
                  <BellIcon />
                  Notification Settings (coming soon)
                </Link>
              </Button>
            </div>
          </div>

          <div className='mt-6 pt-4 border-t border-gray-100 text-center text-xs text-gray-400'>
            Need help? Visit our{' '}
            <a
              href='https://github.com/twaslowski/open-mood-tracker?tab=readme-ov-file#usage'
              className='text-primary-500 hover:underline'
            >
              support page (to be expanded)
            </a>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
