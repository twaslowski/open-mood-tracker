export interface MetricType {
  id: number;
  name: string;
  description: string;
  labels: Label[];
  minValue: number;
  maxValue: number;
  baseline: number;
  isDefault: boolean;
  ownerId: number;
  sortOrder: string; // ASC | DESC
  tracked: boolean;
  trackedMetricId: string | null;
}

export interface MetricCreation {
  name: string;
  description: string;
  labels: Label[] | null;
  minValue: number;
  maxValue: number;
  baseline: number;
  tracked: boolean;
}

interface Label {
  label: string;
  value: number;
}

export class Metric implements MetricType {
  id: number;
  name: string;
  description: string;
  labels: Label[];
  minValue: number;
  maxValue: number;
  baseline: number;
  isDefault: boolean;
  ownerId: number;
  sortOrder: 'ASC' | 'DESC';
  tracked: boolean;
  trackedMetricId: string | null;

  constructor(
      id: number,
      name: string,
      description: string,
      labels: Label[],
      minValue: number,
      maxValue: number,
      baseline: number,
      isDefault: boolean,
      ownerId: number,
      sortOrder: 'ASC' | 'DESC',
      tracked = false,
      trackedMetricId: string | null = null
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.labels = labels;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.baseline = baseline;
    this.isDefault = isDefault;
    this.ownerId = ownerId;
    this.sortOrder = sortOrder;
    this.tracked = tracked;
    this.trackedMetricId = trackedMetricId;
  }
}
