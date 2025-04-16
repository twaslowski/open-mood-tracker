'use client';

import React, {FormEvent, useState} from 'react';

import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import {Checkbox} from "@/components/ui/checkbox";
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';


export default function NewMetric() {
  const [useLabels, setUseLabels] = useState(false);
  const [newMetric, setNewMetric] = useState({
    id: 0,
    name: '',
    description: '',
    labels: [] as Array<{ label: string; value: number }>,
    minValue: 1,
    maxValue: 3,
    baseline: 2,
    tracked: false,
  });

  const handleLabelChange = (value: number, label: string) => {
    const updatedLabels = [...newMetric.labels];
    updatedLabels[value] = {
      "label": label,
      "value": value,
    };
    setNewMetric({
      ...newMetric,
      labels: updatedLabels,
    });
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
  };

  return (
    <Card>
      <CardContent className='pt-6'>
        <h2 className='text-xl font-semibold mb-6'>Create a Custom Metric</h2>

        <form onSubmit={handleSubmit} className='space-y-6'>
          <div>
            <label htmlFor='name' className='block text-sm font-medium text-gray-700 mb-1'>
              Metric Name *
            </label>
            <Input
              type='text'
              id='name'
              value={newMetric.name}
              onChange={(e) => setNewMetric({ ...newMetric, name: e.target.value })}
              className='w-full'
              placeholder='e.g., Anxiety Level'
              required
            />
          </div>

          <div>
            <label htmlFor='description' className='block text-sm font-medium text-gray-700 mb-1'>
              Description *
            </label>
            <Textarea
              id='description'
              value={newMetric.description}
              onChange={(e) => setNewMetric({ ...newMetric, description: e.target.value })}
              rows={3}
              className='w-full'
              placeholder="Describe what this metric measures and why it's important to track"
              required
            />
          </div>

          <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
            <div>
              <label htmlFor='minValue' className='block text-sm font-medium text-gray-700 mb-1'>
                Minimum Value
              </label>
              <Input
                type='number'
                id='minValue'
                value={newMetric.minValue}
                onChange={(e) => setNewMetric({ ...newMetric, minValue: parseInt(e.target.value) })}
                className='w-full'
              />
            </div>

            <div>
              <label htmlFor='maxValue' className='block text-sm font-medium text-gray-700 mb-1'>
                Maximum Value
              </label>
              <Input
                type='number'
                id='maxValue'
                value={newMetric.maxValue}
                onChange={(e) => setNewMetric({ ...newMetric, maxValue: parseInt(e.target.value) })}
                className='w-full'
              />
            </div>

            <div>
              <label
                htmlFor='defaultValue'
                className='block text-sm font-medium text-gray-700 mb-1'
              >
                Default Value
              </label>
              <Input
                type='number'
                id='defaultValue'
                value={newMetric.baseline}
                onChange={(e) =>
                  setNewMetric({ ...newMetric, baseline: parseInt(e.target.value) })
                }
                className='w-full'
              />
            </div>
          </div>

          <div>
            <div className='flex mb-2 space-x-2'>
              <p className='text-md font-medium text-black'>Labels</p>
              <Checkbox className='mt-1'
                        checked={useLabels}
                        onCheckedChange={(checked) => setUseLabels(!!checked)} />
            </div>
            <p className='text-xs text-gray-500 mb-3'>
              Add labels for each value point (e.g., "1: Very Low", "10: Very High")
            </p>

            {useLabels && Array.from({ length: newMetric.maxValue - newMetric.minValue + 1 }, (_, index) => {
                const value = newMetric.minValue + index;
                const label = newMetric.labels.find((l) => l.value === value)?.label || '';
                return (
                    <div key={value} className='flex items-center mb-2'>
                      <div className='w-10 text-right mr-2 text-sm text-gray-500'>
                        {value}:
                      </div>
                      <Input
                          type='text'
                          value={label}
                          onChange={(e) => handleLabelChange(value, e.target.value)}
                          className='flex-grow'
                          placeholder={`Label for value ${value}`}
                      />
                    </div>
                );
              })}
          </div>

          <div className='pt-4'>
            <Button type='submit' className='w-full sm:w-auto'>
              Create Custom Metric
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  );
}
