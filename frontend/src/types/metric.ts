export interface MetricType {
  id: number;
  name: string;
  description: string;
  labels: Label[];
  minValue: number;
  maxValue: number;
  defaultValue: number;
  isDefault: boolean;
  ownerId: number;
  sortOrder: string; // ASC | DESC
  tracked: boolean;
  trackedMetricId: string | null;

  getDefaultValueAsString(): string;
}

export interface MetricCreation {
  name: string;
  description: string;
  labels: Label[] | null;
  minValue: number;
  maxValue: number;
  defaultValue: number;
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
  defaultValue: number;
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
      defaultValue: number,
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
    this.defaultValue = defaultValue;
    this.isDefault = isDefault;
    this.ownerId = ownerId;
    this.sortOrder = sortOrder;
    this.tracked = tracked;
    this.trackedMetricId = trackedMetricId;
  }

  getDefaultValueAsString = (): string => {
    return (
        this.labels.find((label) => label.value === this.defaultValue)?.label ||
        this.defaultValue.toString()
    );
  };
}
