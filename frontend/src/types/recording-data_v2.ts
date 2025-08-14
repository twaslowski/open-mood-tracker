export interface MetricData {
  metricId: string;
  name: string;
  labels: Label[];
  tracked: boolean;
  hasData: boolean;
  minValue: number;
  maxValue: number;
  baseline: number;
  trackingData: Datapoint[];
}

interface Label {
  name: string;
  value: string;
}

export interface Datapoint {
  timestamp: string;
  value: number;
}
