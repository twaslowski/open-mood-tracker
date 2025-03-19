'use client'

import React, {useState} from "react";
import {Plus} from "lucide-react";
import MetricInput from "@/components/metric/MetricInput";
import {Metric} from "@/types/metric";

const Settings = () => {
  const [metrics, setMetrics] = useState([
    {
      id: 1,
      name: 'Mood',
      description: 'What is your mood like today?',
      labels: [
        {key: '2', value: 'Manic'},
        {key: '1', value: 'Hypomanic'},
        {key: '0', value: 'Neutral'},
        {key: '-1', value: 'Lightly Depressed'},
        {key: '-2', value: 'Depressed'}
      ],
      sortOrder: 'ASC'
    }
  ]);

  const handleAddMetric = () => {
    const newMetric = {
      id: Date.now(),
      name: '',
      description: '',
      labels: [],
      sortOrder: 'ascending'
    };
    setMetrics([...metrics, newMetric]);
  };

  const handleMetricChange = (updatedMetric: Metric) => {
    setMetrics(metrics.map(m => m.id === updatedMetric.id ? updatedMetric : m));
  };

  const handleDeleteMetric = (id: number) => {
    setMetrics(metrics.filter(metric => metric.id !== id));
  };

  return (
      <div className="max-w-3xl mx-auto p-4">
        <header className="mb-6">
          <h1 className="text-2xl font-bold text-gray-800 mb-2">Mood Tracker Settings</h1>
          <p className="text-gray-600">Configure the metrics you want to track for your mental
            health.</p>
        </header>

        <div className="space-y-4">
          {metrics.map(metric => (
              <MetricInput
                  key={metric.id}
                  metric={metric}
                  onChange={(updatedMetric) => handleMetricChange(updatedMetric)}
                  onDelete={() => handleDeleteMetric(metric.id)}
              />
          ))}

          <button
              onClick={handleAddMetric}
              className="w-full p-4 border-2 border-dashed rounded-lg text-center text-gray-500 hover:bg-gray-50 flex items-center justify-center"
          >
            <Plus size={20} className="mr-2"/> Add New Metric
          </button>
        </div>

        <div className="mt-8 flex justify-end">
          <button className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
            Save Settings
          </button>
        </div>
      </div>
  );
};

export default Settings;