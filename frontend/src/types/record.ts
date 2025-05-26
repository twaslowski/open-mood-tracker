import { Datapoint } from '@/types/datapoint';

export interface Record {
  timestamp: string;
  datapoints: Datapoint[];
}
