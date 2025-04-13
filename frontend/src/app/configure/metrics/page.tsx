'use client';

import { useEffect, useState } from 'react';

import { getToken } from '@/lib/helper';
import { fetchAllMetrics } from '@/lib/metric';

import MetricList from '@/components/metric/MetricList';
import NewMetric from '@/components/metric/NewMetric';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

import { Metric } from '@/types/metric';

export default function Metrics() {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  return (
    <div className='container mx-auto px-4 py-8 max-w-7xl'>
      <main>
        <h1 className='text-3xl font-bold mb-4 text-gray-800'>Metrics Configuration</h1>
        <p className='text-gray-600 mb-8'>
          Configure which mental health metrics to track and create custom metrics.
        </p>

        <Tabs defaultValue='browse' className='w-full'>
          <TabsList className='grid w-full grid-cols-2 mb-8'>
            <TabsTrigger value='browse'>Browse Metrics</TabsTrigger>
            <TabsTrigger value='create' disabled>Create Custom</TabsTrigger>
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
