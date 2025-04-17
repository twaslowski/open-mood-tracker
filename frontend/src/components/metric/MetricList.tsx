'use client';

import {Filter, Search} from 'lucide-react';
import {useEffect, useState} from 'react';

import {getToken} from "@/lib/helper";
import {fetchAllMetrics} from "@/lib/metric";

import MetricCard from '@/components/metric/MetricCard';
import {Card, CardContent} from '@/components/ui/card';
import {Checkbox} from '@/components/ui/checkbox';
import {Input} from '@/components/ui/input';

import {Metric} from '@/types/metric';

export default function MetricList() {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [showDefault, setShowDefault] = useState(true);
  const [showCustom, setShowCustom] = useState(true);
  const [sortCriteria, _setSortCriteria] = useState('name');

  useEffect(() => {
    const token = getToken()
    if (token) {
      fetchAllMetrics(token)
          .then((data) => {
            setMetrics(data);
          })
          .catch((error) => setError(error.message))
          .finally(() => setLoading(false));
    }
  }, []);


  const filteredMetrics = metrics.filter((metric) => {
    const matchesSearch =
        metric.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        metric.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesFilter =
        (metric.isDefault && showDefault) || (!metric.isDefault && showCustom);
    return matchesSearch && matchesFilter;
  });

  return (
      <Card>
        <CardContent className='pt-6'>
          <div className='mb-6 flex flex-col md:flex-row gap-4 justify-between'>
            <div className='relative flex-grow max-w-md'>
              <div className='absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none'>
                <Search className='w-5 h-5 text-gray-400'/>
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
                <Filter className='w-5 h-5 text-gray-500'/>
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
                {filteredMetrics
                    .sort((a, b) => {
                      // Example sorting logic based on a state variable
                      if (sortCriteria === 'name') {
                        return a.name.localeCompare(b.name);
                      } else if (sortCriteria === 'baseline') {
                        return a.baseline - b.baseline;
                      } else if (sortCriteria === 'tracked') {
                        return (a.tracked === b.tracked) ? 0 : a.tracked ? -1 : 1;
                      }
                      return 0;
                    })
                    .map((metric: Metric) => (
                        <div key={metric.id} className='relative'>
                          <MetricCard metric={metric} onMetricUpdate={(updatedMetric) => {
                            setMetrics((prevMetrics) =>
                                prevMetrics.map((m) => (m.id === updatedMetric.id ? updatedMetric : m))
                            )
                          }
                          }/>
                        </div>
                    ))}
              </div>
          )}
        </CardContent>
      </Card>
  );
}
