'use client';

import { Check, Filter, Plus, Search } from 'lucide-react';
import { useState } from 'react';

import MetricCard from '@/components/metric/MetricCard';
import { Card, CardContent } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
import { Input } from '@/components/ui/input';

import { Metric } from '@/types/metric';

interface MetricListProps {
  metrics: Metric[];
  activeMetrics: number[];
  toggleActiveMetric: (metricId: number) => void;
  loading: boolean;
  error: string;
}

export default function MetricList({
  metrics,
  activeMetrics,
  toggleActiveMetric,
  loading,
  error,
}: MetricListProps) {
  const [searchTerm, setSearchTerm] = useState('');
  const [showDefault, setShowDefault] = useState(true);
  const [showCustom, setShowCustom] = useState(true);

  // Filter metrics based on search and filters
  const filteredMetrics = metrics.filter((metric) => {
    const matchesSearch =
      metric.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      metric.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesFilter =
      (metric.defaultMetric && showDefault) || (!metric.defaultMetric && showCustom);
    return matchesSearch && matchesFilter;
  });

  return (
    <Card>
      <CardContent className='pt-6'>
        {/* Search and filters */}
        <div className='mb-6 flex flex-col md:flex-row gap-4 justify-between'>
          <div className='relative flex-grow max-w-md'>
            <div className='absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none'>
              <Search className='w-5 h-5 text-gray-400' />
            </div>
            <Input
              type='text'
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className='w-full pl-10'
              placeholder='Search metrics...'
            />
          </div>

          <div className='flex items-center gap-4'>
            <div className='flex items-center gap-2'>
              <Filter className='w-5 h-5 text-gray-500' />
              <span className='text-sm text-gray-600'>Filter:</span>
            </div>
            <div className='flex items-center space-x-2'>
              <Checkbox
                id='default'
                checked={showDefault}
                onCheckedChange={() => setShowDefault(!showDefault)}
              />
              <label
                htmlFor='default'
                className='text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70'
              >
                Default
              </label>
            </div>
            <div className='flex items-center space-x-2'>
              <Checkbox
                id='custom'
                checked={showCustom}
                onCheckedChange={() => setShowCustom(!showCustom)}
              />
              <label
                htmlFor='custom'
                className='text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70'
              >
                Custom
              </label>
            </div>
          </div>
        </div>

        {loading && <p className='text-center py-8'>Loading metrics...</p>}

        {error && <p className='text-center py-8 text-red-600'>{error}</p>}

        {!loading && !error && filteredMetrics.length === 0 && (
          <p className='text-center py-8'>No metrics found matching your criteria.</p>
        )}

        {!loading && !error && filteredMetrics.length > 0 && (
          <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
            {filteredMetrics.map((metric: Metric) => (
              <div key={metric.id} className='relative'>
                <MetricCard metric={metric} />
                <button
                  onClick={() => toggleActiveMetric(metric.id)}
                  className={`absolute top-4 right-4 p-1 rounded-full ${
                    activeMetrics.includes(metric.id)
                      ? 'bg-green-100 text-green-600'
                      : 'bg-gray-100 text-gray-400'
                  }`}
                  title={
                    activeMetrics.includes(metric.id)
                      ? 'Active - Click to remove'
                      : 'Inactive - Click to add'
                  }
                >
                  {activeMetrics.includes(metric.id) ? (
                    <Check className='h-6 w-6' />
                  ) : (
                    <Plus className='h-6 w-6' />
                  )}
                </button>
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
