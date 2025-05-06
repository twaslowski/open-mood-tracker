import { Label } from '@/types/label';

export interface Datapoint {
  metricId: number;
  metricName: string;
  metricDescription: string;
  minValue: number;
  maxValue: number;
  baselineValue: number;
  datapointValue: number;
  labels: Label[];
}
