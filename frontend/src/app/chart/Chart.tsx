'use client';

import { format } from 'date-fns';
import { useMemo, useState } from 'react';
import {
  CartesianGrid,
  Legend,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';

import { Record } from '@/types/record';

interface Props {
  data: Record[];
}

const COLORS = ['#4c8cff', '#ff6b6b', '#1dd1a1', '#feca57', '#ff9ff3'];

export default function MentalHealthChart({ data }: Props) {
  const [selectedMonth, setSelectedMonth] = useState<string>(format(new Date(), 'yyyy-MM'));
  const [activeMetric, setActiveMetric] = useState<number>(1);

  const filteredData = useMemo(() => {
    return data
      .filter((record) => format(new Date(record.timestamp), 'yyyy-MM') === selectedMonth)
      .map((record) => {
        const result: any = { timestamp: format(new Date(record.timestamp), 'MMM d') };
        record.datapoints.forEach((dp) => {
          result[dp.metricName] = dp.datapointValue;
        });
      return result;
    });
  }, [data, selectedMonth]);

  const allMetrics = useMemo(() => {
    const metricMap = new Map<number, string>();
    data.forEach((record) =>
      record.datapoints.forEach((dp) => metricMap.set(dp.metricId, dp.metricName)),
    );
    return Array.from(metricMap.entries());
  }, [data]);

  return (
    <div className='p-4 bg-white rounded-2xl shadow-md'>
      <div className='flex justify-between items-center mb-4'>
        <select
          className='border rounded px-2 py-1'
          value={selectedMonth}
          onChange={(e) => setSelectedMonth(e.target.value)}
        >
          {/* @ts-expect-error: typing issues related to es5/es6?? */}
          {[...new Set(data.map((d) => format(new Date(d.timestamp), 'yyyy-MM')))].map((month) => (
            <option key={month} value={month}>
              {month}
            </option>
          ))}
        </select>

        <div className='flex gap-2 flex-wrap'>
          {allMetrics.map(([id, name], idx) => (
            <button
              key={id}
              className={`px-3 py-1 rounded-full text-sm border transition ${
                activeMetric === id
                  ? `bg-${COLORS[idx]} text-gray-700`
                  : 'bg-gray-100 text-gray-700'
              }`}
              onClick={() => setActiveMetric(id)}
            >
              {name}
            </button>
          ))}
        </div>
      </div>

      <ResponsiveContainer width='100%' height={400}>
        <LineChart data={filteredData}>
          <CartesianGrid strokeDasharray='3 3' />
          <XAxis dataKey='timestamp' />
          <YAxis />
          <Tooltip />
          <Legend />
          {allMetrics.map(([id, name], idx) =>
            activeMetric === id ? (
              <>
                <Line key={id} type='monotone' dataKey={name} stroke={`${COLORS[idx]}`} dot={false} />
                <Line
                  key={`${id}-baseline`}
                  type='monotone'
                  name='Baseline'
                  dataKey={() => {
                    const metric = data[0]?.datapoints.find((dp) => dp.metricId === id);
                    return metric?.baselineValue ?? 0;
                  }}
                  stroke='#e67a7d'
                  dot={false}
                  legendType='none'
                />
              </>
            ) : null,
          )}
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
