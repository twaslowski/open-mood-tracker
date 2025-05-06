'use client';

import { Check, Plus } from 'lucide-react';
import React, { useState } from 'react';

import { trackMetric, untrackMetric } from '@/lib/metric';

import { Metric } from '@/types/metric';

type MetricProps = {
  metric: Metric;
};

const TrackingButton: React.FC<MetricProps> = ({ metric }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [isTracked, setIsTracked] = useState(metric.tracked);
  const [trackedMetricId, setTrackedMetricId] = useState(metric.trackedMetricId);
  const [_metricId, setMetricId] = useState(metric.id);

  const toggleTracking = () => {
    setIsLoading(true);
    if (isTracked) {
      if (!trackedMetricId) {
        setIsLoading(false);
        throw new Error('trackedMetricId should be defined, but was not');
      }
      untrackMetric(trackedMetricId).then(() => {
        setIsTracked(false);
        setTrackedMetricId('');
        setIsLoading(false);
      });
    } else {
      trackMetric(metric.id).then((response) => {
        setIsTracked(true);
        setTrackedMetricId(response.trackedMetricId);
        setMetricId(response.id);
        setIsLoading(false);
      });
    }
  };

  return (
    <div>
      {isLoading && (
        <button disabled>
          <Plus className='h-6 w-6 bg-gray-100 text-gray-600 rounded-full animate-spin' />
        </button>
      )}
      {!isLoading && isTracked && (
        <button onClick={toggleTracking}>
          <Check className='h-6 w-6 bg-blue-100 text-blue-600 rounded-full' />
        </button>
      )}
      {!isLoading && !isTracked && (
        <button onClick={toggleTracking}>
          <Plus className='h-6 w-6 bg-green-100 text-green-600 rounded-full' />
        </button>
      )}
    </div>
  );
};

export default TrackingButton;
