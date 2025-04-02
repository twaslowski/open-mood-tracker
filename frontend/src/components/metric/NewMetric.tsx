'use client';

import { PlusCircle, X } from 'lucide-react';
import { useState } from 'react';

import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';

interface NewMetricProps {
  onCreateMetric: (metricData: any) => Promise<boolean>;
}

export default function NewMetric({ onCreateMetric }: NewMetricProps) {
  const [newMetric, setNewMetric] = useState({
    name: '',
    description: '',
    labels: [''],
    minValue: 1,
    maxValue: 10,
    defaultValue: 5,
  });

  const handleAddLabel = () => {
    setNewMetric({
      ...newMetric,
      labels: [...newMetric.labels, ''],
    });
  };

  const handleLabelChange = (index: number, value: string) => {
    const updatedLabels = [...newMetric.labels];
    updatedLabels[index] = value;
    setNewMetric({
      ...newMetric,
      labels: updatedLabels,
    });
  };

  const handleRemoveLabel = (index: number) => {
    const updatedLabels = newMetric.labels.filter((_, i) => i !== index);
    setNewMetric({
      ...newMetric,
      labels: updatedLabels,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const success = await onCreateMetric(newMetric);

    if (success) {
      // Reset form on success
      setNewMetric({
        name: '',
        description: '',
        labels: [''],
        minValue: 1,
        maxValue: 10,
        defaultValue: 5,
      });
    }
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
                value={newMetric.defaultValue}
                onChange={(e) =>
                  setNewMetric({ ...newMetric, defaultValue: parseInt(e.target.value) })
                }
                className='w-full'
              />
            </div>
          </div>

          <div>
            <label className='block text-sm font-medium text-gray-700 mb-2'>
              Labels (Optional)
            </label>
            <p className='text-xs text-gray-500 mb-3'>
              Add labels for each value point (e.g., "1: Very Low", "10: Very High")
            </p>

            {newMetric.labels.map((label, index) => (
              <div key={index} className='flex items-center mb-2'>
                <div className='w-10 text-right mr-2 text-sm text-gray-500'>
                  {newMetric.minValue + index}:
                </div>
                <Input
                  type='text'
                  value={label}
                  onChange={(e) => handleLabelChange(index, e.target.value)}
                  className='flex-grow'
                  placeholder={`Label for value ${newMetric.minValue + index}`}
                />
                {newMetric.labels.length > 1 && (
                  <Button
                    type='button'
                    variant='ghost'
                    size='icon'
                    onClick={() => handleRemoveLabel(index)}
                    className='ml-2 text-red-500 hover:text-red-700 h-8 w-8'
                  >
                    <X className='h-4 w-4' />
                  </Button>
                )}
              </div>
            ))}

            {newMetric.labels.length < newMetric.maxValue - newMetric.minValue + 1 && (
              <Button
                type='button'
                variant='ghost'
                onClick={handleAddLabel}
                className='inline-flex items-center mt-2 text-sm p-0 h-auto'
              >
                <PlusCircle className='h-4 w-4 mr-1' />
                Add another label
              </Button>
            )}
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
