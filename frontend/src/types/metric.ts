export interface Metric {
  id: number;
  name: string;
  description: string;
  labels: { numeric: string; label: string }[];
  minValue: number;
  maxValue: number;
  defaultValue: number;
  ownerId: number;
  sortOrder: string; // ASC | DESC
}