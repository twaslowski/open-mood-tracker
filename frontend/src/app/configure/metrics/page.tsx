'use client';

import { useEffect, useState } from 'react';

import { getToken } from '@/lib/helper';

import MetricCard from '@/components/metric/MetricCard';

import { Metric, MetricType } from '@/types/metric';

export default function Metrics() {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchMetrics = async (authToken: string): Promise<Metric[]> => {
    const response = await fetch('/api/v1/metrics', {
      headers: {
        Authorization: 'Bearer ' + authToken,
      },
    });
    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }
    const data: MetricType[] = await response.json(); // API response matches the interface
    return data.map(
      (metric) =>
        new Metric(
          metric.id,
          metric.name,
          metric.description,
          metric.labels,
          metric.minValue,
          metric.maxValue,
          metric.defaultValue,
          metric.defaultMetric,
          metric.ownerId,
          metric.sortOrder as 'ASC' | 'DESC'
        )
    );
  };

  useEffect(() => {
    getToken()
      .then((token) => fetchMetrics(token))
      .then((data) => setMetrics(data))
      .catch((error) => setError(error.message))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className='container mx-auto px-4 py-8 max-w-7xl'>
      <main>
        <h1 className='text-3xl font-bold mb-8 text-gray-800'>Your Metrics</h1>

        {loading && <p className='text-center py-8'>Loading metrics...</p>}

        {error && <p className='text-center py-8 text-red-600'>{error}</p>}

        {!loading && !error && metrics.length === 0 && (
          <p className='text-center py-8'>No metrics found.</p>
        )}

        {!loading && !error && metrics.length > 0 && (
          <div className='grid grid-cols-1 gap-6 items-center justify-start w-2/5 mx-auto'>
            {metrics.map((metric: Metric) => (
              <MetricCard key={metric.id} metric={metric} />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
