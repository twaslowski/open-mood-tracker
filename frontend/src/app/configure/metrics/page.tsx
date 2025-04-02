'use client';

import {useEffect, useState} from 'react';

import {getToken} from '@/lib/helper';

import MetricList from '@/components/metric/MetricList';
import NewMetric from '@/components/metric/NewMetric';
import TrackedMetrics from '@/components/metric/TrackedMetrics';
import {Tabs, TabsContent, TabsList, TabsTrigger} from '@/components/ui/tabs';

import {Metric, MetricType} from '@/types/metric';

export default function Metrics() {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [activeMetrics, setActiveMetrics] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

const fetchMetrics = (authToken: string): Promise<Metric[]> => {
  return fetch('/api/v1/metrics', {
    headers: {
      Authorization: 'Bearer ' + authToken,
    },
  })
    .then((response) => response.json() as Promise<Metric[]>)
    .catch((error) => {
      throw new Error(error.message)
    });
};
  useEffect(() => {
    getToken()
      .then((token) => fetchMetrics(token))
      .then((data) => {
        setMetrics(data);
        setActiveMetrics(data.map((metric) => metric.id));
      })
      .catch((error) => setError(error.message))
      .finally(() => setLoading(false));
  }, []);

  const toggleActiveMetric = (metricId: number) => {
    setActiveMetrics((prevActiveMetrics) => {
      if (prevActiveMetrics.includes(metricId)) {
        return prevActiveMetrics.filter((id) => id !== metricId);
      } else {
        return [...prevActiveMetrics, metricId];
      }
    });
  };

  const handleCreateMetric = async (newMetricData: Metric) => {
    try {
      const token = await getToken();
      const response = await fetch('/api/v1/metrics', {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + token,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...newMetricData,
          defaultMetric: false,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to create metric');
      }

      const createdMetric: MetricType = await response.json();

      // Add the new metric to the list
      const newMetricObject = new Metric(
        createdMetric.id,
        createdMetric.name,
        createdMetric.description,
        createdMetric.labels,
        createdMetric.minValue,
        createdMetric.maxValue,
        createdMetric.defaultValue,
        false,
        createdMetric.ownerId,
        createdMetric.sortOrder as 'ASC' | 'DESC'
      );

      setMetrics([...metrics, newMetricObject]);

      // Also add to active metrics
      setActiveMetrics([...activeMetrics, createdMetric.id]);

      return true;
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Failed to create metric');
      return false;
    }
  };

  // Get active metrics for the "My Tracking" tab
  const trackedMetrics = metrics.filter((metric) => activeMetrics.includes(metric.id));

  return (
    <div className='container mx-auto px-4 py-8 max-w-7xl'>
      <main>
        <h1 className='text-3xl font-bold mb-4 text-gray-800'>Metrics Configuration</h1>
        <p className='text-gray-600 mb-8'>
          Configure which mental health metrics to track and create custom metrics.
        </p>

        <Tabs defaultValue='browse' className='w-full'>
          <TabsList className='grid w-full grid-cols-3 mb-8'>
            <TabsTrigger value='browse'>Browse Metrics</TabsTrigger>
            <TabsTrigger value='tracking'>My Tracking</TabsTrigger>
            <TabsTrigger value='create'>Create Custom</TabsTrigger>
          </TabsList>

          {/* Browse Metrics Tab */}
          <TabsContent value='browse' className='mt-0'>
            <MetricList
              metrics={metrics}
              activeMetrics={activeMetrics}
              toggleActiveMetric={toggleActiveMetric}
              loading={loading}
              error={error}
            />
          </TabsContent>

          {/* My Tracking Tab */}
          <TabsContent value='tracking' className='mt-0'>
            <TrackedMetrics
              trackedMetrics={trackedMetrics}
              toggleActiveMetric={toggleActiveMetric}
              loading={loading}
              error={error}
            />
          </TabsContent>

          {/* Create Custom Tab */}
          <TabsContent value='create' className='mt-0'>
            <NewMetric onCreateMetric={handleCreateMetric} />
          </TabsContent>
        </Tabs>
      </main>
    </div>
  );
}
