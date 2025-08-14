'use client';

import { format } from 'date-fns';
import { useEffect, useMemo, useState } from 'react';
import {
  CartesianGrid,
  Line,
  LineChart,
  ReferenceArea,
  ReferenceLine,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';

import { MetricData } from '@/types/recording-data_v2';

interface Props {
  data: MetricData[];
}

interface ChartDataPoint {
  timestamp: string;
  date: string;
  [metricId: string]: string | number | null;
}

interface MetricInfo {
  id: string;
  name: string;
  color: string;

  baseline: number;
  minValue: number;
  maxValue: number;
}

const COLORS = [
  '#4c8cff',
  '#ff6b6b',
  '#1dd1a1',
  '#feca57',
  '#ff9ff3',
  '#8e44ad',
  '#e67e22',
  '#34495e',
];

export default function MentalHealthChart({ data }: Props) {
  // Extract all timestamps from all metrics
  const allTimestamps = useMemo(() => {
    return data.flatMap((metric) => metric.trackingData.map((dp) => dp.timestamp));
  }, [data]);

  // Get available months from all timestamps
  const availableMonths = useMemo(() => {
    return Array.from(new Set(allTimestamps.map((ts) => format(new Date(ts), 'yyyy-MM')))).sort();
  }, [allTimestamps]);

  // Set default to latest available month
  const [selectedMonth, setSelectedMonth] = useState<string>('');

  // Track visible metrics by their IDs
  const [visibleMetricIds, setVisibleMetricIds] = useState<Set<string>>(new Set());

  // Create metric info array with consistent color mapping
  const metricsInfo = useMemo((): MetricInfo[] => {
    return data.map((metric, index) => ({
      id: metric.metricId,
      name: metric.name,
      color: COLORS[index % COLORS.length],
      baseline: metric.baseline,
      minValue: metric.minValue,
      maxValue: metric.maxValue,
    }));
  }, [data]);

  useEffect(() => {
    if (availableMonths.length > 0 && !selectedMonth) {
      setSelectedMonth(availableMonths[availableMonths.length - 1]);
    }
  }, [availableMonths, selectedMonth]);

  // Initialize visible metrics when metricsInfo changes
  useEffect(() => {
    if (metricsInfo.length > 0 && visibleMetricIds.size === 0) {
      setVisibleMetricIds(new Set(metricsInfo.map((m) => m.id)));
    }
  }, [metricsInfo, visibleMetricIds.size]);

  // Navigation functions
  const goToPreviousMonth = () => {
    const currentIndex = availableMonths.indexOf(selectedMonth);
    if (currentIndex > 0) {
      setSelectedMonth(availableMonths[currentIndex - 1]);
    }
  };

  const goToNextMonth = () => {
    const currentIndex = availableMonths.indexOf(selectedMonth);
    if (currentIndex < availableMonths.length - 1) {
      setSelectedMonth(availableMonths[currentIndex + 1]);
    }
  };

  const canGoToPrevious = availableMonths.indexOf(selectedMonth) > 0;
  const canGoToNext = availableMonths.indexOf(selectedMonth) < availableMonths.length - 1;

  // Prepare chart data: group datapoints by day and metric, average if multiple per day
  const chartData = useMemo((): ChartDataPoint[] => {
    if (!selectedMonth) return [];

    // Filter metrics by selected month
    const filteredMetrics = data.map((metric) => ({
      ...metric,
      trackingData: metric.trackingData.filter(
        (dp) => format(new Date(dp.timestamp), 'yyyy-MM') === selectedMonth,
      ),
    }));

    // Collect all unique days in the selected month
    const allDays = Array.from(
      new Set(
        filteredMetrics.flatMap((metric) =>
          metric.trackingData.map((dp) => format(new Date(dp.timestamp), 'yyyy-MM-dd')),
        ),
      ),
    ).sort();

    // Build chart data points for each day
    return allDays.map((day) => {
      const dataPoint: ChartDataPoint = {
        timestamp: format(new Date(day), 'MMM d'),
        date: day,
      };

      // For each metric, calculate the average value for this day
      filteredMetrics.forEach((metric) => {
        const dayDatapoints = metric.trackingData.filter(
          (dp) => format(new Date(dp.timestamp), 'yyyy-MM-dd') === day,
        );

        if (dayDatapoints.length > 0) {
          dataPoint[metric.metricId] =
            dayDatapoints.reduce((sum, dp) => sum + dp.value, 0) / dayDatapoints.length;
        } else {
          dataPoint[metric.metricId] = null;
        }
      });

      return dataPoint;
    });
  }, [data, selectedMonth]);

  // Toggle metric visibility
  const toggleMetric = (metricId: string) => {
    const newVisibleMetrics = new Set(visibleMetricIds);
    if (newVisibleMetrics.has(metricId)) {
      newVisibleMetrics.delete(metricId);
    } else {
      newVisibleMetrics.add(metricId);
    }
    setVisibleMetricIds(newVisibleMetrics);
  };

  // Get the overall Y-axis range for proper scaling
  const yAxisDomain = useMemo(() => {
    if (metricsInfo.length === 0) return [0, 10];

    const allMinValues = metricsInfo.map((m) => m.minValue);
    const allMaxValues = metricsInfo.map((m) => m.maxValue);

    const globalMin = Math.min(...allMinValues);
    const globalMax = Math.max(...allMaxValues);

    // Add some padding
    const padding = (globalMax - globalMin) * 0.05;
    return [globalMin - padding, globalMax + padding];
  }, [metricsInfo]);

  // Get visible metrics info
  const visibleMetrics = metricsInfo.filter((metric) => visibleMetricIds.has(metric.id));

  return (
    <div className='p-4 bg-white rounded-2xl shadow-md'>
      <div className='flex justify-between items-center mb-4'>
        <div className='flex items-center gap-3'>
          <button
            onClick={goToPreviousMonth}
            disabled={!canGoToPrevious}
            className={`p-2 rounded-full border ${
              canGoToPrevious
                ? 'border-gray-300 hover:bg-gray-100 text-gray-700'
                : 'border-gray-200 text-gray-300 cursor-not-allowed'
            }`}
            aria-label='Previous month'
          >
            <svg className='w-4 h-4' fill='none' stroke='currentColor' viewBox='0 0 24 24'>
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                strokeWidth={2}
                d='M15 19l-7-7 7-7'
              />
            </svg>
          </button>

          <div className='text-lg font-medium min-w-[100px] text-center'>
            {selectedMonth ? format(new Date(selectedMonth + '-01'), 'MMM yyyy') : ''}
          </div>

          <button
            onClick={goToNextMonth}
            disabled={!canGoToNext}
            className={`p-2 rounded-full border ${
              canGoToNext
                ? 'border-gray-300 hover:bg-gray-100 text-gray-700'
                : 'border-gray-200 text-gray-300 cursor-not-allowed'
            }`}
            aria-label='Next month'
          >
            <svg className='w-4 h-4' fill='none' stroke='currentColor' viewBox='0 0 24 24'>
              <path strokeLinecap='round' strokeLinejoin='round' strokeWidth={2} d='M9 5l7 7-7 7' />
            </svg>
          </button>
        </div>

        <div className='text-sm text-gray-600'>Showing all metrics with daily averages</div>
      </div>

      <ResponsiveContainer width='100%' height={400}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray='3 3' />
          <XAxis dataKey='timestamp' />
          <YAxis
            label={{ value: 'Values', angle: -90, position: 'insideLeft' }}
            domain={yAxisDomain}
            tickFormatter={(value) => Math.round(value).toString()}
            allowDecimals={false}
          />
          <Tooltip />

          {/* Render min/max backdrops for visible metrics */}
          {visibleMetrics.map((metric) => (
            <ReferenceArea
              key={`backdrop-${metric.id}`}
              y1={metric.minValue}
              y2={metric.maxValue}
              fill={metric.color}
              fillOpacity={0.1}
              stroke='none'
            />
          ))}

          {/* Render lines */}
          {visibleMetrics.map((metric) => (
            <Line
              key={metric.id}
              type='monotone'
              dataKey={metric.id}
              stroke={metric.color}
              strokeWidth={2}
              dot={{ r: 3 }}
              name={metric.name}
              connectNulls={false}
            />
          ))}

          {/* Render baseline reference lines */}
          {visibleMetrics.map((metric) => (
            <ReferenceLine
              key={`baseline-${metric.id}`}
              y={metric.baseline}
              stroke={metric.color}
              strokeWidth={2}
              strokeDasharray='3 3'
              name={`${metric.name} Baseline`}
            />
          ))}
        </LineChart>
      </ResponsiveContainer>

      {/* Custom clickable legend */}
      <div className='mt-4 flex flex-wrap gap-2 justify-center'>
        {metricsInfo.map((metric) => (
          <button
            key={metric.id}
            onClick={() => toggleMetric(metric.id)}
            className={`px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200 border ${
              visibleMetricIds.has(metric.id)
                ? 'bg-white border-gray-300 text-gray-700 hover:bg-gray-50'
                : 'bg-gray-100 border-gray-200 text-gray-400 hover:bg-gray-150'
            }`}
          >
            <div className='flex items-center gap-2'>
              <div
                className={`w-3 h-3 rounded-full ${
                  visibleMetricIds.has(metric.id) ? 'opacity-100' : 'opacity-40'
                }`}
                style={{ backgroundColor: metric.color }}
              />
              {metric.name}
            </div>
          </button>
        ))}
      </div>
    </div>
  );
}
