import { BellIcon, ChartAreaIcon, ChartLine } from 'lucide-react';
import Link from 'next/link';

import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

export default function ConfigurePage() {
  return (
    <div className='page-gradient-bg gradient-blue'>
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
              <Button className='btn-gradient' asChild>
                <Link href='/configure/metrics'>
                  <ChartAreaIcon />
                  Metrics
                </Link>
              </Button>

              <Button disabled={true} className='btn-gradient' asChild>
                <Link href='/chart'>
                  <ChartLine />
                  Chart
                </Link>
              </Button>

              <Button disabled={true} className='btn-gradient' asChild>
                <Link href='/configure/notifications'>
                  <BellIcon />
                  Notification Settings (coming soon)
                </Link>
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
