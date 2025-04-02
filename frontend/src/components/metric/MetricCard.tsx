import React from 'react';

import { Metric } from '@/types/metric';

type MetricCardProps = {
  metric: Metric;
};

const MetricCard: React.FC<MetricCardProps> = ({ metric }) => {
  const isDefaultValue = (labelValue: number) => {
    return metric.defaultValue === labelValue;
  };

  const renderLabels = (metric: Metric) => {
    return (
      <div className='flex flex-wrap gap-2 mb-2'>
        {metric.labels.map((label) => (
          <span
            key={label.value}
            className={`px-2 py-1 rounded-lg text-s 
          ${
            isDefaultValue(label.value) ? 'bg-blue-100 text-blue-800' : 'bg-gray-100 text-gray-800'
          }`}
          >
            {label.label}
          </span>
        ))}
      </div>
    );
  };

  return (
    <div
      key={metric.id}
      className='border border-gray-200 rounded-lg p-6 bg-white shadow hover:shadow-md transition-all hover:-translate-y-1'
    >
      <h2 className='text-xl font-semibold mb-2 text-gray-800'>{metric.name}</h2>
      <p className='text-gray-600 mb-4 text-sm'>{metric.description}</p>
      <div className='mb-4 text-sm'>{renderLabels(metric)}</div>
      <p className='text-xs text-gray-500 pt-3 border-t border-gray-100'>
        {metric.defaultMetric ? 'Default Metric' : 'Custom Metric'}
      </p>
    </div>
  );
};

export default MetricCard;
