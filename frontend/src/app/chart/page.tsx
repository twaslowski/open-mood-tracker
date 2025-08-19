'use client';

import { useEffect, useState } from 'react';

import { getToken } from '@/lib/helper';

import { ErrorBox } from '@/components/ui/error-box';

import MentalHealthChart from './Chart';

import { MetricData } from '@/types/recording-data_v2';

export default function Home() {
  const [chartData, setChartData] = useState<MetricData[]>([]);
  const [error, setError] = useState<boolean>(false);

  useEffect(() => {
    const authToken = getToken();
    fetch('/api/v2/records', {
      headers: {
        Authorization: 'Bearer ' + authToken,
      },
    })
      .then((res) => res.json())
      .then((data) => setChartData(data))
      .catch((_) => setError(true));
  }, []);

  return (
    <main className='min-h-screen bg-gray-50 pt-4 px-2'>
      {error ? (
        <ErrorBox title='Could not load chart data'>
          There was a problem loading or displaying the chart. Please try again later.
        </ErrorBox>
      ) : (
        <MentalHealthChart data={chartData} />
      )}
    </main>
  );
}
