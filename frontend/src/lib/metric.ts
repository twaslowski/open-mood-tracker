import { getToken } from '@/lib/helper';

import { Metric } from '@/types/metric';

export const fetchAllMetrics = async (authToken: string): Promise<Metric[]> => {
  return fetch('/api/v1/metric', {
    headers: {
      Authorization: 'Bearer ' + authToken,
    },
  })
    .then((response) => response.json() as Promise<Metric[]>)
    .catch((error) => {
      throw new Error(error.message);
    });
};

export const trackMetric = async (metricId: number): Promise<Metric> => {
  const authToken = getToken();
  return fetch(`/api/v1/metric/tracking/${metricId}`, {
    method: 'POST',
    headers: {
      Authorization: 'Bearer ' + authToken,
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error('Failed to track metric');
    }
    return response.json();
  });
};

export const untrackMetric = async (metricId: number): Promise<void> => {
  const authToken = getToken();
  await fetch(`/api/v1/metric/tracking/${metricId}`, {
    method: 'DELETE',
    headers: {
      Authorization: 'Bearer ' + authToken,
    },
  });
};

// export const submitMetric = async (metric: Metric): Promise<Metric> => {
//   const authToken = getToken();
//   return fetch('/api/v1/metric', {
//     method: 'POST',
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: 'Bearer ' + authToken,
//     },
//     body: JSON.stringify(metric),
//   }).then(response => response.json() as Promise<Metric>);
// }

export const setBaseline = async (metric: Metric, newBaseline: number): Promise<Metric> => {
  const authToken = getToken();
  metric.baseline = newBaseline;
  return fetch(`/api/v1/metric`, {
    method: 'PUT',
    body: JSON.stringify(metric),
    headers: {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + authToken,
    },
  })
    .then((response) => response.json() as Promise<Metric>)
    .catch((error) => error.message);
};
