'use client'

import { useState, useEffect } from 'react';
import Head from 'next/head';
import {Metric} from "@/types/metric";

export default function Metrics() {
  const [metrics, setMetrics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchMetrics = async () => {
      const token = localStorage.getItem('authToken');
      try {
        const response = await fetch('/api/v1/metrics', {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }

        const data = await response.json();
        setMetrics(data);
        setLoading(false);
      } catch (err) {
        setError('Failed to load metrics. Please try again later.');
        setLoading(false);
        console.error('Error fetching metrics:', err);
      }
    };

    fetchMetrics();
  }, []);

  // Helper function to display labels if they exist
  const renderLabels = (metric: Metric) => {
    if (!metric.labels || Object.keys(metric.labels).length === 0) {
      return <span className="text-gray-600 italic">Numeric: {metric.minValue} to {metric.maxValue}</span>;
    }

    return (
        <div className="flex flex-wrap gap-2 mb-2">
          {Object.entries(metric.labels).map(([value, label]) => (
              <span key={value} className="bg-gray-100 px-2 py-1 rounded text-sm">
            {value}: {label}
          </span>
          ))}
        </div>
    );
  };

  return (
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        <Head>
          <title>Your Metrics</title>
          <meta name="description" content="View your mental health metrics" />
        </Head>

        <main>
          <h1 className="text-3xl font-bold mb-8 text-gray-800">Your Metrics</h1>

          {loading && <p className="text-center py-8">Loading metrics...</p>}

          {error && <p className="text-center py-8 text-red-600">{error}</p>}

          {!loading && !error && metrics.length === 0 && (
              <p className="text-center py-8">No metrics found.</p>
          )}

          {!loading && !error && metrics.length > 0 && (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {metrics.map((metric: Metric) => (
                    <div key={metric.id} className="border border-gray-200 rounded-lg p-6 bg-white shadow hover:shadow-md transition-all hover:-translate-y-1">
                      <h2 className="text-xl font-semibold mb-2 text-gray-800">{metric.name}</h2>
                      <p className="text-gray-600 mb-4 text-sm">{metric.description}</p>
                      <div className="mb-4 text-sm">
                        {renderLabels(metric)}
                        {metric.defaultValue !== null && (
                            <p className="text-gray-600 mt-2">Default: {metric.defaultValue}</p>
                        )}
                      </div>
                      <p className="text-xs text-gray-500 pt-3 border-t border-gray-100">
                        {metric.ownerId === 1 ? 'Default Metric' : 'Custom Metric'}
                      </p>
                    </div>
                ))}
              </div>
          )}
        </main>
      </div>
  );
}