'use client';

import { ArrowLeft } from 'lucide-react';

import MetricList from '@/components/metric/MetricList';
import NewMetric from '@/components/metric/NewMetric';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

export default function Metrics() {
  return (
    <div className='page-gradient-bg flex-col gradient-cyan mx-auto px-4 py-8'>
      <main>
        <a href='/configure'>
          <button className='flex items-center text-blue-600 hover:underline mb-4'>
            <ArrowLeft />
            Back to Configuration
          </button>
        </a>
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
