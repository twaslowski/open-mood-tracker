'use client';

import { X } from 'lucide-react';

import MetricCard from '@/components/metric/MetricCard';
import { Card, CardContent } from '@/components/ui/card';

import { Metric } from '@/types/metric';

interface TrackedMetricsProps {
  trackedMetrics: Metric[];
  toggleActiveMetric: (metricId: number) => void;
  loading: boolean;
  error: string;
}

export default function TrackedMetrics({
  trackedMetrics,
  toggleActiveMetric,
  loading,
  error,
}: TrackedMetricsProps) {
  return (
    <Card>
      <CardContent className='pt-6'>
        <h2 className='text-xl font-semibold mb-6'>Metrics You're Currently Tracking</h2>

        {loading && <p className='text-center py-8'>Loading your tracked metrics...</p>}

        {error && <p className='text-center py-8 text-red-600'>{error}</p>}

        {!loading && !error && trackedMetrics.length === 0 && (
          <div className='text-center py-8'>
            <p className='text-gray-600 mb-4'>You're not tracking any metrics yet.</p>
            <p className='text-sm text-gray-500'>
              Go to the "Browse Metrics" tab to add metrics to track.
            </p>
          </div>
        )}

        {!loading && !error && trackedMetrics.length > 0 && (
          <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
            {trackedMetrics.map((metric: Metric) => (
              <div key={metric.id} className='relative'>
                <MetricCard metric={metric} />
                <button
                  onClick={() => toggleActiveMetric(metric.id)}
                  className='absolute top-4 right-4 p-1 rounded-full bg-red-100 text-red-600'
                  title='Remove from tracking'
                >
                  <X className='h-6 w-6' />
                </button>
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
