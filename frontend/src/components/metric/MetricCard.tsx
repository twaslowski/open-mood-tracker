import React from "react";

import {setBaseline} from "@/lib/metric";

import TrackingButton from "@/components/metric/TrackingButton";

import {Metric} from '@/types/metric';

type MetricCardProps = {
  metric: Metric;
  onMetricUpdate: (metric: Metric) => void;
};

const MetricCard: React.FC<MetricCardProps> = ({metric, onMetricUpdate}) => {
  const [currentMetric, setCurrentMetric] = React.useState<Metric>(metric);

  const isBaseline = (value: number) => {
    return value === metric.baseline;
  };

  const deriveBaselineClassName = (value: number) => {
    const nonBaseline = 'bg-gray-100 text-gray-800';
    const baselineTracked = 'bg-blue-100 text-blue-800';
    const baselineUntracked = 'bg-blue-50 text-gray-800';
    if (isBaseline(value)) {
      if (metric.tracked) {
        return 'px-2 py-1 rounded-lg text-s ' + baselineTracked;
      } else {
        return 'px-2 py-1 rounded-lg text-s ' + baselineUntracked;
      }
    }
    return 'px-2 py-1 rounded-lg text-s ' + nonBaseline;
  }

  const renderLabels = (metric: Metric) => {
    return (
        <div className='flex flex-wrap gap-2 mb-2'>
          {metric.labels.map((label) => (
              <button
                  key={label.value}
                  onClick={() => {
                    setBaseline(metric, label.value).then((metric) => {
                      setCurrentMetric(metric)
                      onMetricUpdate(metric);
                    });
                  }}
                  className={deriveBaselineClassName(label.value)}>
            {label.label}
          </button>
          ))}
        </div>
    );
  };

  return (
      <div
          key={currentMetric.id}
          className='border border-gray-200 rounded-lg p-6 bg-white shadow hover:shadow-md transition-all hover:-translate-y-1'
      >
        <div className='flex items-center justify-between'>
          <h2 className='text-xl font-semibold mb-2 text-gray-800'>{currentMetric.name}</h2>
          <TrackingButton metric={currentMetric} />
        </div>
        <p className='text-gray-600 mb-4 text-sm'>{currentMetric.description}</p>
        <div className='mb-4 text-sm'>{renderLabels(currentMetric)}</div>
        <p className='text-xs text-gray-500 pt-3 border-t border-gray-100'>
          {currentMetric.isDefault ? 'Default Metric' : 'Custom Metric'}
        </p>
      </div>
  );
};

export default MetricCard;
