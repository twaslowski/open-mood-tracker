'use client';

import MetricList from '@/components/metric/MetricList';
import NewMetric from '@/components/metric/NewMetric';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

export default function Metrics() {
  return (
    <div className='container mx-auto px-4 py-8 max-w-7xl'>
      <a href='/configure'>
        <button className='flex items-center text-blue-600 hover:underline mb-4'>
          <svg
            xmlns='http://www.w3.org/2000/svg'
            className='h-5 w-5 mr-2'
            viewBox='0 0 20 20'
            fill='currentColor'
          >
            <path
              fillRule='evenodd'
              d='M10 18a1 1 0 01-.707-.293l-7-7a1 1 0 010-1.414l7-7a1 1 0 111.414 1.414L4.414 10l6.293 6.293A1 1 0 0110 18z'
              clipRule='evenodd'
            />
          </svg>
          Back to Configuration
        </button>
      </a>
      <main>
        <h1 className='text-3xl font-bold mb-4 text-gray-800'>Metrics Configuration</h1>
        <p className='text-gray-600 mb-8'>
          Configure which mental health metrics to track and create custom metrics.
        </p>

        <Tabs defaultValue='browse' className='w-full'>
          <TabsList className='grid w-full grid-cols-2 mb-8'>
            <TabsTrigger value='browse'>Browse Metrics</TabsTrigger>
            <TabsTrigger value='create' disabled>
              Create Custom
            </TabsTrigger>
          </TabsList>

          <TabsContent value='browse' className='mt-0'>
            <MetricList />
          </TabsContent>

          <TabsContent value='create' className='mt-0'>
            <NewMetric />
          </TabsContent>
        </Tabs>
      </main>
    </div>
  );
}
