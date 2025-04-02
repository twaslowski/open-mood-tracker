'use client';

import { ArrowUpDown, Plus, Trash } from 'lucide-react';
import React, { useState } from 'react';

import { Metric } from '@/types/metric';

interface MetricInputProps {
  metric: Metric;
  onChange: (metric: Metric) => void;
  onDelete: () => void;
}

const MetricInput = ({ metric, onChange, onDelete }: MetricInputProps) => {
  const [labels, setLabels] = useState(metric.labels || []);

  const handleAddKeyValue = () => {
    const newPairs = [...labels, { numeric: '', label: '' }];
    setLabels(newPairs);
    onChange({ ...metric });
  };

  const handleLabelEdit = (index: number, numeric: number, label: string) => {
    const newPairs: { numeric: string; label: string }[] = [...labels];
    newPairs[index][numeric] = label;
    setLabels(newPairs);
    onChange({ ...metric });
  };

  const handleRemoveKeyValue = (index: number) => {
    const newPairs = labels.filter((_, i) => i !== index);
    setLabels(newPairs);
    onChange({ ...metric });
  };

  return (
    <div className='p-4 mb-4 border rounded-lg bg-white shadow-sm'>
      <div className='flex justify-between items-center mb-4'>
        <h3 className='text-lg font-medium'>{metric.name || 'New Metric'}</h3>
        <button
          onClick={onDelete}
          className='p-1 rounded-full hover:bg-gray-100'
          aria-label='Delete metric'
        >
          <Trash size={16} className='text-red-500' />
        </button>
      </div>

      <div className='space-y-4'>
        {/* Name input */}
        <div>
          <label className='block text-sm font-medium text-gray-700 mb-1'>Name</label>
          <input
            type='text'
            value={metric.name || ''}
            onChange={(e) => onChange({ ...metric, name: e.target.value })}
            className='w-full p-2 border rounded-md'
            placeholder='e.g., Anxiety Level'
          />
        </div>

        {/* Description input */}
        <div>
          <label className='block text-sm font-medium text-gray-700 mb-1'>Description</label>
          <textarea
            value={metric.description || ''}
            onChange={(e) => onChange({ ...metric, description: e.target.value })}
            className='w-full p-2 border rounded-md'
            placeholder='e.g., Track your daily anxiety levels'
            rows={2}
          />
        </div>

        {/* Sort order selection */}
        <div>
          <label className='block text-sm font-medium text-gray-700 mb-1'>Sort Order</label>
          <div className='flex items-center'>
            <ArrowUpDown size={16} className='mr-2 text-gray-500' />
            <select
              value={metric.sortOrder || 'ascending'}
              onChange={(e) => onChange({ ...metric, sortOrder: e.target.value })}
              className='p-2 border rounded-md'
            >
              <option value='ascending'>Ascending (Lower is better)</option>
              <option value='descending'>Descending (Higher is better)</option>
            </select>
          </div>
        </div>

        {/* Key-value pairs */}
        <div>
          <label className='block text-sm font-medium text-gray-700 mb-2'>Rating Scale</label>
          <div className='space-y-2'>
            {labels.map((values, index) => (
              <div key={index} className='flex items-center gap-2'>
                <input
                  type='number'
                  value={values.numeric}
                  onChange={(e) => handleLabelEdit(index, parseInt(values.numeric), e.target.value)}
                  className='w-16 p-2 border rounded-md'
                  placeholder='1'
                />
                <span className='text-gray-500'>:</span>
                <input
                  type='text'
                  value={values.label}
                  onChange={(e) => handleLabelEdit(index, parseInt(values.label), e.target.value)}
                  className='flex-1 p-2 border rounded-md'
                  placeholder='e.g., Mild'
                />
                <button
                  onClick={() => handleRemoveKeyValue(index)}
                  className='p-1 rounded-full hover:bg-gray-100'
                  aria-label='Remove key-value pair'
                >
                  <Trash size={16} className='text-gray-500' />
                </button>
              </div>
            ))}
            <button
              onClick={handleAddKeyValue}
              className='flex items-center text-sm text-blue-600 hover:text-blue-800'
            >
              <Plus size={16} className='mr-1' /> Add Rating Level
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
export default MetricInput;
