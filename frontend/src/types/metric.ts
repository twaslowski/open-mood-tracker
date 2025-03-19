export interface Metric {
  id: number;
  name: string;
  description: string;
  labels: { numeric: string; label: string }[];
  sortOrder: string; // ASC | DESC
}